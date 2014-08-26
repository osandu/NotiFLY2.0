package uk.ac.ucl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class UserMailManagerTest extends TestCase{
    
	private NotiflyDatabase notifly;
    private Wiser wiser = new Wiser();
    private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
  
    @Before
    public void setUp(){
    	ApplicationContext context = new ClassPathXmlApplicationContext( "echoapp-servlet.xml" );
   		notifly = ( NotiflyDatabase ) context.getBean( "notifly" );
   		notifly.insertNewUser("email@address.com","password","firstName","lastName",new String[]{"category1","category2","category3"},new String[]{"app1","app2"},new String[]{"Real-time","1 day","1 hour"},new String[]{"Real-time","Real-time"});
   		((JavaMailSenderImpl)notifly.mailManager.getMailSender()).setHost("localhost");
   		((JavaMailSenderImpl)notifly.mailManager.getMailSender()).setPort(587);
   		mailSender.setPort(587);
 	    wiser.setPort(587); // Default is 25
    }
    //maybe test other frequency cases
    
    @Test
   	public void testUserShouldReceiveEmailWhenANotificationIsUploadedBySubscribedApp() throws MessagingException, IOException{
    	wiser.start();
    	notifly.insertNotification("message", "app1", "categories");
    	wiser.stop();
    	assertEquals(1,wiser.getMessages().size());
    	MimeMessage message = wiser.getMessages().get(0).getMimeMessage();
    	assert(message.getContent().toString().contains("firstName")); 
    	assert(message.getContent().toString().contains("The following Notification has been uploaded by the app1 Application"));
    	assert(message.getContent().toString().contains("Notification added in the following categories: categories"));
    	assert(message.getContent().toString().contains("Message: message"));  
   	}
   
   	@Test
   	public void testUserShouldReceiveEmailWhenANotificationIsUploadedInSubscribedCategory(){
	   	wiser.start();
	   	notifly.insertNotification("message", "adifferentapp", "category1");
	   	wiser.stop();
	   	assertEquals(1,wiser.getMessages().size());
   	}
   
   	@Test
   	public void testUserShouldNotReceiveEmailWhenANotificationIsUploadedToUnsubscribed(){
   		wiser.start();
   		notifly.insertNotification("message", "adifferentapp", "adifferentcategory");
   		wiser.stop();
   		assertEquals(0,wiser.getMessages().size());
   	}
   
   	@Test
   	public void testUserShouldNotReceiveInstantNotificationIfFrequencyIsNotRealTime(){
   		notifly.insertNewUser("email2@address.com","password2","firstName2","lastName2",new String[]{"category1","category2"},new String[]{"app1"},new String[]{"1 hour","1 day"},new String[]{"1 hour"});
   		wiser.start();
   		notifly.insertNotification("message", "app1", "categories");
   		wiser.stop();
   		assertEquals(1,wiser.getMessages().size());
   	}
   
   	@After//note this will run after each test so each test is run on a new database
   	public void tearDown(){//NOTE CHANGED TEMPLATE IN NOTIFLYDATABASE.JAVA TO PROTECTED
   		notifly.template.execute("DELETE FROM notificationcategories");
   		notifly.template.execute("DELETE FROM notification");
		notifly.template.execute("DELETE FROM subscription");
		notifly.template.execute("DELETE FROM user");  
   	}
}
