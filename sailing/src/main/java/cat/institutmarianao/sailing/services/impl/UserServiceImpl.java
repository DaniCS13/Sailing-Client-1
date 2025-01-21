package cat.institutmarianao.sailing.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cat.institutmarianao.sailing.model.User;
import cat.institutmarianao.sailing.services.UserService;

@Service
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {

	private static final String USERNAME = "username";

	private static final String USERS_SERVICE = "/users";
	private static final String USERS_GET_BY_USERNAME = USERS_SERVICE + "/get/by/username/{" + USERNAME + "}";
	private static final String USERS_FIND_ALL = USERS_SERVICE + "/find/all";
	private static final String USERS_SAVE = USERS_SERVICE + "/save";
	private static final String USERS_UPDATE = USERS_SERVICE + "/update";
	private static final String USERS_DELETE_BY_USERNAME = USERS_SERVICE + "/delete/by/username/{" + USERNAME + "}";
	private static final String USERS_CHECK_USERNAME = USERS_SERVICE + "/check/{" + USERNAME + "}";

	@Value("${webService.host}")
	private String webServiceHost;

	@Value("${webService.port}")
	private String webServicePort;

	@Autowired
	private RestTemplate restTemplate;

	@Override /* Spring security */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final String baseUri = webServiceHost + ":" + webServicePort + USERS_GET_BY_USERNAME;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(USERNAME, username);

		User response = restTemplate.getForObject(uriTemplate.buildAndExpand(uriVariables).toUriString(), User.class);
		return response;
	}

	@Override
	public List<User> getAllUsers() {
		final String uri = webServiceHost + ":" + webServicePort + USERS_FIND_ALL;

		ResponseEntity<User[]> response = restTemplate.getForEntity(uri, User[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public boolean check(String username) {
		final String baseUri = webServiceHost + ":" + webServicePort + USERS_CHECK_USERNAME;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(USERNAME, username);

		ResponseEntity<Boolean> response = restTemplate
				.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), Boolean.class);
		return response.getBody();
	}

	@Override
	public User add(User user) {
		final String uri = webServiceHost + ":" + webServicePort + USERS_SAVE;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<User> request = new HttpEntity<>(user, headers);

		return restTemplate.postForObject(uri, request, User.class);
	}

	@Override
	public void update(User user) {
		final String uri = webServiceHost + ":" + webServicePort + USERS_UPDATE;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<User> request = new HttpEntity<>(user, headers);

		restTemplate.put(uri, request);
	}

	@Override
	public void remove(String username) {
		final String baseUri = webServiceHost + ":" + webServicePort + USERS_DELETE_BY_USERNAME;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(USERNAME, username);

		restTemplate.delete(uriTemplate.buildAndExpand(uriVariables).toUriString());
	}
}
