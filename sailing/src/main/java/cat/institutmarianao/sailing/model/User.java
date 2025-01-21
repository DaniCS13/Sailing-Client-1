package cat.institutmarianao.sailing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/* JSON annotations */
/*
 * Maps JSON data to Employee, Technician or Supervisor instance depending on
 * property role
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "role", visible = true)
@JsonSubTypes({ @Type(value = Admin.class, name = User.ADMIN), @Type(value = Client.class, name = User.CLIENT) })
/* Lombok */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String ROLE_PREFIX = "ROLE_";
	public static final String ADMIN = "ADMIN";
	public static final String CLIENT = "CLIENT";
	public static final String ROLE_ADMIN = ROLE_PREFIX + ADMIN;
	public static final String ROLE_CLIENT = ROLE_PREFIX + CLIENT;

	public enum Role {
		ADMIN, CLIENT
	}

	public static final int MIN_USERNAME = 2;
	public static final int MAX_USERNAME = 25;
	public static final int MIN_PASSWORD = 10;
	public static final int MIN_FULL_NAME = 3;
	public static final int MAX_FULL_NAME = 100;

	/* Validation */
	@NotBlank
	@Size(min = MIN_USERNAME, max = MAX_USERNAME)
	/* JPA */
	/* Lombok */
	@NonNull
	@EqualsAndHashCode.Include
	protected String username;

	/* JSON */
	@JsonInclude(Include.NON_NULL)
	/* Validation */
	@NotBlank
	@Size(min = MIN_PASSWORD)
	/* Lombok */
	@NonNull
	protected String password;

	/* Validation */
	@NotNull
	protected Role role;

	@Override /* Spring Security getter */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority(ROLE_PREFIX + getRole()));
		return list;
	}

	@Override /* Spring Security getter */
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override /* Spring Security getter */
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override /* Spring Security getter */
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override /* Spring Security getter */
	public boolean isEnabled() {
		return true;
	}

}
