package cat.institutmarianao.sailing.ws.model.dto;

import java.io.Serializable;

import cat.institutmarianao.sailing.ws.model.User.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminDto extends UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public AdminDto(String username, String password) {
		super(username, password);
		role = Role.ADMIN;
	}
}
