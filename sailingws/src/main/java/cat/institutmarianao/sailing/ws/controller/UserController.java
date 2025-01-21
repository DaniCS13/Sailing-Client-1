package cat.institutmarianao.sailing.ws.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.sailing.ws.model.Admin;
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.model.dto.UserDto;
import cat.institutmarianao.sailing.ws.service.UserService;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Tag(name = "User", description = "User API")

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
	@Autowired
	private ConversionService conversionService;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Operation(summary = "Check if user exists", description = "Check if a user exists in the database. The response is true if exists, false otherwise")
	@ApiResponse(responseCode = "200", content = { @Content() }, description = "Check goes ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error checking the user. See response body for more details")
	@GetMapping(value = "/check/{username}")
	public @ResponseBody boolean check(@PathVariable("username") @NotBlank String username) throws Exception {
		return userService.existsById(username);
	}

	/* Swagger */
	@Operation(summary = "Get user by id")
	@ApiResponse(responseCode = "200", description = "User retrieved ok")
	@ApiResponse(responseCode = "404", description = "Resource not found")
	/**/
	@GetMapping("/get/by/username/{username}")
	public UserDto findByUsername(@PathVariable("username") @NotBlank String username) {
		return conversionService.convert(userService.getByUsername(username), UserDto.class);
	}

	@Operation(summary = "Retrieve all users", description = "Retrieve all users from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDto.class))) }, description = "Users retrieved ok")
	@GetMapping(value = "/find/all")
	@RolesAllowed("ROLE_ADMIN")
	public @ResponseBody List<UserDto> findAll() {

		List<User> users = userService.findAll();

		List<UserDto> usersDto = new ArrayList<>(users.size());
		for (User user : users) {
			UserDto userDto = conversionService.convert(user, UserDto.class);
			usersDto.add(userDto);
		}

		return usersDto;
	}

	@Operation(summary = "Save a user", description = "Saves a user in the database. The response is the stored user from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }, description = "User saved ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error saving the user. See response body for more details")
	@PostMapping(value = "/save")
	public @ResponseBody UserDto save(@RequestBody @NotNull UserDto userDto) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		if (!authorities.stream().anyMatch(ga -> "ROLE_ADMIN".equals(ga.getAuthority()))) {
			if (!Role.CLIENT.equals(userDto.getRole())) {
				throw new Exception("Clients only can create client accounts");
			}
		}

		if (userService.existsById(userDto.getUsername())) {
			throw new Exception("User with username " + userDto.getUsername() + " already exists");
		}
		return conversionService.convert(userService.save(convertAndEncodePassword(userDto)), UserDto.class);
	}

	/* Swagger */
	@Operation(summary = "Update a user")
	@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(anyOf = {
			Admin.class, Client.class }, discriminatorProperty = "role")) }, description = "User updated ok")
	@ApiResponse(responseCode = "404", content = {
			@Content(mediaType = "application/json") }, description = "Resource not found")
	/**/
	@PutMapping("/update")
	@Validated(OnUserUpdate.class)
	public UserDto update(@RequestBody @Valid UserDto userDto) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		if (!authorities.stream().anyMatch(ga -> "ROLE_CLIENT".equals(ga.getAuthority()))) {
			if (!authentication.getName().equals(userDto.getUsername())) {
				throw new Exception("Clients only can update themself");
			}
		}

		return conversionService.convert(userService.update(convertAndEncodePassword(userDto)), UserDto.class);
	}

	/* Swagger */
	@Operation(summary = "Delete a user")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json") }, description = "User deleted ok")
	/**/
	@DeleteMapping("/delete/by/username/{username}")
	public void deleteByUsername(@PathVariable("username") @NotBlank String username) throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		// TODO FIXME Clientes no eliminan y admin reciben el error
		if (!authorities.stream().anyMatch(ga -> "ROLE_CLIENT".equals(ga.getAuthority()))) {
			if (!authentication.getName().equals(username)) {
				throw new Exception("Clients only can delete themself");
			}
		}

		userService.deleteByUsername(username);
	}

	private User convertAndEncodePassword(UserDto userDto) {
		String rawPassword = userDto.getPassword();
		if (rawPassword != null) {
			userDto.setPassword(passwordEncoder.encode(rawPassword));
		}
		return conversionService.convert(userDto, User.class);
	}
}
