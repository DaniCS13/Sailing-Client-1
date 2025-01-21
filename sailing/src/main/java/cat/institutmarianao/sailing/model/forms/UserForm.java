package cat.institutmarianao.sailing.model.forms;

import cat.institutmarianao.sailing.model.Admin;
import cat.institutmarianao.sailing.model.Client;
import cat.institutmarianao.sailing.model.User;
import cat.institutmarianao.sailing.model.User.Role;
import cat.institutmarianao.sailing.validation.groups.OnUserCreate;
import cat.institutmarianao.sailing.validation.groups.OnUserUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserForm {

	@NotBlank(groups = { OnUserCreate.class, OnUserUpdate.class })
	@Size(min = User.MIN_USERNAME, max = User.MAX_USERNAME, groups = { OnUserCreate.class, OnUserUpdate.class })
	protected String username;
	@NotNull(groups = { OnUserCreate.class, OnUserUpdate.class })
	protected Role role;

	@NotBlank(groups = OnUserCreate.class)
	@Size(min = User.MIN_PASSWORD, groups = OnUserCreate.class)
	protected String password;

	@NotBlank(groups = OnUserCreate.class)
	@Size(min = User.MIN_PASSWORD, groups = OnUserCreate.class)
	protected String verify;

	protected String fullName;

	protected String phone;

	public UserForm(User user) {
		username = user.getUsername();
		role = user.getRole();
		password = null;

		if (user instanceof Client client) {
			fullName = client.getFullName();
			phone = client.getPhone();
		}
	}

	public User getUser() {
		return switch (role) {
		case ADMIN -> new Admin(username, password);
		case CLIENT -> new Client(username, password, fullName, phone);
		};
	}
}
