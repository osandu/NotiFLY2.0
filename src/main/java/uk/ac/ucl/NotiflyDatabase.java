/**
 * Copyright 2012 UCL 

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
 */
package uk.ac.ucl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DuplicateKeyException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class NotiflyDatabase {
		
	JdbcTemplate template;
	private volatile HashMap<String,ArrayList<UserMail>> hrMailsToSend,dayMailsToSend;
	private Timer perHourTimer, perDayTimer;
	private SimpleJdbcInsert noteInsert;
	@Autowired
	protected UserMailManager mailManager;
	private int REALTIME, HOUR, DAY;


	@Autowired
	public NotiflyDatabase(JdbcTemplate template)
	{
		this.template = template;
		this.noteInsert = new SimpleJdbcInsert(template); //needed to retrieve auto-generated keys to manage foreign keys
		this.hrMailsToSend = new HashMap<String,ArrayList<UserMail>>();
		this.dayMailsToSend = new HashMap<String,ArrayList<UserMail>>();
		noteInsert.setTableName("Notification");
		noteInsert.setGeneratedKeyNames(new String[] {"message_id","updated_on"});
		REALTIME = 0; HOUR = 1; DAY = 2;
	}
	
	//Notification table Insertions and Queries
	
	public boolean insertNotification(String message, String application, String categories)
	{
		Map<String,Object> parameters = new HashMap<String,Object>(2);
		parameters.put("message", message);
		parameters.put("application",application);
		try 
		{
			int p_key = noteInsert.executeAndReturnKey(parameters).intValue();
			Date now = new Date();
			if(!categories.equals("none"))
			{
				String[] splitCategories = categories.split(",");
				for(String s: splitCategories)
				{
					template.update("insert into notificationcategories(MESSAGE_ID,CATEGORY) values(?,?)",p_key,s);
				}	
				createEmails(message,application,splitCategories,now);
			}
			else createEmails(message,application,new String[]{},now);
			return true;
		}
		catch(Exception e) { return false; }		
	}
	
	public void createEmails(String message, String application, String[] splitCategories, Date now)
	{
		List<User> users = template.query("select user.*, subscription.* " +
				"from user " +
				"left join subscription on subscription.email_address = user.email_address",new UserSubscriptionExtractor());
		for(User user: users)
		{
			UserMail newMail = null;
			if(user.isSubscribedToApplication(application))
			{
				newMail = new UserMail(message,application,splitCategories,now.toString());
				newMail.userSubscribedToApplication();
			}
			for(String cat: splitCategories)
			{
				if(user.isSubscribedToCategory(cat)) 
				{
					if(newMail == null) newMail = new UserMail(message,application,splitCategories,now.toString());
					newMail.addToNotifyForCategories(cat);
				}
			}
			if(newMail != null) decideWhenToSend(newMail,user);
		}
	}
	
	public int checkAppFrequency(UserMail mail,User user)
	{
		String appFrequency = user.getApplicationFrequency(mail.getApplication());
		if(appFrequency == null) return -1;
		if(appFrequency.equals("Real-time")) return REALTIME;
		else if(appFrequency.equals("1 hour")) 	return HOUR;
		return DAY;
	}
	
	public void decideWhenToSend(UserMail mail, User user)
	{
		int appFreq = checkAppFrequency(mail,user);
		int catFreq = checkCatFrequencies(mail,user);
		if((appFreq == REALTIME) || (catFreq == REALTIME)) mailManager.emailNotification(user, mail);
		else if((appFreq == HOUR) || (catFreq == HOUR)) storeEmail(hrMailsToSend,user,mail);
		else storeEmail(dayMailsToSend,user,mail);
	}
	
	public int checkCatFrequencies(UserMail mail,User user)
	{
		boolean sendMailHr = false;
		boolean sendMailDay = false;
		if(mail.getNotifyForCategories() == null) return -1;
		for(String cat: mail.getNotifyForCategories())
		{
			String catFrequency = user.getCategoryFrequency(cat);
			if(catFrequency.equals("Real-time")) return REALTIME;
			else if(catFrequency.equals("1 hour")) sendMailHr = true;
			else if(catFrequency.equals("1 day")) sendMailDay = true;
		} 
		if(sendMailHr==true) return HOUR;
		if(sendMailDay==true) return DAY;
		sendMailHr = false; sendMailDay = false;
		return -1;
	}
	
	public void storeEmail(HashMap<String,ArrayList<UserMail>> map,User user, UserMail mail)
	{
		ArrayList<UserMail> mailsToSend = map.get(user.getEmailAddress());
		if(mailsToSend == null)
		{
			mailsToSend = new ArrayList<UserMail>();
		}
		mailsToSend.add(mail);
		map.put(user.getEmailAddress(), mailsToSend);
	}
	
	public void notifyUsers(HashMap<String,ArrayList<UserMail>> map)
	{
		Iterator<Map.Entry<String, ArrayList<UserMail>>> entries = map.entrySet().iterator();
		while(entries.hasNext()) {
			Map.Entry<String, ArrayList<UserMail>> entry = entries.next();
			User user = getUserByEmailWithSubscriptions(entry.getKey());
			for(UserMail mail: entry.getValue())
			{
				mailManager.emailNotification(user, mail);
			}
			entries.remove();
		}
	}
	
	public User getUserByEmail(String emailAddress)
	{
		return template.queryForObject("select email_address,password,first_name,last_name " +
				"from user " +
				"where email_address = ?",new Object[]{emailAddress}, new UserSubscriptionExtractor.UserMapper());
	}
	
	public boolean emailExists(String emailAddress)
	{
		String query = "select count(email_address) from user where email_address = ?";
		int count = template.queryForInt(query, emailAddress);
		if(count == 1) return true;
		return false;
	}
	
	public User getUserByEmailWithSubscriptions(String emailAddress)
	{
		return template.query("select user.*, subscription.* " +
				"from user " +
				"left join subscription on subscription.email_address = user.email_address " +
				"where user.email_address = ?",new Object[]{emailAddress}, new UserSubscriptionExtractor()).get(0);
	}
	
	//this creates two daemon threads that check on an hourly and daily basis if users need to be sent emails using the hashmap mailsToSend
	public void runTimers()
	{
		if(perHourTimer == null)
		{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY) + 1); //check at the next end of hour
			c.set(Calendar.MINUTE,0);
			c.set(Calendar.SECOND,0);
			perHourTimer = new Timer(true); //create a daemon thread
			perHourTimer.scheduleAtFixedRate(new TimerTask()
			{
				public void run()
				{
					//notifyUsers("1 hour");
					notifyUsers(hrMailsToSend);
				}
			},c.getTime(), 360000);		
		}
		if(perDayTimer == null)
		{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1); //set after midnight
			c.set(Calendar.HOUR_OF_DAY,0);
			c.set(Calendar.MINUTE,0);
			c.set(Calendar.SECOND,0);//send emails after midnight if user's frequency is 1 day
			perDayTimer = new Timer(true); //create a daemon thread
			perDayTimer.scheduleAtFixedRate(new TimerTask() {
				public void run()
				{
					notifyUsers(dayMailsToSend);
				}
			}, c.getTime(), 86400000);
		}
	}

	public List<Notification> getNotifications()
	{
		//List<Notification> notifications = template.query("select (*) from notification", new NotificationMapper());
		String query = "select notification.*, notificationcategories.* " +
				"from notification " +
				"left join notificationcategories on notificationcategories.message_id = notification.message_id " +
				"order by notification.updated_on desc";
		List<Notification> notifications = template.query(query, new NotificationCategoriesExtractor());
		return notifications;
	}
	
	public List<Notification> getNotificationsByCategory(String category)
	{
		String sql = "select notification.*, notificationcategories.* " +
				"from notification " +
				"left join notificationcategories on notificationcategories.message_id = notification.message_id " +
				"where exists (select c.* from notificationcategories c where c.category = ? " +
				"and notification.message_id = c.message_id) " +
				"order by notification.updated_on desc";
		List<Notification> notifications = template.query(sql, new Object[]{category}, new NotificationCategoriesExtractor());
		return notifications;
	}
	
	public int getTotalNotifications()
	{
		return template.queryForInt("select count(*) from notification");
	}
	
	public List<Notification> getNotificationsByApplication(String app)
	{
		List<Notification> filteredNotifications = template.query("select notification.*, notificationcategories.message_id,notificationcategories.category " +
				"from notification " +
				"left join notificationcategories on notificationcategories.message_id = notification.message_id " +
				"where notification.application = ? " +
				"order by notification.updated_on desc",
				new Object[]{app},
				new NotificationCategoriesExtractor());		
		return filteredNotifications;
	}	
	
	//User table Insertions and Queries	
	
	public User insertNewUser(String emailAddress, String password, String firstName, String lastName, 
			String categories[], String applications[], String[] catFrequencies,String[] appFrequencies) throws DuplicateKeyException
	{
		try
		{
			template.update("insert into user(EMAIL_ADDRESS,PASSWORD,FIRST_NAME,LAST_NAME) values (?,?,?,?)",
					emailAddress,password,firstName,lastName);
			for(int i = 0; i < applications.length; i++)
			{
				if(applications[i].trim().length()>0)
				template.update("insert into subscription(EMAIL_ADDRESS,TYPE,SUB,FREQUENCY) values (?,?,?,?)", 
						emailAddress, "Application", applications[i], appFrequencies[i]);
			}			
			for(int i = 0; i < categories.length; i++)
			{
				if(categories[i].trim().length()>0)
				template.update("insert into subscription(EMAIL_ADDRESS,TYPE,SUB,FREQUENCY) values (?,?,?,?)", 
						emailAddress, "Category", categories[i], catFrequencies[i]);
			}
			return getUser(emailAddress,password);
		}
		catch(DuplicateKeyException e)
		{
			return null;
		}

	}
	
	public void updateUserPersonalDetails(User user,String firstName,String lastName)
	{
		template.update("update user set first_name = ?, last_name = ? where email_address = ?",
				firstName,lastName,user.getEmailAddress());
		user.setFirstName(firstName);
		user.setLastName(lastName);
	}
	
	
	//checks if there is a user registered with the given email address and password, returns null if no such user exists
	public User getUser(String emailAddress, String password)
	{
		try 
		{
			List<User> user = template.query("select user.*, subscription.* " +
					"from user " +
					"left join subscription on subscription.email_address = user.email_address " +
					"where user.email_address = ? and user.password = ?",new Object[]{emailAddress,password}, 
					new UserSubscriptionExtractor());
			return user.get(0);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	//Users cannot add duplicate applications to their subscriptions
	public int addUserApp(User user, String app, String frequency)
	{
		try
		{
			if(app.trim().length()>0)
			{
				if(frequency == null) template.update("insert into subscription(EMAIL_ADDRESS,TYPE,SUB) values (?,?,?)",
						user.getEmailAddress(),"Application",app);
				else template.update("insert into subscription(EMAIL_ADDRESS,TYPE,SUB,FREQUENCY) values (?,?,?,?)",
						user.getEmailAddress(),"Application",app,frequency);
				user.addApplication(app, frequency);
				return 0;
			} return -1;
		}
		catch(Exception e)
		{
			return -1;
		}
	
	}	
	
	//Users cannot add duplicate categories to their subscriptions
	public int addUserCategory(User user, String category, String frequency)
	{
		try
		{
			if(category.trim().length()>0)
			{
				if(frequency == null) template.update("insert into subscription (EMAIL_ADDRESS,TYPE,SUB) values (?,?,?)",
						user.getEmailAddress(),"Category",category);
				else template.update("insert into subscription (EMAIL_ADDRESS,TYPE,SUB,FREQUENCY) values (?,?,?,?)",
						user.getEmailAddress(),"Category",category,frequency);
				user.addCategory(category, frequency);	
				return 0;
			} return -1;
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	
	//this method is only used when updating the user's categories from user_account.jsp
	public void deleteAllUserCategories(User user)
	{
		template.update("delete from subscription where email_address = ? and type = ?",
				user.getEmailAddress(),"Category");
		user.getCategoriesMap().clear();
	}
	
	public int deleteUserCategory(User user, String category)
	{
		try 
		{
			template.update("delete from subscription where email_address = ? and type = ? and sub = ?",
					user.getEmailAddress(),"Category",category);
			user.removeCategory(category);
			return 0;
		}
		catch(Exception e)
		{
			return -1;
		}
		
	}	
	
	public void deleteAllUserApplications(User user)
	{
		template.update("delete from subscription where email_address = ? and type = ?",
				user.getEmailAddress(),"Application");
		user.getCategoriesMap().clear();
	}
	
	
	public int deleteUserApp(User user, String application)
	{
		try
		{
			template.update("delete from subscription where email_address = ? and type = ? and sub = ?",
					user.getEmailAddress(),"Application",application);
			user.removeCategory(application);
			return 0;
		}
		catch(Exception e)
		{
			return -1;
		}
		
	}
	
	public boolean sendPasswordReminder(String emailAddress)
	{
		try
		{
			User user = getUserByEmail(emailAddress);
			mailManager.forgettenPassword(user);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
