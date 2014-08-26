package uk.ac.ucl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.jwebunit.html.Cell;
import net.sourceforge.jwebunit.util.TestingEngineRegistry;
import org.junit.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;

import org.junit.Before;
import org.junit.Test;

public class AllNotificationsTest{

	private Date date = new Date();
	
	@Before
	public void prepare(){
		setTestingEngineKey(TestingEngineRegistry.TESTING_ENGINE_HTMLUNIT);
		setBaseUrl("http://localhost:8080/NotiFLY/");
		beginAt("http://localhost:8080/NotiFLY/new/Test Application/Some notification message/Cat1,Cat2");
	}

	@Test
	public void testPageNotifiesUserAfterAddingNewNotification(){
		assertTextPresent("Notification uploaded successfully");
	}

	@Test
	public void testNewNotificationIsAddedToTable(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String dateFormatted = sdf.format(date);
		
		String[][] row = new String[1][4];
		row[0][0] = "Some notification message";
		row[0][1] = "Test Application";
		row[0][2] = dateFormatted;
		row[0][3] = "Cat2, Cat1";
		
		assertTableRowsEqual("notifications", 1, row);
	}
}
