package uk.ac.ucl;

import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;


public class UserTest extends TestCase{
    private User user;
    private NotiflyDatabase notifly;
    
    @Before
    public void setUp(){
    	ApplicationContext context = new ClassPathXmlApplicationContext( "echoapp-servlet.xml" );
   		notifly = ( NotiflyDatabase ) context.getBean( "notifly" );
   		user = new User("email@address.com","password","firstName","lastName");
   		user.addApplication("app1", "Real-time");
   		user.addApplication("app2", "1 day");
   		user.addCategory("cat1", "1 hour");
   		user.addCategory("cat2", "1 day");
   		user.addCategory("cat3", "Real-time");
    }
   
    @Test
    public void testUserDetailsAreCorrect(){
    	assertThat(user.getEmailAddress(), is("email@address.com"));
    	assertThat(user.getFirstName(), is("firstName"));
    	assertThat(user.getLastName(), is("lastName"));
    	assertThat(user.getPassword(), is("password"));
    }
    
    @Test
    public void testWhenYouGetCategoriesItContainsAllCategoryUserSubscribedTo(){
    	List<String> cats = user.getCategories();
    	assertThat(cats.size(), is(3));
    	assert(cats.contains("category1"));
    	assert(cats.contains("category2"));
    	assert(cats.contains("category3"));
    }
    
    @Test
    public void testWhenYouGetApplicationItContainsAllApplicationsUserSubscribedTo(){
    	List<String> apps = user.getApplications(); 
    	assertThat(apps.size(), is(2));
    	assert(apps.contains("app1"));
    	assert(apps.contains("app2"));
    }
    
    @After//note this will run after each test so each test is run on a new database
    public void tearDown(){//NOTE CHANGED TEMPLATE IN NOTIFLYDATABASE.JAVA TO PROTECTED
    	notifly.template.execute("DELETE FROM user");
    }
}
