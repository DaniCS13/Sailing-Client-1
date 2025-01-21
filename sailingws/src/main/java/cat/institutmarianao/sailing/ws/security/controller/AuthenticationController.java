package cat.institutmarianao.sailing.ws.security.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.sailing.ws.security.JwtUtils;
import cat.institutmarianao.sailing.ws.security.model.AuthCredentials;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Authentication", description = "Authentication API")

@RestController
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Value("${jwt.response.authorization.header}")
	private String headerAuthorization;

	@Value("${jwt.response.authorization.value}")
	private String bearer;

	@PostMapping(value = "/authenticate")
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthCredentials authenticationRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

			final String token = jwtUtils.generateToken(authentication.getName(), authentication.getAuthorities());

			return ResponseEntity.ok().header(headerAuthorization, String.format("%s %s", bearer, token)).build();
		} catch (DisabledException e) {
			return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value())).body("User disabled");
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value())).body("Bad credentials");
		}
	}

	@Hidden
	@GetMapping(value = "/refreshtoken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		Claims claims = (Claims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtUtils.generateRefreshToken(expectedMap, expectedMap.get("sub").toString());
		return ResponseEntity.ok().header(headerAuthorization, String.format("%s %s", bearer, token)).build();
	}

	private Map<String, Object> getMapFromIoJsonwebtokenClaims(Claims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}
}