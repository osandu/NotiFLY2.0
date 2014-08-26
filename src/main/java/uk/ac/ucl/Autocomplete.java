package uk.ac.ucl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.*;

public class Autocomplete {
		private String categories_string, applications_string;
		private String[] categories, applications; 	
	 
	 	@Autowired
	    public Autocomplete(String apps,String cats) {
	    	applications_string = apps;
	    	categories_string = cats;
	    	applications = applications_string.split(",");
	        categories = categories_string.split(",");
	    }
	    
	    
	    public List<String> getCategories(String query) {
	        String category = null;
	        query = query.toLowerCase();
	        List<String> matched = new ArrayList<String>();
	        for(int i=0; i<categories.length; i++) {
	            category = categories[i].toLowerCase();
	            if(category.startsWith(query)) {
	                matched.add(categories[i]);
	            }
	        }
	        return matched;
	    }
	    
	    public List<String> getApplications(String query) {
	        String application = null;
	        query = query.toLowerCase();
	        List<String> matched = new ArrayList<String>();
	        for(int i=0; i<applications.length; i++) {
	            application = applications[i].toLowerCase();
	            if(application.startsWith(query)) {
	                matched.add(applications[i]);
	            }
	        }
	        return matched;
	    }

}
