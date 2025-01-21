package cat.institutmarianao.sailing.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import cat.institutmarianao.sailing.model.User;

public interface UserService extends UserDetailsService {
	List<User> getAllUsers();

	User add(User user);

	void update(User user);

	void remove(String username);

	boolean check(String username);
}
