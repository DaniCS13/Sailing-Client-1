package cat.institutmarianao.sailing.ws.security.model;

import lombok.Data;

@Data
public class AuthCredentials {
	private String username;
	private String password;
}
