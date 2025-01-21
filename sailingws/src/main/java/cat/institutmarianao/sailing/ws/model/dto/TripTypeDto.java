package cat.institutmarianao.sailing.ws.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripTypeCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripTypeUpdate;
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
public class TripTypeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Validation */
	@Null(groups = OnTripTypeCreate.class) // Must be null on inserts
	@NotNull(groups = OnTripTypeUpdate.class) // Must be not null on updates
	/* Lombok */
	@EqualsAndHashCode.Include
	private Long id;

	/* Validation */
	@NotNull
	private String title;

	/* Validation */
	@NotNull
	private TripType.Category category;

	/* Validation */
	@NotNull
	private String description;

	/* Validation */
	@Positive
	private double price;

	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.TIME_PATTERN)
	private List<Date> departures;

	/* Validation */
	@Positive
	private int duration;

	/* Validation */
	@Positive
	private int maxPlaces;
}
