package cat.institutmarianao.sailing.ws.validation.constraints.impl;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import cat.institutmarianao.sailing.ws.model.Action.Type;
import cat.institutmarianao.sailing.ws.model.Booking;
import cat.institutmarianao.sailing.ws.model.Cancellation;
import cat.institutmarianao.sailing.ws.model.Done;
import cat.institutmarianao.sailing.ws.model.Rescheduling;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.service.UserService;
import cat.institutmarianao.sailing.ws.validation.constraints.Performer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PerformerImpl implements ConstraintValidator<Performer, Object> {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserService userService;

	private String property;
	private Role[] allowedRoles;

	@Override
	public void initialize(Performer constraintAnnotation) {
		property = constraintAnnotation.property();
		allowedRoles = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		if (messageSource == null || userService == null) {
			return true; // We have no context, so is not under @Valid annotation
		}

		Object performerValue = new BeanWrapperImpl(object).getPropertyValue(property);

		String username;
		if (performerValue instanceof String usernameProperty) {
			username = usernameProperty;
		} else if (performerValue instanceof User user) {
			username = user.getUsername();
		} else {
			addError(context, "error.Performer.property.is.not.instance.of.user.or.string", property);
			return false;
		}

		if (username == null) {
			return true; // Depends on @NotNull annotation. If allows nulls, performer is OK being null
		}

		Type type;
		if (object instanceof Booking) {
			type = Type.BOOKING;
		} else if (object instanceof Cancellation) {
			type = Type.CANCELLATION;
		} else if (object instanceof Done) {
			type = Type.DONE;
		} else if (object instanceof Rescheduling) {
			type = Type.RESCHEDULING;
		} else {
			addError(context, "error.Performer.annotation.should.be.attached.to.Action.or.ActionDto.subtypes",
					property);
			return false;
		}

		User performer = userService.getByUsername(username);

		return validate(context, type, performer, allowedRoles);
	}

	private boolean validate(ConstraintValidatorContext context, Type type, User performer, Role[] roles) {
		if (ArrayUtils.contains(roles, (performer.getRole()))) {
			return true;
		}
		addError(context, "error.Performer.role.invalid.for.action", type, Arrays.toString(roles), performer.getRole());
		return false;
	}

	private void addError(ConstraintValidatorContext context, String code, Object... args) {
		context.buildConstraintViolationWithTemplate(
				messageSource.getMessage(code, args, LocaleContextHolder.getLocale())).addPropertyNode(property)
				.addConstraintViolation();
	}
}
