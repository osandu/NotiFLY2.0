package uk.ac.ucl;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
import javax.validation.Constraint;
import javax.validation.Payload;

/**This is an interface that creates a custom annotation for the hibernate validation, that will check that the email 
 * address that the user is registering does not exist in the database. **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=EmailDoesNotExistsValidator.class)
public @interface EmailDoesNotExists {
	String message() default "Email doesn't exists";
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
