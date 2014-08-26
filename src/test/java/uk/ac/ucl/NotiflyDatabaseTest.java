package uk.ac.ucl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

public class NotiflyDatabaseTest extends TestCase{

	private NotiflyDatabase notifly;
	private List<Notification> notifications;
    
    @Before
    public void setUp(){
    	ApplicationContext context = new ClassPathXmlApplicationContext( "echoapp-servlet.xml" );
    	notifly = ( NotiflyDatabase ) context.getBean( "notifly" );
    	notifications = notifly.getNotifications();
    }
	
	@Test
	public void testShouldBeAbleToInsertANotificationInDatabase(){
		notifly.insertNotification("message","application","category");
		System.out.println(notifly.getTotalNotifications());
		assertThat(notifly.getTotalNotifications(), is(1));
		assert(notifications.get(0).getMessage().equals("message"));
		assert(notifications.get(0).getApplication().equals("application"));
		assert(notifications.get(0).getCategories().equals("category"));
	}
	
	@Test
	public void testGetNotificationsByCategory(){
		notifly.insertNotification("amessage", "application", "category");
		notifly.insertNotification("anothermessage","anotherapplication","anothercategory");
		notifly.insertNotification("yetanothermessage","athirdapplication","category,anothercategory");
		List<Notification> notifications = notifly.getNotificationsByCategory("category");
		assertThat(notifications.size(), is(2));
    }
	
	@Test
    public void testGetNotificationsByApplication(){
		notifly.insertNotification("amessage", "application", "category");
	   	notifly.insertNotification("anothermessage","anotherapplication","anothercategory");
   		notifly.insertNotification("yetanothermessage","application","category,anothercategory");
   		List<Notification> notifications = notifly.getNotificationsByApplication("application");
   		assertThat(notifications.size(), is(2));
    }
	
	@Test
	public void testInsertingANewUser(){
		notifly.insertNewUser("email", "password", "firstName", "lastName", new String[]{"cat1","cat2"}, new String[]{"app1","app2"}, new String[]{"1 day","1 hour"}, new String[]{"Real-time","1 hour"});
		assertThat(notifly.getUser("email", "password"),notNullValue());
	}
	   
	@Test
	public void testAfterInsertingANewUserCorrectAppFrequencyIsGiven(){
		notifly.insertNewUser("email", "password", "firstName", "lastName", new String[]{"cat1","cat2"}, new String[]{"app1","app2"}, new String[]{"1 day","1 hour"}, new String[]{"Real-time","1 hour"});
		assertThat(notifly.getUser("email", "password").getApplicationFrequency("app1"), is("Real-time"));
		assertThat(notifly.getUser("email", "password").getApplicationFrequency("app2"), is("1 hour"));
	}
	
	@Test
	public void testAfterInsertingANewUserCorrectCatFrequencyIsGiven(){
		notifly.insertNewUser("email", "password", "firstName", "lastName", new String[]{"cat1","cat2"}, new String[]{"app1","app2"}, new String[]{"1 day","1 hour"}, new String[]{"Real-time","1 hour"});
		assertThat(notifly.getUser("email", "password").getCategoryFrequency("cat1"), is("1 day"));
		assertThat(notifly.getUser("email", "password").getCategoryFrequency("cat2"), is("1 hour"));
	}
	   
	@Test
	public void testInsertingANewUserWithDuplicateEmailAddressShouldFail(){
	    notifly.insertNewUser("email", "password", "firstName", "lastName", new String[]{"cat1"}, new String[]{"app1"}, new String[]{"1 day"}, new String[]{"Real-time"});
	    assertThat(notifly.insertNewUser("email", "password", "first", "last", new String[]{"cat2"}, new String[]{"app2"}, new String[]{"1 hour"}, new String[]{"1 hour"}),nullValue());
	}
	   
	@Test
	public void testInsertingANewUserWithoutDuplicateEmailAddressShouldSucceed(){
	    notifly.insertNewUser("email", "password", "firstName", "lastName", new String[]{"cat1"}, new String[]{"app1"}, new String[]{"1 day"}, new String[]{"Real-time"});
	    assertThat(notifly.insertNewUser("differentEmail", "password", "firstName", "lastName", new String[]{"cat1"}, new String[]{"app1"}, new String[]{"1 day"}, new String[]{"Real-time"}),notNullValue());
	}
	   
	@Test
	public void testGetUserCategorizedNotifications(){
	    notifly.insertNotification("amessage", "application", "category");
	    notifly.insertNotification("anothermessage","anotherapplication","anothercategory");
	    notifly.insertNotification("yetanothermessage","application","category,anothercategory");
	    notifly.insertNewUser("emailaddress","password","firstname","lastname", new String[]{"category"}, new String[]{"app"},new String[]{"1 hour"}, new String[]{"Real-time"});
	
	    User user = notifly.getUserByEmailWithSubscriptions("emailaddress");
	    List<String> userCats = user.getCategories();
	    List<Notification> notes = new ArrayList<Notification>();
	    for(String c : userCats){
		    notes.addAll(notifly.getNotificationsByCategory(c));
	    }
	    assertThat(notes.size(), is(2)); 
	}
	
	@Test
	public void testGetUserNotificationsByApplication(){
	    notifly.insertNotification("amessage", "application", "category");
	    notifly.insertNotification("anothermessage","anotherapplication","anothercategory");
	    notifly.insertNotification("yetanothermessage","application","category,anothercategory");
	    notifly.insertNewUser("emailaddress","password","firstname","lastname", new String[]{"category"}, new String[]{"application"},new String[]{"1 hour"}, new String[]{"Real-time"});
	
	    User user = notifly.getUserByEmailWithSubscriptions("emailaddress");
	    List<String> userApps = user.getApplications();
	    List<Notification> notes = new ArrayList<Notification>();
	    for(String a : userApps){
		    notes.addAll(notifly.getNotificationsByApplication(a));
	    }
	    assertThat(notes.size(), is(2)); 
	}
	   
	@Test
	public void testCannotGetNonexistentUser(){
		notifly.insertNewUser("email","password","firstname","lastname", new String[]{"category"}, new String[]{"apps"},new String[]{"1 hour"}, new String[]{"Real-time"});
		assertThat(notifly.getUser("emailaddress", "password"),nullValue());
	}
	   
	@Test
	public void testAddAndDeleteUserCategory(){
		notifly.insertNewUser("email","password","firstname","lastname", new String[]{"category"}, new String[]{"apps"},new String[]{"1 hour"}, new String[]{"Real-time"});
		User user = notifly.getUser("email", "password");
		notifly.addUserCategory(user, "anothercategory", "1 hour");
		assert(user.getCategories().contains("anothercategory"));
		notifly.deleteUserCategory(user, "anothercategory");
		assert(!(user.getCategories().contains("anothercategory")));   
	}
	   
	@Test
	public void testSetUserFrequency(){
		notifly.insertNewUser("email","password","firstname","lastname", new String[]{"cat1"}, new String[]{"app1"},new String[]{"1 hour"}, new String[]{"Real-time"});
		User user = notifly.getUser("email", "password");
		assert(user.getCategoryFrequency("cat1").equals("1 hour"));
		assert(user.getApplicationFrequency("app1").equals("Real-time"));
	}
	
	@After//note this will run after each test so each test is run on a new database
	public void tearDown(){//NOTE CHANGED TEMPLATE IN NOTIFLYDATABASE.JAVA TO PROTECTED
		//notifly.template.execute("DELETE FROM notification WHERE message = 'Test'");
		notifly.template.execute("DELETE FROM notificationcategories");
		notifly.template.execute("DELETE FROM notification");
		notifly.template.execute("DELETE FROM subscription");
    	notifly.template.execute("DELETE FROM user");
    }
}