package cat.institutmarianao.sailing.ws.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
@PropertySource("classpath:jwt.properties")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

	@Value("${jwt.payload_auth}")
	private String payloadAuth;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration.in.ms}")
	private long expirationInMs;

	@Value("${jwt.refresh.expiration.in.ms}")
	private long refreshExpirationInMs;

	public String generateToken(String principal, Collection<? extends GrantedAuthority> authorities) {

		Claims claims = Jwts.claims();

		claims.setSubject(principal);
		claims.setExpiration(new Date(System.currentTimeMillis() + expirationInMs));
		claims.put(payloadAuth, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));

		SecretKey signature = Keys.hmacShaKeyFor(secret.getBytes());

		return Jwts.builder().setClaims(claims).signWith(signature).compact();
	}

	public String generateRefreshToken(Map<String, Object> claims, String principal) {

		SecretKey signature = Keys.hmacShaKeyFor(secret.getBytes());

		return Jwts.builder().setClaims(claims).setSubject(principal).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + refreshExpirationInMs)).signWith(signature)
				.compact();
	}

	public UsernamePasswordAuthenticationToken getAuthentication(String token) {
		try {
			SecretKey signature = Keys.hmacShaKeyFor(secret.getBytes());

			Claims claims = Jwts.parserBuilder().setSigningKey(signature).build().parseClaimsJws(token).getBody();

			String subject = claims.getSubject();

			@SuppressWarnings("unchecked")
			Collection<? extends GrantedAuthority> authorities = claims.get(payloadAuth, ArrayList.class).stream()
					.map(auth -> new SimpleGrantedAuthority(auth.toString())).toList();

			return new UsernamePasswordAuthenticationToken(subject, null, authorities);
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("Invalid credentials", ex);
		} catch (ExpiredJwtException e) {
			throw e;
		}
	}

}