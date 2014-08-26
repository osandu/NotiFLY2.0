package uk.ac.ucl;

import org.springframework.social.twitter.api.Twitter;
import org.springframework.beans.factory.annotation.*;

public class NotiflyTwitter {
	
	private Twitter twitterTemplate;	
	
	@Autowired
	public NotiflyTwitter(Twitter twitterTemplate)
	{
		this.twitterTemplate = twitterTemplate;
	}

	public void tweetANotification(String message, String application, String categories)
	{
		if(categories.equals("none")) categories = "";
		String status = message + "\nUploaded By " + application + "\n" + categories;
		twitterTemplate.timelineOperations().updateStatus(status);
	}

}
