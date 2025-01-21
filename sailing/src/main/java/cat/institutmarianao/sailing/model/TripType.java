package cat.institutmarianao.sailing.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.SailingApplication;
import cat.institutmarianao.sailing.validation.groups.OnTripTypeCreate;
import cat.institutmarianao.sailing.validation.groups.OnTripTypeUpdate;
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
public class TripType implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String GROUP = "GROUP";
	public static final String PRIVATE = "PRIVATE";

	public enum Category {
		GROUP, PRIVATE
	}

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
	private Category category;

	/* Validation */
	@NotNull
	private String description;

	/* Validation */
	@Positive
	private double price;

	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = SailingApplication.TIME_PATTERN, timezone = "Europe/Madrid")
	/* Spring */
	@DateTimeFormat(pattern = SailingApplication.TIME_PATTERN)
	private List<Date> departures;

	/* Validation */
	@Positive
	private int duration;

	/* Validation */
	@Positive
	private long maxPlaces;
}
