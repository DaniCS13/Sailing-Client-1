package cat.institutmarianao.sailing.ws.security;

import java.io.IOException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.response.authorization.header}")
	private String headerAuthorization;

	@Value("${jwt.response.authorization.value}")
	private String bearer;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {
			String jwtToken = extractToken(request);

			if (!Strings.isEmpty(jwtToken)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtils
						.getAuthentication(jwtToken);

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		} catch (ExpiredJwtException e) {
			// https://www.javainuse.com/webseries/spring-security-jwt/chap7
			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			// allow for Refresh Token creation if following conditions are true.
			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
				allowRefreshToken(e, request);
			} else {
				response.sendError(401, "ExpiredJwtException");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(headerAuthorization);
		if (StringUtils.hasText(bearerToken) && bearerToken.contains(bearer)) {
			return bearerToken.replaceFirst(bearer, "").trim();
		}
		return null;
	}

	// https://www.javainuse.com/webseries/spring-security-jwt/chap7
	private void allowRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());
	}

}