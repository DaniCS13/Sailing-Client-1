package cat.institutmarianao.sailing.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/* Lombok */
@Data
@EqualsAndHashCode(callSuper = true)
public class Cancellation extends Action {
	private static final long serialVersionUID = 1L;

	public Cancellation() {
		super(Type.CANCELLATION);
	}

}
