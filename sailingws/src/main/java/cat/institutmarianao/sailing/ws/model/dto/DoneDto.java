package cat.institutmarianao.sailing.ws.model.dto;

import cat.institutmarianao.sailing.ws.model.Action.Type;
import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.validation.constraints.Performer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/* Validation */
@Performer({ Role.ADMIN })
/* Lombok */
@Data
@EqualsAndHashCode(callSuper = true)
public class DoneDto extends ActionDto {
	private static final long serialVersionUID = 1L;

	public DoneDto() {
		super(Type.DONE);
	}
}
