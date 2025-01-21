package cat.institutmarianao.sailing.ws.validation.constraints.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Action.Type;
import cat.institutmarianao.sailing.ws.validation.constraints.Tracking;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrackingImpl implements ConstraintValidator<Tracking, List<Action>> {

	private static final List<Type> LAST_ACTIONS = Arrays.asList(Type.CANCELLATION, Type.DONE);

	private static final Map<Type, List<Type>> ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS = new EnumMap<>(Type.class);

	// BOOKING->CANCELLATION
	// BOOKING->DONE
	// BOOKING->RESCHEDULING->[RESCHEDULING|CANCELLATION|DONE]
	static {
		ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS.put(Type.BOOKING, new ArrayList<>());
		ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS.put(Type.CANCELLATION, Arrays.asList(Type.BOOKING, Type.RESCHEDULING));
		ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS.put(Type.RESCHEDULING, Arrays.asList(Type.BOOKING, Type.RESCHEDULING));
		ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS.put(Type.DONE, Arrays.asList(Type.BOOKING, Type.RESCHEDULING));
	}

	@Autowired
	private MessageSource messageSource;

	private boolean closed;

	@Override
	public boolean isValid(List<Action> tracking, ConstraintValidatorContext context) {

		if (tracking == null) {
			addError(context, "error.Tracking.is.null");
			return false;
		}
		if (tracking.isEmpty()) {
			addError(context, "error.Tracking.is.empty");
			return false;
		}

		// Date validations
		Date previousDate;
		Date currentDate = null;

		Action previousAction;
		Action currentAction = null;

		closed = false;

		boolean valid = true;

		// Assume tracking is a stack; comes in reverse order
		for (int pos = tracking.size() - 1; pos >= 0; pos--) {
			previousAction = currentAction;
			currentAction = tracking.get(pos);

			valid = valid && !isAlreadyClosed(context, currentAction, pos);
			valid = valid && isAllowedAction(context, currentAction, previousAction, pos);

			previousDate = currentDate;
			currentDate = currentAction.getDate();

			valid = valid && isCurrentDateAfterPrevious(context, previousDate, currentDate, pos);
		}
		if (!valid) {
			addError(context, tracking);
		}
		return valid;
	}

	private boolean isAlreadyClosed(ConstraintValidatorContext context, Action action, int pos) {
		boolean alreadyClosed = closed;
		if (alreadyClosed) {
			addError(context, "error.Tracking.before.close", pos);
		} else {
			closed = LAST_ACTIONS.contains(action.getType());
		}
		return alreadyClosed;
	}

	private boolean isAllowedAction(ConstraintValidatorContext context, Action action, Action previousAction, int pos) {
		Type currentType = action.getType();
		Type previousType = previousAction == null ? null : previousAction.getType();

		boolean allowed = (previousType == null && ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS.get(currentType).isEmpty()
				|| ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS.get(currentType).contains(previousType));

		if (!allowed) {
			String nothing = messageSource.getMessage("error.Tracking.nothing", null, LocaleContextHolder.getLocale());

			String allowedTypesString = ACTION_AND_ITS_ALLOWED_PREVIOUS_ACTIOS.get(currentType).toString();
			String previousTypeString = previousType == null ? nothing : previousType.name();

			addError(context, "error.Tracking.action.not.allowed", pos, currentType.name(), allowedTypesString,
					previousTypeString);
		}

		return allowed;
	}

	private boolean isCurrentDateAfterPrevious(ConstraintValidatorContext context, Date previousDate, Date currentDate,
			int pos) {
		if (previousDate == null) {
			return true;
		}
		if (previousDate.after(currentDate)) {
			addError(context, "error.Tracking.wrong.date", pos);
			return false;
		}
		return true;
	}

	private void addError(ConstraintValidatorContext context, String code, Object... args) {
		context.buildConstraintViolationWithTemplate(
				messageSource.getMessage(code, args, LocaleContextHolder.getLocale())).addConstraintViolation();
	}

	private void addError(ConstraintValidatorContext context, List<Action> tracking) {
		context.buildConstraintViolationWithTemplate("Tracking=["
				+ tracking.stream().map(a -> a.getType().toString()).collect(Collectors.joining(", ")) + "]")
				.addConstraintViolation();
	}
}
