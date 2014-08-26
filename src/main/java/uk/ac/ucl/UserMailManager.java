package uk.ac.ucl;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


public class UserMailManager 
{
	private MailSender mailSender;
	private SimpleMailMessage notificationMsg, forgottenPasswordMsg;
	
	@Autowired
	public UserMailManager(MailSender mailSender, SimpleMailMessage notificationMsg, SimpleMailMessage forgottenPasswordMsg)
	{
		this.mailSender = mailSender;
		this.notificationMsg = notificationMsg;
		this.forgottenPasswordMsg = forgottenPasswordMsg;
	}
	
	protected MailSender getMailSender(){
		return mailSender;
	}
	
	public void emailNotification(User user, UserMail userMail)
	{
		notificationMsg.setTo(user.getEmailAddress());
		notificationMsg.setSubject("New Notification Uploaded");
		String emailText = "Dear " + user.getFirstName() + " " + user.getLastName() + ",";
		String notifyForApplication = "The following Notification has been uploaded by the " + userMail.getApplication() + " Application.";
		if(userMail.getNotifyForApp()) emailText += "\n\n" + notifyForApplication;
		String notifyForCategories = "Notification added in the following categories: ";
		ArrayList<String> getCategoriesToNotify = userMail.getNotifyForCategories();
		if(getCategoriesToNotify != null)
		{
			for(int i=0; i<getCategoriesToNotify.size(); i++)
			{
				notifyForCategories += getCategoriesToNotify.get(i);
				if(i !=  getCategoriesToNotify.size()-1) notifyForCategories += ", ";
			}
			emailText += "\n\n" + notifyForCategories;
		}
		String notificationtxt = "\n\nMessage: " + userMail.getMessage() +
				"\nUploaded By: " + userMail.getApplication() +
				"\nCategories: " + userMail.getCategories() +
				"\nUploaded: " + userMail.getDateUpdated();
		notificationMsg.setText(
				emailText + notificationtxt +
				"\n\nYou are subscribed to \nApplication(s): " + user.getApplications() + "\nCategories(s): " + user.getCategories() +
				"\n\nBest Wishes,\n\nThe NotiFLY Team"
				);
		sendMail(notificationMsg);
		
	}
	
	public void forgettenPassword(User user)
	{
		forgottenPasswordMsg.setTo(user.getEmailAddress());
		String msg = "Dear " + user.getFirstName() + " " + user.getLastName() +", " +
				"\n\nYour password is: " + user.getPassword() + 
				"\n\nBest Wishes,\n\nThe NotiFLY Team";
		forgottenPasswordMsg.setText(msg);
		sendMail(forgottenPasswordMsg);
	}
	
	public void sendMail(SimpleMailMessage msg)
	{
		try
		{
			this.mailSender.send(msg);
		}
		catch(MailException ex)
		{
			System.err.println(ex.getMessage());
		}
	}
}
