package cat.institutmarianao.sailing.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
@PropertySource("classpath:jwt.properties")
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:application.properties")
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Value("${webService.host}")
	private String webServiceHost;

	@Value("${webService.port}")
	private String webServicePort;

	@Value("${exception.badCredentials}")
	private String badCredentialsMessage;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${jwt.response.authorization.header}")
	private String headerAuthorization;

	@Value("${jwt.response.authorization.value}")
	private String bearer;

	@Value("${jwt.cookie.name}")
	private String jwtCookieName;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private HttpServletResponse servletResponse;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String uri = webServiceHost + ":" + webServicePort + "/authenticate";

		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> postBody = new HashMap<>();
		postBody.put("username", username);
		postBody.put("password", password);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(postBody, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);

			// if the registration is successful
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				String token = extractToken(response);
				Cookie cookie = new Cookie(jwtCookieName, token);
				cookie.setPath("/");
				servletResponse.addCookie(cookie);
				return jwtUtils.getAuthentication(token);
			}
		} catch (Exception ex) {
		}
		throw new BadCredentialsException(badCredentialsMessage);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private String extractToken(ResponseEntity<String> response) {
		String bearerToken = response.getHeaders().get(headerAuthorization).get(0);
		if (StringUtils.hasText(bearerToken) && bearerToken.contains(bearer)) {
			return bearerToken.replaceFirst(bearer, "").trim();
		}
		return null;
	}
}
