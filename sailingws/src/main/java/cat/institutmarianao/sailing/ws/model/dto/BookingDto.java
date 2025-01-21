package cat.institutmarianao.sailing.ws.model.dto;

import cat.institutmarianao.sailing.ws.model.Action.Type;
import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.validation.constraints.Performer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/* Validation */
@Performer({ Role.CLIENT })
/* Lombok */
@Data
@EqualsAndHashCode(callSuper = true)
public class BookingDto extends ActionDto {
	private static final long serialVersionUID = 1L;

	public BookingDto() {
		super(Type.BOOKING);
	}
}
