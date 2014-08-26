package uk.ac.ucl;


import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.*;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class UserRegistrationTest{

	TestHelper helper = new TestHelper();
	
	@Before
	public void prepare(){
		setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		setBaseUrl("http://localhost:8080/NotiFLY/");
		beginAt("new_user");
		helper.createFakeUser();
	}
	
	@Test
    public void testFrequenciesInfoLink(){
		beginAt("new_user");
		clickLinkWithExactText("Learn about frequencies");
		assertWindowPresent("popup");
	}
	
	@Test
    public void testUserRegistrationWorks(){
		//after user is registered they should be able to login with their account details
		beginAt("login");
		setTextField("emailAddress", "valentinabodurova@yahoo.com");
        setTextField("password", "randompassword");
        submit();
        assertTextPresent("Welcome Val Bod");
	}
}
