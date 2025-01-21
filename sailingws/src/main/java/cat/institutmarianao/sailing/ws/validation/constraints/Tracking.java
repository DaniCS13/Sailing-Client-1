package cat.institutmarianao.sailing.ws.validation.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import cat.institutmarianao.sailing.ws.validation.constraints.impl.TrackingImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = TrackingImpl.class)
@Target({ METHOD, FIELD, PARAMETER, LOCAL_VARIABLE })
@Retention(RUNTIME)
public @interface Tracking {
	String message() default "{error.Tracking.is.not.valid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
