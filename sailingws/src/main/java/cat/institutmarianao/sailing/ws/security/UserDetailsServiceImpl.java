package cat.institutmarianao.sailing.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException("User with email='" + username + "' not found"));

		return new UserDetailsImpl(user);
	}

}
