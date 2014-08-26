package uk.ac.ucl;


import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class ForgottenPasswordTest{

	TestHelper helper = new TestHelper();
	
	@Before
	public void prepare(){
		setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		setBaseUrl("http://localhost:8080/NotiFLY/");
		beginAt("new_user");
		helper.createFakeUser();
	}
	
	@Test
    public void testForgottenPasswordWrongEmailGivesErrorMessage(){
		beginAt("forgottenpassword");
		setTextField("emailAddress", "wrongEmail");
		submit();
		assertTextPresent("This email address has not been registered with NotiFLY.");
	}
	
	@Test
    public void testForgottenPasswordCorrectEmailGivesSuccessMessage(){
		beginAt("forgottenpassword");
		setTextField("emailAddress", "valentinabodurova@yahoo.com");
		submit();
		assertTextPresent("Your password has been sent to your email account.");
	}
}
