package cat.institutmarianao.sailing.components.forms.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cat.institutmarianao.sailing.model.forms.UserForm;

@Component
public class UserFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserForm userForm = (UserForm) target;

		if (!userForm.getPassword().equals(userForm.getVerify())) {
			errors.rejectValue("verify", "userFormValidation.password.does.not.match");
		}
	}

}