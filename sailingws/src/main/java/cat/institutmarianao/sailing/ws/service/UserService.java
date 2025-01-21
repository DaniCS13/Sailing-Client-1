package cat.institutmarianao.sailing.ws.service;

import java.util.List;

import cat.institutmarianao.sailing.ws.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public interface UserService {

	List<User> findAll();

	User getByUsername(@NotBlank String username);

	User save(@NotNull @Valid User user);

	User update(@NotNull @Valid User user);

	void deleteByUsername(@NotBlank String username);

	boolean existsById(@NotBlank String username);
}