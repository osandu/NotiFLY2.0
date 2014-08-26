package uk.ac.ucl;


import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.*;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class TestHelper{

	@Before
	public void prepare(){
		setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		setBaseUrl("http://localhost:8080/NotiFLY/");
		beginAt("new_user");
	}
	
	public void createFakeUser(){
		beginAt("new_user");
		setTextField("emailAddress", "valentinabodurova@yahoo.com");
		setTextField("password", "randompassword");
		setTextField("firstName", "Val");
		setTextField("lastName", "Bod");
		checkCheckbox("appchck");
		selectOptionByValue("appfrequency", "1 hour");
		checkCheckbox("categorychk");
		selectOptionByValue("catfrequency", "Real-time");
		submit();
	}
	
	public void createAnotherFakeUser(){
		beginAt("new_user");
		setTextField("emailAddress", "valbod@hotmail.co.uk");
		setTextField("password", "anotherpassword");
		setTextField("firstName", "Vali");
		setTextField("lastName", "Bodi");
		checkCheckbox("appchck");
		selectOptionByValue("appfrequency", "1 hour");
		checkCheckbox("categorychk");
		selectOptionByValue("catfrequency", "1 day");
		submit();
	}
}
