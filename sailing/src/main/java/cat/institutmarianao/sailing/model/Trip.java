package cat.institutmarianao.sailing.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.SailingApplication;
import cat.institutmarianao.sailing.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreateDate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreateDeparture;
import cat.institutmarianao.sailing.validation.groups.OnTripUpdate;
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
public class Trip implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_DESCRIPTION = 500;

	public static final String RESERVED = "RESERVED";
	public static final String RESCHEDULED = "RESCHEDULED";
	public static final String CANCELLED = "CANCELLED";
	public static final String DONE = "DONE";

	public enum Status {
		RESERVED, RESCHEDULED, CANCELLED, DONE
	}

	/* Validation */
	@Null(groups = { OnTripCreateDate.class, OnTripCreateDeparture.class, OnTripCreate.class }) // Must be null on
																								// inserts
	@NotNull(groups = OnTripUpdate.class) // Must be not null on updates
	/* Lombok */
	@EqualsAndHashCode.Include
	private Long id;

	/* Validation */
	@NotNull
	private Long typeId;

	/* Validation */
	@NotBlank
	private String clientUsername;

	@Positive(groups = { OnTripCreate.class, OnTripUpdate.class })
	private int places = 2;

	/* JSON */
	private Trip.Status status;

	/* Validation */
	@NotNull(groups = { OnTripCreateDate.class, OnTripUpdate.class })
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingApplication.DATE_PATTERN)
	/* Spring */
	@DateTimeFormat(pattern = SailingApplication.DATE_PATTERN)
	private Date date;

	/* Validation */
	@NotNull(groups = { OnTripCreateDeparture.class, OnTripUpdate.class })
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingApplication.TIME_PATTERN)
	/* Spring */
	@DateTimeFormat(pattern = SailingApplication.TIME_PATTERN)
	private Date departure;
}
