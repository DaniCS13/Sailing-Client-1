package cat.institutmarianao.sailing.ws.model.dto;

import cat.institutmarianao.sailing.ws.model.Action.Type;
import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.validation.constraints.Performer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/* Validation */
@Performer({ Role.CLIENT, Role.ADMIN })
/* Lombok */
@Data
@EqualsAndHashCode(callSuper = true)
public class CancellationDto extends ActionDto {
	private static final long serialVersionUID = 1L;

	public CancellationDto() {
		super(Type.CANCELLATION);
	}
}
