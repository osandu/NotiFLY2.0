/**
 * Copyright 2012 UCL 

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
 */
package uk.ac.ucl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class WebController {
	
	@Autowired
	private Autocomplete db;
	@Autowired
	private NotiflyDatabase notifly;
	@Autowired
	private NotiflyTwitter notiflyTwitter;
	private PrintWriter out;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
		
	
	@RequestMapping(value = "/", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public void viewAllNotifications( HttpServletResponse response, HttpServletRequest request ) throws Exception
	{
		initialise(response,request);
		int total = notifly.getTotalNotifications();
		if(total == 0)	out.println("<br>There are currently no notifications</br>");
		else 
		{
			out.println("<br>Total No. of Notifications: " + notifly.getTotalNotifications() + "</br>");
			List<Notification> notifications = notifly.getNotifications();
			display(notifications);	
		}
		out.print("</body></html>");
	}
	
	
	@RequestMapping(value = "/map", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public String viewAllNotifications( HttpServletResponse response, HttpServletRequest request, ModelMap model ) throws Exception
	{
		initialise(response,request);
		int total = notifly.getTotalNotifications();
		model.addAttribute("total",total);
		String message = null;
		if(total == 0)	message = "There are currently no notifications";
		else 
		{
			message = "Total No. of Notifications: " + notifly.getTotalNotifications();
			List<Notification> notifications = notifly.getNotifications();
			model.addAttribute("notifications",notifications);
		}
		model.addAttribute("message",message);
		return "jsp/all_notifications.jsp";
	}
	
	
	@RequestMapping(value = "/images/{name}", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public String logo( HttpServletResponse response, @PathVariable String name) throws Exception
	{
		return "images/" + name + ".png";
	}
	
	@RequestMapping(value = "/styles/{name}", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public String autocomplete_css( HttpServletResponse response, @PathVariable String name) throws Exception
	{
		return "styles/" + name + ".css";
	}
	
	@RequestMapping(value = "/js/{name}", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public String autocomplete_js( HttpServletResponse response, @PathVariable String name) throws Exception
	{
		return "js/" + name + ".js";
	}
	
	@RequestMapping( value="/getcategories", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public void getCategories( HttpServletResponse response, HttpServletRequest request ) throws Exception
	{
		initialiseVariables(response,request);
		out = this.response.getWriter();
	    String query = request.getParameter("q");
	     
	    List<String> categories = db.getCategories(query);
	    Iterator<String> iterator = categories.iterator();
	    while(iterator.hasNext()) {
	        String cat = (String)iterator.next();
	        out.println(cat);
	    }
	}
	
	@RequestMapping( value="/getapplications", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public void getApplications( HttpServletResponse response, HttpServletRequest request ) throws Exception
	{
		initialiseVariables(response,request);
		out = this.response.getWriter();
	    String query = request.getParameter("q");
	     
	    List<String> applications = db.getApplications(query);
	    Iterator<String> iterator = applications.iterator();
	    while(iterator.hasNext()) {
	        String app = (String)iterator.next();
	        out.println(app);
	    }
	}
	
	@RequestMapping( value = "/new/{app}/{message}/{categories}", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public void newNotificiation(HttpServletResponse response, HttpServletRequest request, @PathVariable String app, @PathVariable String message, @PathVariable String categories) throws Exception
	{
		notifly.runTimers();
		initialise(response,request);
		boolean uploaded = notifly.insertNotification(message,app,categories);
		if(uploaded)
		{
			out.print("<p> Notification uploaded successfully </p>");
		}
		else out.print("<p class='error'> Notification could not be uploaded. Please make sure the caetgories entered are unique.</p>");
		out.print("<br>Total No. of Notifications: " + notifly.getTotalNotifications() + "</br>");
		List<Notification> notifications = notifly.getNotifications();
		display(notifications);
		out.print("</body></html>");
	}
	
	
	@RequestMapping( value = "/new/{app}/{message}/{categories}/tweet", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public void tweetNewNotification(HttpServletResponse response, HttpServletRequest request, @PathVariable String app, @PathVariable String message, @PathVariable String categories) throws Exception
	{
		notifly.runTimers();
		initialise(response,request);
		boolean uploaded = notifly.insertNotification(message,app,categories);
		if(uploaded)
		{
			out.print("<p> Notification uploaded successfully. Notification has been tweeted.</p>");
		}
		else out.print("<p class = 'error'>Notification could not be uploaded. Please make sure the categories entered are unique.</p>");
		out.print("<br>Total No. of Notifications: " + notifly.getTotalNotifications() + "</br>");
		List<Notification> notifications = notifly.getNotifications();
		display(notifications);
		out.print("</body></html>");
		if(uploaded) notiflyTwitter.tweetANotification(message, app, categories);
	}
	
	
	@RequestMapping( value = "/new/{app}/{message}", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public String newNotificiationWithoutCategory(HttpServletResponse response, HttpServletRequest request, @PathVariable String app, @PathVariable String message) throws Exception
	{
		return "redirect:/new/" + app + "/" + message + "/none";
	}
	
	
	@RequestMapping( value = "/new/{app}/{message}/tweet", method=RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public String tweetNewNotificiationWithoutCategory(HttpServletResponse response, HttpServletRequest request, @PathVariable String app, @PathVariable String message) throws Exception
	{
		return "redirect:/new/" + app + "/" + message + "/none/tweet";
	}
	
	//filter the database to show notifications by an Application
	
	@RequestMapping ( value = "/applications/{app}", method = RequestMethod.GET )
	@ResponseStatus( HttpStatus.OK )
	public void viewNotificationsByApp(HttpServletResponse response, HttpServletRequest request, @PathVariable String app) throws Exception
	{
		initialise(response,request);
		List<Notification> appNotifications = notifly.getNotificationsByApplication(app);
		if(appNotifications.size()==0 )	out.print("<br> There are no notifications uploaded by <strong>" + app + "</strong> </br>");
		else 
		{
			out.print("<br>" + appNotifications.size() + " notifications uploaded by the <strong>" + app + "</strong></br>");
			display(appNotifications);
		}
		out.print("</body></html>");
	}
	
	
	@RequestMapping( value = "/categories/{category}", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public void viewNotificationsByCategory(HttpServletResponse response, HttpServletRequest request, @PathVariable String category) throws Exception
	{
		initialise(response,request);
		List<Notification> categorised = notifly.getNotificationsByCategory(category);
		if(categorised.size() == 0) out.print("<br> There are no notifications in the <strong> " + category + "</strong></br>");
		else 
		{
			out.print("<br>" + categorised.size() + " notifications in <strong>" + category + "</strong></br>");
			display(categorised);
		}
		out.print("</body></html>");
	}
	
	
	@RequestMapping( value = "/new_user", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public String user_registration(HttpServletResponse response, HttpServletRequest request, final ModelMap model)
	{
		UserForm form = new UserForm();
		model.put("userForm", form);
		return "/jsp/user_reg.jsp";
	}
	
	
	@RequestMapping( value = "/frequenciesinfo", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public String learnAboutFrequencies(HttpServletResponse response, HttpServletRequest request)
	{
		return "/jsp/frequenciesinfo.jsp";
	}
	
	//registers a new user after the user has entered its' details into a form
	
	@RequestMapping( value = "/reg", method = RequestMethod.POST)
	@ResponseStatus( HttpStatus.OK )
	public String registrationByForm(final ModelMap model, @Valid final UserForm userForm, final BindingResult result, HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		if(result.hasErrors())	return "jsp/user_reg.jsp";
		registerNewUser(response,request,userForm);	
		return "redirect:/user";
	}
	
	public String registrationFormWithErrors(final ModelMap model, @Valid final UserForm userform, final BindingResult result,
			HttpServletResponse response, HttpServletRequest request)
	{
		return "jsp/user_reg.jsp";
	}
	
	public void registerNewUser(HttpServletResponse response, HttpServletRequest request, UserForm userForm) throws IOException
	{
		String emailAddress = userForm.getEmailAddress();
		String password = userForm.getPassword();
		String firstName = userForm.getFirstName();
		String lastName = userForm.getLastName();
		String[] categories = request.getParameterValues("category");
		String[] catFrequencies = request.getParameterValues("catfrequency");
		String[] applications = request.getParameterValues("application");
		String[] appFrequencies = request.getParameterValues("appfrequency");
		initialiseVariables(response,request);
		setLoginSession(emailAddress,password);
		initialise(response,request);
		notifly.insertNewUser(emailAddress, password, firstName, lastName, categories, applications,catFrequencies,appFrequencies);
	}
	
	public void setLoginSession(String emailAddress, String password){
		session.setAttribute("emailAddress", emailAddress); //stores the user's login credentials so that the user does not need to re-enter when viewing its subscriptions
		session.setAttribute("password",password);
	}
	
	
	@RequestMapping( value = "/user/categories", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public void getUserCategories(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		//print out all notifications belonging to categories that the user has subscribed to
		if(initialiseUser(request,response)==0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"),(String)session.getAttribute("password"));
			if(user == null) out.println("Incorrect email address or password");
			else
			{
				out.println("<h2> Welcome " + user.getFirstName() + " " + user.getLastName() + "</h2>");
				out.println("<a href = " + request.getContextPath() + "/user>User home</a>");
				if((user.getCategories() == null)||(user.getCategories().size() == 0)) out.println("<br> You are not subscribed to any Categories.");
				else {
					out.println("<br>Categories subscribed to: " + user.getCategories() + "</br>");
					for(String cat:user.getCategories())
					{
						out.println("<h3>Notifications uploaded by " + cat + "</h3></br>");
						display(notifly.getNotificationsByCategory(cat));
					}
				}
			}
		}		 
		out.print("</body></html>");
	}
	
	
	@RequestMapping( value = "/user/applications", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public void getUsersApplication(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		//print out all notifications uploaded by applications that the user has subscribed to
		if(initialiseUser(request,response)==0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"),(String)session.getAttribute("password"));
			if(user == null) out.println("Incorrect email address or password");
			else
			{
				out.println("<h2> Welcome " + user.getFirstName() + " " + user.getLastName() + "</h2>");
				out.println("<a href = " + request.getContextPath() + "/user>User home</a>");
				if((user.getApplications() == null)||(user.getApplications().size() == 0)) out.println("<br> You are not subscribed to any Applications.");
				else {
					out.println("<br>Applications subscribed to: " + user.getApplications() + "</br>");
					for(String app:user.getApplications())
					{
						out.println("<h3>Notifications uploaded by " + app + "</h3></br>");
						display(notifly.getNotificationsByApplication(app));
					}
				}
					
			} 
			out.print("</body></html>");
		}		
	}
	
	@RequestMapping( value = "/user/categories/add/{category}", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public void addUserCategory(HttpServletResponse response, HttpServletRequest request, @PathVariable String category) throws Exception
	{
		//adds one category to the user's list of subscribed categories
		if(initialiseUser(request,response)==0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"), (String)session.getAttribute("password"));
			if(user == null) out.println("Incorrect email address or password");
			String[] cats = category.split(":");
			try {
				notifly.addUserCategory(user, cats[0], cats[1]);
			}
			catch(ArrayIndexOutOfBoundsException e) {
				notifly.addUserCategory(user,cats[0],null);
			}			
			out.println("You are now subscribed to the following category: <strong> " + category + "</strong> has been added.");
			printUserPage(user);
			out.print("</body></html>");
		}
	}
	
	@RequestMapping( value = "/user/applications/add/{app}", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )	
	public void addUserApp(HttpServletResponse response, HttpServletRequest request, @PathVariable String app) throws Exception
	{
		if(initialiseUser(request,response)==0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"), (String)session.getAttribute("password"));
			if(user == null) out.println("Incorrect email address or password");
			String[] apps = app.split(":");
			try {
				notifly.addUserApp(user,apps[0],apps[1]);
			}
			catch(Exception e)
			{
				notifly.addUserApp(user,apps[0],null);
			}
			out.println("You are now subscribed to the following application: <strong> " + apps[0] + "</strong> ");
			printUserPage(user);
			out.print("</body></html>");
		}
	}

	
	@RequestMapping( value = "/user/applications/delete/{app}", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )	
	public void deleteUserApp(HttpServletResponse response, HttpServletRequest request, @PathVariable String app) throws Exception
	{
		//deletes one application from the list of subscribed Applications
		if(initialiseUser(request,response)==0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"), (String)session.getAttribute("password"));
			if(user == null) out.println("Incorrect email address or password");
			if(notifly.deleteUserApp(user, app)<0) out.println("The following application does not exist in your subscriptions: <strong>" + app + "</strong>" );
			else out.println("You are no longer subscribed to the following application: <strong> " + app + "</strong>");
			printUserPage(user);
		}
	}	
	
	@RequestMapping( value = "/user/categories/delete/{category}", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )	
	public void deleteUserCategory(HttpServletResponse response, HttpServletRequest request, @PathVariable String category) throws Exception
	{
		//deletes one category from the list of subscribed categories
		if(initialiseUser(request,response)==0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"), (String)session.getAttribute("password"));
			if(user == null) out.println("Incorrect email address or password");
			if(notifly.deleteUserCategory(user, category)<0) out.println("The following category does not exist in your subscriptions: <strong>" + category + "</strong>");
			else out.println("You are no longer subscribed to the following category: <strong> " + category + "</strong>");
			printUserPage(user);
			out.print("</body></html>"); 
		}
	}	
	
	//allows the user to login to its own user space. once logged in, the user's login credentials are stored
	@RequestMapping( value = "/user", method = RequestMethod.POST)
	@ResponseStatus( HttpStatus.OK )
	public void userHomepage(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		initialiseVariables(response,request);
		String emailAddress = request.getParameter("emailAddress");
		String password = request.getParameter("password");
		User user = notifly.getUser(emailAddress, password);
		if(user == null) {
			initialise(response,request);
			incorrectLoginCredentials(); 
		}
		else 
		{
			session.setAttribute("emailAddress", emailAddress);
			session.setAttribute("password", password);
			initialiseUser(request,response);
			printUserPage(user);
		}
		out.print("</body></html>");		
	}
	
	@RequestMapping( value = "/user/update", method = RequestMethod.POST)
	@ResponseStatus( HttpStatus.OK )
	public String updateUserPersonalDetails(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		if(initialiseUser(request,response) == 0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"),(String)session.getAttribute("password"));
			notifly.updateUserPersonalDetails(user, (String)request.getParameter("firstname"), (String)request.getParameter("lastname"));
			return "redirect:/user/account";
		}
		return "redirect:/login";
	}
	
	@RequestMapping( value = "/user/categories/update", method = RequestMethod.POST)
	@ResponseStatus( HttpStatus.OK )
	public String updateUserCategories(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		if(initialiseUser(request,response) == 0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"),(String)session.getAttribute("password"));
			notifly.deleteAllUserCategories(user);
			String[] categories = request.getParameterValues("category");
			if(categories != null)
			{
				String[] catFrequencies = request.getParameterValues("catfrequency");
				for(int i = 0; i<categories.length; i++)
				{
					notifly.addUserCategory(user, categories[i], catFrequencies[i]);
				}
			}
			return "redirect:/user/account";
		}
		return "redirect:/login";
	}
	
	@RequestMapping( value = "/user/applications/update", method = RequestMethod.POST)
	@ResponseStatus( HttpStatus.OK )
	public String updateUserApplications(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		if(initialiseUser(request,response) == 0)
		{
			User user = notifly.getUser((String)session.getAttribute("emailAddress"),(String)session.getAttribute("password"));
			notifly.deleteAllUserApplications(user);
			String[] applications = request.getParameterValues("application");
			if(applications != null)
			{
				String[] appFrequencies = request.getParameterValues("appfrequency");
				for(int i = 0; i<applications.length; i++)
				{
					notifly.addUserApp(user, applications[i], appFrequencies[i]);
				}
			}
			return "redirect:/user/account";
		}
		return "redirect:/login";
	}
	
	@RequestMapping( value = "/user", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public String getUserHome(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
		initialiseUser(request,response);
		/*requires no verification of credentials because they were already verified during login, the user will only divert to this page when wanting to come
		back to the user homepage after viewing its subscribed notifications
		*/
		if(session == null) return "redirect:/login";
		String emailAddress = (String)session.getAttribute("emailAddress");
		String password = (String)session.getAttribute("password");
		User user = notifly.getUser(emailAddress,password);
		if(user == null) return "redirect:/login"; 
		printUserPage(user);
		return null;
	}
	
	@RequestMapping( value = "/user/account")
	@ResponseStatus( HttpStatus.OK )
	public String getUserAccountDetails(HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception
	{
		if(initialiseUser(request,response) == 0) {
			User user = notifly.getUser((String)session.getAttribute("emailAddress"),(String)session.getAttribute("password"));
			HashMap<String,String> categories = user.getCategoriesMap();
			HashMap<String,String> applications = user.getApplicationsMap();
			model.addAttribute("categories",categories);
			model.addAttribute("applications",applications);
			model.addAttribute("user",user);
			return "/jsp/user_account.jsp";
		}
		else return "redirect:/login";
	}
	
	public void incorrectLoginCredentials()
	{
		out.println("Incorrect email address or password. <br>" +
		"<a href = " + request.getContextPath() + "/login>Try Again</a>");
	}
	
	@RequestMapping( value = "/login", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public String login(HttpServletResponse response, HttpServletRequest request, ModelMap map) throws Exception
	{
		initialise(response,request);
		return "/jsp/user_login.jsp";
	}
		
	@RequestMapping( value = "/logout", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )
	public String logout(HttpServletResponse response) throws Exception
	{
		if(session == null) return "redirect:/login"; 
		session.removeAttribute("emailAddress");
		session.removeAttribute("password");
		return "redirect:/login";
	}	
	
	@RequestMapping( value = "/forgottenpassword", method = RequestMethod.GET)
	@ResponseStatus( HttpStatus.OK )	
	public String forgottenPasswordForm() 
	{
		return "/jsp/forgotten_password.jsp";
	}
	
	@RequestMapping( value = "/forgottenpasswordsend", method = RequestMethod.POST)
	@ResponseStatus( HttpStatus.OK )
	public void forgottenPassword(HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception
	{
		initialise(response,request);
		String emailAddress = request.getParameter("emailAddress");
		String message = null;
		if(notifly.sendPasswordReminder(emailAddress))
		{
			message = "Your password has been sent to your email account.<br>" +
					"If you do not receive an email within the next few minutes, please contact the administrator.";
		}
		else
		{
			message = "<p class = 'error'>This email address has not been registered with NotiFLY. " +
					"<a href=" + request.getContextPath() + "/forgottenpassword>Try Again.</a></p>";
		}
		model.addAttribute("message",message);
		out.println(message);
	}
	
	public void printUserPage(User user)
	{
		out.println("<h2> Welcome " + user.getFirstName() + " " + user.getLastName() + "</h2>" +
		"<a href = " + request.getContextPath() + "/user/account >My Account</a><br>" + 
		"<a href = " + request.getContextPath() + "/user/applications > View Notifications by subscribed Applications </a><br>" +
		"<a href = " + request.getContextPath() + "/user/categories > View Notifications by subscribed Categories </a><br>" + 
		"<a href = " + request.getContextPath() + "/logout> Logout</a>");
	}
	
	public void printTitle()
	{
		out.print("<html><head><title>NotiFLY</title>" +
				" <link href=" + request.getContextPath() + "/styles/notiflyStyle rel=\"stylesheet\" type=\"text/css\"></head> " +
				"<body><center><img src=" + request.getContextPath() + "/images/notiflyTitle width=\"368\" height=\"243\" alt=\"title\" usemap=\"#logomap\">" +
						"<map name = \"logomap\">" +
								"<area shape = \"rect\" coords = \"0,0,366,243\" href = " + request.getContextPath() + "/ ></map>" +
						" <body>");
		out.print(getLinks());
	}
	
	//This decides which links should be displayed to the user depending on whether it is logged in or not.
	public String getLinks()
	{
		if((session.getAttribute("emailAddress")==null)||(session.getAttribute("password"))==null) 
			return "<br><a href = " + request.getContextPath() + "/login>Login </a><a href = " + request.getContextPath() + "/new_user>  Register</a> <hr>";
		else return "<br><a href = " + request.getContextPath() + "/user> User Home </a><a href = " + request.getContextPath() + "/logout>Logout</a><hr>";
	}
	
	public int initialiseUser(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		initialiseVariables(response,request);
		response.setContentType("text/html");
		out = this.response.getWriter();
		String emailAddress = (String)session.getAttribute("emailAddress");
		String password = (String)session.getAttribute("password");
		printTitle();
		if(emailAddress == null||password == null)
		{
			out.println("You are not logged in. Please <a href= " + request.getContextPath() + "/login> log in</a>.");
			out.print("</body></html>");
			return -1;
		}
		return 0;
	}
	
	public void initialise(HttpServletResponse response, HttpServletRequest request) throws IOException
	{
		initialiseVariables(response,request);
		out = this.response.getWriter();
		response.setContentType("text/html");
		printTitle();
	}
	
	public void initialiseVariables(HttpServletResponse response, HttpServletRequest request) throws IOException
	{
		this.request = request;
		this.session = this.request.getSession();
		this.response = response;
	}
	
	//stores the notifications in a table
	public void display(List<Notification> notifications)
	{
		String table = "<table id = 'notifications'>" +
				"<tr> <th> MESSAGE </th>" +
				"<th> APPLICATION </th>" +
				"<th> UPLOADED ON </th>" +
				"<th> CATEGORIES </th> </tr>";
		out.print(table);
		for(Notification o: notifications)
		{
			out.println("<td> " + o.getMessage() + "</td>");
			out.println("<td> <a href = '" + request.getContextPath() + "/applications/" + o.getApplication() + "/'>" + o.getApplication() + "</a></td>");
			out.println("<td> " + o.getUpdateDate() + "</td>");
			out.println("<td>");
			if(o.getCategories() != null)
			{
				int count = 0;
				for(String s: o.getCategories())
				{
					out.print("<a href = '" + request.getContextPath() + "/categories/" + s + "/'>" + s + "</a>");
					count++;
					if(count != o.getCategories().size()) out.print(", ");
				}
			}
			out.println("</td></tr>");
		}
		out.print("</table>");
	}	
}
