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

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class User 
{
	private String emailAddress, password, firstName, lastName;
	//The following maps are a list of notifications relevant to the user's subscribed applications and categories
	private HashMap<String,List<Notification>> userCatNotes, userAppNotes;
	//The following maps are a list of a user's subscribed applications and categories along with their frequencies
	private HashMap<String,String> applications, categories;
	
	public User(String emailAddress, String password, String firstName, String lastName)
	{
		this.emailAddress = emailAddress;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		categories = new HashMap<String,String>();
		applications = new HashMap<String,String>();
	}
	
	public String getEmailAddress()
	{
		return emailAddress;
	}
	
	public void setEmailAddress(String address)
	{
		this.emailAddress = address;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String name)
	{
		this.firstName = name;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String name)
	{
		this.lastName = name;
	}
	
	public HashMap<String,String> getCategoriesMap()
	{
			return categories;
	}
	
	public HashMap<String,String> getApplicationsMap()
	{
		return applications;
	}
	
	public List<String> getCategories()
	{
		if(categories == null) return null;
		List<String> sortedCategories = new ArrayList<String>(categories.keySet());
		Collections.sort(sortedCategories);
		return sortedCategories;
	}
	
	public void addCategory(String cat, String frequency)
	{
		if(categories == null) this.categories = new HashMap<String, String>();
		categories.put(cat, frequency);
	}
	
	public void removeCategory(String cat)
	{
		categories.remove(cat);
	}
	
	public void addApplication(String app, String frequency)
	{
		if(applications == null) this.applications = new HashMap<String, String>();
		applications.put(app, frequency);
	}
	
	public void removeApplication(String app)
	{
		applications.remove(app);
	}
	
	public String getCategoryFrequency(String category)
	{
		return categories.get(category);
	}
	
	public String getApplicationFrequency(String application)
	{
		return applications.get(application);
	}
	
	public boolean isSubscribedToCategory(String cat)
	{
		return categories.containsKey(cat);
	}
	
	public void addCategorisedNotification(String category,List<Notification> notifications)
	{
		userCatNotes.put(category, notifications);
	}
	
	public List<String> getApplications()
	{
		if(applications == null) return null;
		List<String> sortedApplications = new ArrayList<String>(applications.keySet());
		Collections.sort(sortedApplications);
		return sortedApplications;
	}
	
	public boolean isSubscribedToApplication(String app)
	{
		return applications.containsKey(app);
	}
	
	public void addAppNotification(String app,List<Notification> notifications)
	{
		userAppNotes.put(app, notifications);
	}
}
