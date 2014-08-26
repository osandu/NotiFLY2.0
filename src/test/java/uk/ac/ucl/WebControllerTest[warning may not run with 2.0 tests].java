package uk.ac.ucl;

//import java.io.IOException;
import java.io.PrintWriter;
import org.jmock.lib.legacy.ClassImposteriser;

//import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.util.Calendar;
//import java.util.Date;
import java.util.GregorianCalendar;

@RunWith(JMock.class)
public class WebControllerTest{
	
    Mockery context = new JUnit4Mockery(){{
    	setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    WebController webcontroller;
    
      
    @Before
    public void setUp(){
    	webcontroller = new WebController();
        ApplicationContext appcontext = new ClassPathXmlApplicationContext( "echoapp-servlet.xml" );
    	webcontroller.notifly = ( NotiflyDatabase ) appcontext.getBean( "notifly" );
    	
    }
    
    @Test 
    public void testViewAllNotificationsShouldPrintNoNotificationsWhenThereAreNoNotifications() throws Exception {
        // set up
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final PrintWriter out = context.mock(java.io.PrintWriter.class);
        
        
        // expectations
        context.checking(new Expectations() {{
        	oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).println("<br>There are currently no notifications</br>");
            oneOf(out).print("</body></html>");
            
        }});

        //execute
        webcontroller.viewAllNotifications(response);
                     
        
    }
    
    @Test 
    public void testViewAllNotifications() throws Exception {
    	// set up
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final PrintWriter out = context.mock(java.io.PrintWriter.class);
        Calendar longDate = new GregorianCalendar();
        final int year = longDate.get(Calendar.YEAR);
        final String years;
        if(year<10){years = "0"+String.valueOf(year);}
        else{years = String.valueOf(year);}
        final int month = longDate.get(Calendar.MONTH)+1;
        final String months;
        if(month<10){months = "0"+String.valueOf(month);}
        else{months = String.valueOf(month);}
        final int day = longDate.get(Calendar.DAY_OF_MONTH);
        final String days;
        if(day<10){days = "0"+String.valueOf(day);}
        else{days = String.valueOf(day);}
        final int hour = longDate.get(Calendar.HOUR_OF_DAY);
        final String hours;
        if(hour<10){hours = "0"+String.valueOf(hour);}
        else{hours = String.valueOf(hour);}
        final int min = longDate.get(Calendar.MINUTE);
        final String mins;
        if(min<10){mins = "0"+String.valueOf(min);}
        else{mins = String.valueOf(min);}  
       
     // expectations
        context.checking(new Expectations() {{
        	oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).print("<br> Notification uploaded successfully </br>");
            oneOf(out).print("<br>Total No. of Notifications: 1</br>");
            oneOf(out).print("<table border = 3>" +
				"<tr> <th> MESSAGE </th>" +
				"<th> APPLICATION </th>" +
				"<th> UPDATED_ON </th>" +
				"<th> CATEGORIES </th> </tr>");
            oneOf(out).println("<td> message</td>");
			oneOf(out).println("<td> app</td>");
			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
			oneOf(out).println("<td> category</td></tr>");
			oneOf(out).print("</table>");
			oneOf(out).print("</body></html>");
			oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).println("<br>Total No. of Notifications: 1</br>");
            oneOf(out).print("<table border = 3>" +
				"<tr> <th> MESSAGE </th>" +
				"<th> APPLICATION </th>" +
				"<th> UPDATED_ON </th>" +
				"<th> CATEGORIES </th> </tr>");
            oneOf(out).println("<td> message</td>");
			oneOf(out).println("<td> app</td>");
			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
			oneOf(out).println("<td> category</td></tr>");
			oneOf(out).print("</table>");
			oneOf(out).print("</body></html>");
            
        }});

        
        //execute
        
        webcontroller.newNotificiation(response, "app", "message", "category");
        webcontroller.viewAllNotifications(response);
               
    }
    
    @SuppressWarnings("deprecation")
	@Test 
    public void testNewNotification() throws Exception {
        // set up
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final PrintWriter out = context.mock(java.io.PrintWriter.class);
        Calendar longDate = new GregorianCalendar();
        final int year = longDate.get(Calendar.YEAR);
        final String years;
        if(year<10){years = "0"+String.valueOf(year);}
        else{years = String.valueOf(year);}
        final int month = longDate.get(Calendar.MONTH)+1;
        final String months;
        if(month<10){months = "0"+String.valueOf(month);}
        else{months = String.valueOf(month);}
        final int day = longDate.get(Calendar.DAY_OF_MONTH);
        final String days;
        if(day<10){days = "0"+String.valueOf(day);}
        else{days = String.valueOf(day);}
        final int hour = longDate.get(Calendar.HOUR_OF_DAY);
        final String hours;
        if(hour<10){hours = "0"+String.valueOf(hour);}
        else{hours = String.valueOf(hour);}
        final int min = longDate.get(Calendar.MINUTE);
        final String mins;
        if(min<10){mins = "0"+String.valueOf(min);}
        else{mins = String.valueOf(min);}        
        // expectations
        context.checking(new Expectations() {{
        	oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).print("<br> Notification uploaded successfully </br>");
            oneOf(out).print("<br>Total No. of Notifications: 1</br>");
            oneOf(out).print("<table border = 3>" +
				"<tr> <th> MESSAGE </th>" +
				"<th> APPLICATION </th>" +
				"<th> UPDATED_ON </th>" +
				"<th> CATEGORIES </th> </tr>");
            oneOf(out).println("<td> message</td>");
			oneOf(out).println("<td> app</td>");
			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
			oneOf(out).println("<td> category</td></tr>");
			oneOf(out).print("</table>");
			oneOf(out).print("</body></html>");
            
        }});

        //execute
        webcontroller.newNotificiation(response, "app","message","category");
        //System.out.print("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");      
    }
    
    @Test
    public void testWhenThereAreNoNotificationsForAnAppViewNotificationByAppReturnsNoNotifications()throws Exception {
        // set up
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final PrintWriter out = context.mock(java.io.PrintWriter.class);
        Calendar longDate = new GregorianCalendar();
        final int year = longDate.get(Calendar.YEAR);
        final String years;
        if(year<10){years = "0"+String.valueOf(year);}
        else{years = String.valueOf(year);}
        final int month = longDate.get(Calendar.MONTH)+1;
        final String months;
        if(month<10){months = "0"+String.valueOf(month);}
        else{months = String.valueOf(month);}
        final int day = longDate.get(Calendar.DAY_OF_MONTH);
        final String days;
        if(day<10){days = "0"+String.valueOf(day);}
        else{days = String.valueOf(day);}
        final int hour = longDate.get(Calendar.HOUR_OF_DAY);
        final String hours;
        if(hour<10){hours = "0"+String.valueOf(hour);}
        else{hours = String.valueOf(hour);}
        final int min = longDate.get(Calendar.MINUTE);
        final String mins;
        if(min<10){mins = "0"+String.valueOf(min);}
        else{mins = String.valueOf(min);}  
       
        
        // expectations
        context.checking(new Expectations() {{
        	oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).print("<br> Notification uploaded successfully </br>");
            oneOf(out).print("<br>Total No. of Notifications: 1</br>");
            oneOf(out).print("<table border = 3>" +
				"<tr> <th> MESSAGE </th>" +
				"<th> APPLICATION </th>" +
				"<th> UPDATED_ON </th>" +
				"<th> CATEGORIES </th> </tr>");
            oneOf(out).println("<td> message</td>");
			oneOf(out).println("<td> app</td>");
			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
			oneOf(out).println("<td> category</td></tr>");
			oneOf(out).print("</table>");
			oneOf(out).print("</body></html>");oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).print("<br> There are no notifications uploaded by anotherapp</br>");
        	oneOf(out).print("</body></html>");
            
        }});

        //execute
        
        webcontroller.newNotificiation(response, "app", "message", "category");
        webcontroller.viewNotificationByApp(response, "anotherapp");
               
    }
    
    @Test
    public void testViewNotificationsByApp()throws Exception {
        // set up
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final PrintWriter out = context.mock(java.io.PrintWriter.class);
        Calendar longDate = new GregorianCalendar();
        final int year = longDate.get(Calendar.YEAR);
        final String years;
        if(year<10){years = "0"+String.valueOf(year);}
        else{years = String.valueOf(year);}
        final int month = longDate.get(Calendar.MONTH)+1;
        final String months;
        if(month<10){months = "0"+String.valueOf(month);}
        else{months = String.valueOf(month);}
        final int day = longDate.get(Calendar.DAY_OF_MONTH);
        final String days;
        if(day<10){days = "0"+String.valueOf(day);}
        else{days = String.valueOf(day);}
        final int hour = longDate.get(Calendar.HOUR_OF_DAY);
        final String hours;
        if(hour<10){hours = "0"+String.valueOf(hour);}
        else{hours = String.valueOf(hour);}
        final int min = longDate.get(Calendar.MINUTE);
        final String mins;
        if(min<10){mins = "0"+String.valueOf(min);}
        else{mins = String.valueOf(min);}  
       
        
        // expectations
        context.checking(new Expectations() {{
        	oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).print("<br> Notification uploaded successfully </br>");
            oneOf(out).print("<br>Total No. of Notifications: 1</br>");
            oneOf(out).print("<table border = 3>" +
				"<tr> <th> MESSAGE </th>" +
				"<th> APPLICATION </th>" +
				"<th> UPDATED_ON </th>" +
				"<th> CATEGORIES </th> </tr>");
            oneOf(out).println("<td> message</td>");
			oneOf(out).println("<td> app</td>");
			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
			oneOf(out).println("<td> category</td></tr>");
			oneOf(out).print("</table>");
			oneOf(out).print("</body></html>");oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).print("<br>1 notifications uploaded by the app</br>");
            oneOf(out).print("<table border = 3>" +
				"<tr> <th> MESSAGE </th>" +
				"<th> APPLICATION </th>" +
				"<th> UPDATED_ON </th>" +
				"<th> CATEGORIES </th> </tr>");
            oneOf(out).println("<td> message</td>");
			oneOf(out).println("<td> app</td>");
			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
			oneOf(out).println("<td> category</td></tr>");
			oneOf(out).print("</table>");
			oneOf(out).print("</body></html>");
            
        }});

        //execute
        
        webcontroller.newNotificiation(response, "app", "message", "category");
        webcontroller.viewNotificationByApp(response, "app");
               
    }
    
    @Test 
    public void testRegisterNewUser() throws Exception {
        // set up
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        final PrintWriter out = context.mock(java.io.PrintWriter.class);
        final HttpSession session = context.mock(HttpSession.class);
        
        
        // expectations
        context.checking(new Expectations() {{
        	oneOf(request).getSession();will(returnValue(session));
        	oneOf(session).setAttribute("emailAddress", "emailAddress");
        	oneOf(session).setAttribute("password","password");
        	oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(request).getContextPath();
            oneOf(request).getContextPath();
            oneOf(request).getContextPath();
            oneOf(out).println("<h2> Welcome firstName lastName</h2><br> <strong> Frequency of Notification Emails:  </strong>Real-time<br><a href = /user/applications > View Notifications of Subscribed Applications </a><br><a href = /user/categories > View Notifications of Subscribed Categories </a><br><a href = /logout >Logout</a>");
            oneOf(out).print("</body></html>");
           
        }});

        //execute
        webcontroller.registerNewUser(response, request, "emailAddress", "password", "firstName", "lastName", "category", "app","Real-time");                            
        
    }
    
    @Test
    public void testRegisterUserWithExistingEmailAddressAndPasswordShouldNotBeAllowed() throws Exception {
    	 // set up
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        final PrintWriter out = context.mock(java.io.PrintWriter.class);
        final HttpSession session = context.mock(HttpSession.class);
        
        
        // expectations
        context.checking(new Expectations() {{
        	oneOf(request).getSession();will(returnValue(session));
        	oneOf(session).setAttribute("emailAddress", "emailAddress");
        	oneOf(session).setAttribute("password","password");
        	oneOf(response).setContentType("text/html");
            oneOf (response).getWriter();will(returnValue(out));
            oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
            oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
            oneOf(out).print("<br>This email address already exists</br>");
            oneOf(out).print("</body></html>");
            
        }});

        //execute
        webcontroller.notifly.insertNewUser("emailAddress", "password", "someFirstName", "someLastName", "someCategory", "someApp");
        webcontroller.registerNewUser(response, request, "emailAddress", "password", "firstName", "lastName", "category", "app","Real-time");   
    }
    
    //note: to get these two tests to work changed some code in webcontroller
    //Object emailAddressObj = session.getAttribute("emailAddress");
	//String emailAddress = null;
	//if(emailAddressObj!= null){emailAddress = emailAddressObj.toString();}
	//same thing for password
    @Test
    public void testGetUsersApplicationShouldNotWorkIfUserNotLoggedIn() throws Exception {
   	 // set up
       final HttpServletResponse response = context.mock(HttpServletResponse.class);
       final HttpServletRequest request = context.mock(HttpServletRequest.class);
       final PrintWriter out = context.mock(java.io.PrintWriter.class);
       final HttpSession session = context.mock(HttpSession.class);
       
       
       // expectations
       context.checking(new Expectations() {{
    	   oneOf(request).getSession();will(returnValue(session));
       	oneOf(session).getAttribute("emailAddress");
        oneOf(session).getAttribute("password");
       	   oneOf(response).setContentType("text/html");
           oneOf (response).getWriter();will(returnValue(out));
           oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
           oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
           oneOf(session).getAttribute("emailAddress");will(returnValue(null));
           oneOf(out).println("You are not logged in. Please log in!");
           oneOf(out).print("</body></html>");
           
       }});

       //execute
       
       webcontroller.getUsersApplication(response, request);
                        
    }
    
   @Test
    public void testGetUserCategoriesShouldNotWorkIfUserNotLoggedIn() throws Exception {
	   // set up
       final HttpServletResponse response = context.mock(HttpServletResponse.class);
       final HttpServletRequest request = context.mock(HttpServletRequest.class);
       final PrintWriter out = context.mock(java.io.PrintWriter.class);
       final HttpSession session = context.mock(HttpSession.class);
       
       
       // expectations
       context.checking(new Expectations() {{
    	   oneOf(request).getSession();will(returnValue(session));
       	oneOf(session).getAttribute("emailAddress");
        oneOf(session).getAttribute("password");
       	   oneOf(response).setContentType("text/html");
           oneOf (response).getWriter();will(returnValue(out));
           oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
           oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
           oneOf(session).getAttribute("emailAddress");will(returnValue(null));
           oneOf(out).println("You are not logged in. Please log in.");
           oneOf(out).print("</body></html>");
           
       }});

       //execute
       
       webcontroller.getUserCategories(response, request);
                        
    }
    
    @Test
    public void testGetUserCategories() throws Exception {
      	 // set up
          final HttpServletResponse response = context.mock(HttpServletResponse.class);
          final HttpServletRequest request = context.mock(HttpServletRequest.class);
          final PrintWriter out = context.mock(java.io.PrintWriter.class);
          final HttpSession session = context.mock(HttpSession.class);
          Calendar longDate = new GregorianCalendar();
          final int year = longDate.get(Calendar.YEAR);
          final String years;
          if(year<10){years = "0"+String.valueOf(year);}
          else{years = String.valueOf(year);}
          final int month = longDate.get(Calendar.MONTH)+1;
          final String months;
          if(month<10){months = "0"+String.valueOf(month);}
          else{months = String.valueOf(month);}
          final int day = longDate.get(Calendar.DAY_OF_MONTH);
          final String days;
          if(day<10){days = "0"+String.valueOf(day);}
          else{days = String.valueOf(day);}
          final int hour = longDate.get(Calendar.HOUR_OF_DAY);
          final String hours;
          if(hour<10){hours = "0"+String.valueOf(hour);}
          else{hours = String.valueOf(hour);}
          final int min = longDate.get(Calendar.MINUTE);
          final String mins;
          if(min<10){mins = "0"+String.valueOf(min);}
          else{mins = String.valueOf(min);} 
          final User user = webcontroller.notifly.getUser("myemailAddress","mypassword");
          
          //NEED TO INSERT NOTIFICATION AND CHECK DISPLAY IT
          // expectations
          context.checking(new Expectations() {{
        	               
                oneOf(request).getSession();will(returnValue(session));
             	oneOf(session).getAttribute("emailAddress");will(returnValue("myemailAddress"));
              oneOf(session).getAttribute("password");will(returnValue("mypassword"));
             	   oneOf(response).setContentType("text/html");
                 oneOf (response).getWriter();will(returnValue(out));
                 oneOf(out).print("<html><head> <title> NotiFLY </title> </head> " + "<body>");
                 oneOf(out).print("<br><h1><strong><center>NotiFLY Notifications</strong></h1><br><hr/>");
                 oneOf(session).getAttribute("emailAddress");will(returnValue("myemailAddress"));
                 oneOf(session).getAttribute("password");will(returnValue("mypassword"));
                 oneOf(request).getContextPath();
                 oneOf(out).println("<a href = /user>User home</a>");
                 oneOf(out).println("<h2> Welcome " + "firstName" + " " + "lastName" + "</h2>");
  			 	oneOf(out).println("<br>Categories subscribed to: " + "category,anothercategory" + "</br>");
  			oneOf(out).println("<h3>Notifications in category</h3></br>");
  		  oneOf(out).print("<table border = 3>" +
  				"<tr> <th> MESSAGE </th>" +
  				"<th> APPLICATION </th>" +
  				"<th> UPDATED_ON </th>" +
  				"<th> CATEGORIES </th> </tr>");
              oneOf(out).println("<td> message</td>");
  			oneOf(out).println("<td> app</td>");
  			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
  			oneOf(out).println("<td> category</td></tr>");
  			oneOf(out).print("</table>");
  			oneOf(out).println("<h3>Notifications in anothercategory</h3></br>");
  			oneOf(out).print("<table border = 3>" +
  				"<tr> <th> MESSAGE </th>" +
  				"<th> APPLICATION </th>" +
  				"<th> UPDATED_ON </th>" +
  				"<th> CATEGORIES </th> </tr>");
              oneOf(out).println("<td> anothermessage</td>");
  			oneOf(out).println("<td> anotherapp</td>");
  			oneOf(out).println("<td> "+days+"-"+months+"-"+years+" "+hours+":"+mins+"</td>");
  			oneOf(out).println("<td> anothercategory</td></tr>");
  			oneOf(out).print("</table>");
  			oneOf(out).print("</body></html>");
              
          }});

          //execute
          webcontroller.notifly.insertNotification("app", "message", "category");
          webcontroller.notifly.insertNotification("anotherapp", "anothermessage", "anothercategory");
          webcontroller.notifly.insertNewUser("myemailAddress", "mypassword", "firstName", "lastName", "category,anothercategory", "app");
          webcontroller.getUserCategories(response, request);          
       }
    
    @After
    public void tearDown(){//NOTE CHANGED TEMPLATE IN NOTIFLYDATABASE.JAVA TO PROTECTED
    	webcontroller.notifly.template.execute("DELETE FROM notification");
    	webcontroller.notifly.template.execute("DELETE FROM user");
    }
   
    
}
