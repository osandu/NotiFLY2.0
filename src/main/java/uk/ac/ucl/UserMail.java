package uk.ac.ucl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
//This class models an email that would be sent to user when a single new notification is added to NotiFLY
public class UserMail {
	
	private String message,application,dateUpdated;
	private String[] categories;
	private boolean notifyForApp;
	private ArrayList<String> notifyForCategories;
	
	public UserMail(String message, String application, String[] categories, String dateUpdated)
	{
		this.message = message;
		this.application = application;
		this.categories = categories;
		this.dateUpdated = dateUpdated;
		this.notifyForApp = false; //set to true if the email is being sent because the notification is uploaded by an application
		//that the user has subscribed to
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getApplication()
	{
		return application;
	}
	
	public Set<String> getCategories()
	{
		Set<String> mySet = new HashSet<String>(Arrays.asList(categories));
		return mySet;
	}
	
	public String getDateUpdated()
	{
		return dateUpdated;
	}
	
	public ArrayList<String> getNotifyForCategories()
	{
		return notifyForCategories;
	}
	
	public boolean getNotifyForApp()
	{
		return notifyForApp;
	}
	
	
	public void addToNotifyForCategories(String category)
	{
		if(notifyForCategories == null) notifyForCategories = new ArrayList<String>();
		this.notifyForCategories.add(category);
	}
	
	public void userSubscribedToApplication()
	{
		this.notifyForApp = true;
	}
}
