package cat.institutmarianao.sailing.ws.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TripDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Validation */
	@Null(groups = OnTripCreate.class) // Must be null on inserts
	@NotNull(groups = OnTripUpdate.class) // Must be not null on updates
	/* Lombok */
	@EqualsAndHashCode.Include
	/* JSON */
	private Long id;

	/* JPA */
	@NotNull
	private Long typeId;

	/* Validation */
	@NotBlank(groups = OnTripCreate.class)
	private String clientUsername;

	@Positive
	private int places;

	/* Lombok */
	private Trip.Status status;

	/* Validation */
	@NotNull
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.DATE_PATTERN)
	private Date date;

	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.TIME_PATTERN)
	private Date departure;
}
