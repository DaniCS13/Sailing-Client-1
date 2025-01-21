package cat.institutmarianao.sailing.ws.model.dto;

import java.io.Serializable;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookedPlaceDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Validation */
	@NotNull
	private Long tripTypeId;

	/* Validation */
	@NotNull
	private Date date;

	private Date departure;

	private long bookedPlaces;
}
