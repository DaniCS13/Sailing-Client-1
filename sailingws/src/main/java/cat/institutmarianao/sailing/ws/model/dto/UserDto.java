package cat.institutmarianao.sailing.ws.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserCreate;
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
@JsonSubTypes({ @Type(value = AdminDto.class, name = User.ADMIN), @Type(value = ClientDto.class, name = User.CLIENT) })

/* Lombok */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class UserDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/* Validation */
	@NotBlank
	@Size(min = User.MIN_USERNAME, max = User.MAX_USERNAME)
	/* Lombok */
	@NonNull
	@EqualsAndHashCode.Include
	protected String username;

	/* Validation */
	@NotBlank(groups = OnUserCreate.class)
	@Size(min = User.MIN_PASSWORD)
	/* Lombok */
	@NonNull
	protected String password;

	/* Validation */
	@NotNull
	protected User.Role role;
}
