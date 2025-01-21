package cat.institutmarianao.sailing.ws.model;

import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.validation.constraints.Performer;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/* Validation */
@Performer({ Role.CLIENT })
/* JPA annotations */
@Entity
@DiscriminatorValue(Action.BOOKING)
/* Lombok */
@Data
@EqualsAndHashCode(callSuper = true)
public class Booking extends Action {
	private static final long serialVersionUID = 1L;

	public Booking() {
		super(Type.BOOKING);
	}
}
