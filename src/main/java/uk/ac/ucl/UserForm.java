package uk.ac.ucl;

import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

public class UserForm {
	
	@EmailDoesNotExists (message = "This email address already exists. Please login or use the forgotten password service.")
	@NotEmpty (message = "Please enter an Email Address")
	@Email (message = "Please enter a valid Email Address")
	private String emailAddress;
	
	@NotEmpty (message = "Please enter a password of more than 5 characters.")
	@Size(min = 5, message = "Please enter a password of more than 5 characters.")
	private String password;
	
	@NotEmpty(message = "Please enter your First Name")
	@Size(min = 2, message = "Please enter a valid First Name of 2 or more characters")
	private String firstName;
	
	@NotEmpty(message = "Please enter your Last Name")
	@Size(min = 2, message = "Please enter a valid Last Name of 2 or more characters")
	private String lastName;
	
	public String getEmailAddress()
	{
		return this.emailAddress;
	}
	
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
}
