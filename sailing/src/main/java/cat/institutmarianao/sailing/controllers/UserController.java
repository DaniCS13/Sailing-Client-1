package cat.institutmarianao.sailing.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cat.institutmarianao.sailing.components.forms.validators.UserFormValidator;
import cat.institutmarianao.sailing.model.User;
import cat.institutmarianao.sailing.model.User.Role;
import cat.institutmarianao.sailing.model.forms.UserForm;
import cat.institutmarianao.sailing.services.UserService;
import cat.institutmarianao.sailing.validation.groups.OnUserCreate;
import cat.institutmarianao.sailing.validation.groups.OnUserUpdate;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/users")
public class UserController {
	@Autowired
	private UserFormValidator userFormValidator;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageSource messageSource;

	@InitBinder(value = "userForm")
	public void initialiseBinder(WebDataBinder binder) {
		binder.addValidators(userFormValidator);
	}

	@GetMapping("/list")
	public ModelAndView allUsersList() {

		ModelAndView usersView = new ModelAndView("users");
		usersView.getModelMap().addAttribute("users", userService.getAllUsers());
		return usersView;
	}

	@GetMapping("/new/{user_role}")
	public ModelAndView newUser(@PathVariable("user_role") String userRole) {

		ModelAndView newUserView = new ModelAndView("user");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		UserForm userForm = new UserForm();

		if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
			userForm.setRole(Role.ADMIN);
		} else {
			userForm.setRole(Role.CLIENT);
		}

		newUserView.getModelMap().addAttribute("userForm", userForm);
		newUserView.getModelMap().addAttribute("edit", false);

		return newUserView;
	}

	@PostMapping("/new")
	public String submitNewUser(HttpServletRequest request, @Validated(OnUserCreate.class) UserForm userForm,
			BindingResult result, ModelMap modelMap) {

		if (result.hasErrors()) {
			modelMap.addAttribute("userForm", userForm);
			modelMap.addAttribute("edit", false);
			return "user";
		}

		if (userService.check(userForm.getUser().getUsername())) {
			result.addError(new ObjectError("userForm", messageSource.getMessage("validation.user.already.exists",
					new Object[] { userForm.getUser().getUsername() }, LocaleContextHolder.getLocale())));
			modelMap.addAttribute("userForm", userForm);
			modelMap.addAttribute("edit", false);
			return "user";
		}

		userService.add(userForm.getUser());

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/users/list";
		}

		try {
			// https://www.baeldung.com/spring-security-auto-login-user-after-registration
			request.login(userForm.getUsername(), userForm.getPassword());
		} catch (ServletException e) {
			// Can not perform login
		}
		return "redirect:/";
	}

	@GetMapping("/edit")
	public ModelAndView editUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		User user = (User) userService.loadUserByUsername(username);

		ModelAndView editUserView = new ModelAndView("user");
		editUserView.getModelMap().addAttribute("user", user);
		editUserView.getModelMap().addAttribute("userForm", new UserForm(user));
		editUserView.getModelMap().addAttribute("edit", true);

		return editUserView;
	}

	@PostMapping("/edit")
	public String submitEditUser(@Validated(OnUserUpdate.class) UserForm userForm, BindingResult result,
			ModelMap modelMap, Model model) {

		if (result.hasErrors()) {
			modelMap.addAttribute("userForm", userForm);
			modelMap.addAttribute("edit", true);
			return "user";
		}

		if (userForm.getPassword() == null || userForm.getPassword().isBlank()) {
			userForm.setPassword(null);
		}

		User user = userForm.getUser();

		userService.update(user);
		model.addAttribute("user", user);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		if (!authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/users/list";
		}

		return "redirect:/trips/booked";
	}

	@GetMapping("/remove/{username}")
	public String removeUser(@PathVariable("username") String username) {

		try {
			userService.remove(username);
		} catch (Exception e) {
			return "redirect:/users/list?removeerror=" + username;
		}
		return "redirect:/users/list";
	}
}
