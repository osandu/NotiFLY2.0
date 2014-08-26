package uk.ac.ucl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

public class EmailDoesNotExistsValidator implements ConstraintValidator<EmailDoesNotExists,String> {
	
	@Autowired
	private NotiflyDatabase notifly;
	
	public void initialize(EmailDoesNotExists constraint){
		
	}

	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if(notifly.emailExists(object))
		{
			return false;
		}
		return true;
	}

}
