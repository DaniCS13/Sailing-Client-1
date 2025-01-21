package cat.institutmarianao.sailing.security;

import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.util.WebUtils;

import cat.institutmarianao.sailing.model.User;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@PropertySource("classpath:jwt.properties")
public class WebSecurityConfiguration {
	@Value("${jwt.response.authorization.header}")
	private String headerAuthorization;

	@Value("${jwt.response.authorization.value}")
	private String bearer;

	@Value("${jwt.cookie.name}")
	private String jwtCookieName;

	protected static final String LOGIN_URL = "/login";
	protected static final String LOGIN_FAIL_URL = "/loginfailed";
	protected static final String LOGOUT_SUCCESS_URL = "/";

	// Tots els usuaris editar el seu perfil i accedir a reserves
	// (admin les
	// pot veure totes i els clients només les seves)
	protected static final String[] USERS_URLS = { "/users/edit", "/users/edit", "/trips/booked" };
	// ADMIN pot fer totes les operacions d'usuari, a més de editar el seu perfil
	protected static final String[] ADMIN_URLS = { "/users/list" };
	// CLIENT pot fer reserves
	protected static final String[] CLIENT_URLS = { "/trips/book/**" };

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationProvider authenticationProvider)
			throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(authenticationProvider)
				.build();
	}

	@Bean
	HttpClient httpClient(CustomHttpClientFactory customHttpClientFactory) throws Exception {
		return customHttpClientFactory.getObject();
	}

	@Bean
	@RequestScope
	RestTemplate restTemplate(HttpClient httpClient, RestTemplateBuilder builder, HttpServletRequest servletRequest) {
		Cookie jwtCookie = WebUtils.getCookie(servletRequest, jwtCookieName);
		if (jwtCookie == null) {
			return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
		}
		String token = jwtCookie.getValue();
		return builder.requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient))
				.additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
					request.getHeaders().add(headerAuthorization, bearer + " " + token);
					return execution.execute(request, body);
				}).build();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
				.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
				.requestMatchers(USERS_URLS).hasAnyRole(User.ADMIN, User.CLIENT).requestMatchers(ADMIN_URLS)
				.hasAnyRole(User.ADMIN).requestMatchers(CLIENT_URLS).hasAnyRole(User.CLIENT).anyRequest().permitAll())
				.formLogin(formLogin -> formLogin.loginPage(LOGIN_URL).failureUrl(LOGIN_FAIL_URL))
				.logout(logout -> logout.logoutSuccessUrl(LOGOUT_SUCCESS_URL).deleteCookies("JSESSIONID",
						jwtCookieName))
				.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage(LOGIN_URL)).build();
	}
}
