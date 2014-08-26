package uk.ac.ucl;

public class UserSubscription 
{
	private String email_address, type, sub, frequency;
	
	public UserSubscription(String email_address, String type, String sub, String frequency)
	{
		this.email_address = email_address;
		this.type = type;
		this.sub = sub;
		this.frequency = frequency;
	}

	public String getEmailAddress()
	{
		return email_address;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getSub()
	{
		return sub;
	}
	
	public String getFrequency()
	{
		return frequency;
	}
}
