package cat.institutmarianao.sailing.ws.model;

import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.validation.constraints.Performer;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/* Validation */
@Performer({ Role.CLIENT, Role.ADMIN })
/* JPA annotations */
@Entity
@DiscriminatorValue(Action.CANCELLATION)
/* Lombok */
@Data
@EqualsAndHashCode(callSuper = true)
public class Cancellation extends Action {
	private static final long serialVersionUID = 1L;

	public Cancellation() {
		super(Type.CANCELLATION);
	}
}
