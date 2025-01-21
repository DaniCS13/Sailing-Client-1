package cat.institutmarianao.sailing.ws.model;

import java.util.Date;

import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.validation.constraints.Performer;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Validation */
@Performer({ Role.ADMIN })
/* JPA annotations */
@Entity
@DiscriminatorValue(Action.RESCHEDULING)
/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Rescheduling extends Action {
	private static final long serialVersionUID = 1L;

	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.DATE)
	private Date newDate;

	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.TIME)
	private Date newDeparture;
}
