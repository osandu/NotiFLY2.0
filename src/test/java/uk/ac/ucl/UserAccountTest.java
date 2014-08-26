package uk.ac.ucl;


import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class UserAccountTest{
	
	TestHelper helper = new TestHelper();
	
	@Before
	public void prepare(){
		setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		setBaseUrl("http://localhost:8080/NotiFLY/");
		beginAt("new_user");
		helper.createFakeUser();
		helper.createAnotherFakeUser();
	}
	
	@Test
    public void testAccountLink(){
		userLogin();
		clickLinkWithExactText("My Account");
		assertTitleEquals("User Account");
	}
	
	@Test
    public void testAppSubscriptionLink(){
		userLogin();
		clickLinkWithExactText("View Notifications by subscribed Applications");
		assertTextPresent("You are not subscribed to any Applications.");
	}
	
	@Test
    public void testCatSubscriptionLink(){
		userLogin();
		clickLinkWithExactText("View Notifications by subscribed Categories");
		assertTextPresent("You are not subscribed to any Categories.");
	}
	
	@Test
    public void testLogoutLink(){
		userLogin();
		clickLinkWithExactText("Logout");
		assertTitleEquals("User Login");
	}
	
	@Test
    public void testUserCanSubscribeToAppsAndCategories(){
		beginAt("login");
		userLogin2();
		assertTextPresent("Welcome Vali Bodi");
		clickLinkWithExactText("My Account");
		assertTextPresent("User Account Details");
		//add an application
		clickButtonWithText("Add Application");
		setTextField("application", "Integration Layer");
		selectOptionByValue("appfrequency", "1 hour");
		clickButton("saveApps");
		//add a category
		clickButtonWithText("Add Category");
		setTextField("category", "Accounting");
		selectOptionByValue("catfrequency", "Real-time");
		clickButton("saveCats");
		
		clickLinkWithExactText("User Home");
		clickLinkWithExactText("View Notifications by subscribed Applications");
		assertTextPresent("Applications subscribed to: [Integration Layer]");
		
		clickLinkWithExactText("User Home");
		clickLinkWithExactText("View Notifications by subscribed Categories");
		assertTextPresent("Categories subscribed to: [Accounting]");
	}
	
	private void userLogin(){
		beginAt("login");
		setTextField("emailAddress", "valentinabodurova@yahoo.com");
		setTextField("password", "randompassword");
		submit();
	}
	
	private void userLogin2(){
		beginAt("login");
		setTextField("emailAddress", "valbod@hotmail.co.uk");
		setTextField("password", "anotherpassword");
		submit();
	}
}
