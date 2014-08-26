package uk.ac.ucl;


import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class LoginTest{

	TestHelper helper = new TestHelper();
	
	@Before
	public void prepare(){
		setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		setBaseUrl("http://localhost:8080/NotiFLY/");
		beginAt("new_user");
		helper.createFakeUser();
	 }
	
	@Test
    public void testRegisterLink(){
		beginAt("login");
		clickLinkWithExactText("Register");
		assertTitleEquals("User Registration");	
	}
	
	@Test
    public void testForgottenPasswordLink(){
		beginAt("login");
		clickLinkWithExactText("Forgotten Password");
		assertTitleEquals("NotiFLY Password Reminder");
	}
	
	@Test
    public void testUserCanLoginWithCorrectDetails(){
    	beginAt("login");
    	setTextField("emailAddress", "valentinabodurova@yahoo.com");
        setTextField("password", "randompassword");
        submit();
        assertTextPresent("Welcome Val Bod");
    }
	
	@Test
    public void testUserCannotLoginWithIncorrectDetails(){
    	beginAt("login");
    	setTextField("emailAddress", "wrongEmail");
        setTextField("password", "wrongPassword");
        submit();
        assertTextPresent("Incorrect email address or password");
    }
}