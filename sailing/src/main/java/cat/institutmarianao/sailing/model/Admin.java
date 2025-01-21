package cat.institutmarianao.sailing.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends User implements Serializable {

	private static final long serialVersionUID = 1L;

	public Admin(String username, String password) {
		super(username, password);
		role = Role.ADMIN;
	}
}
