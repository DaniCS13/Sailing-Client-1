package cat.institutmarianao.sailing.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.SailingApplication;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookedPlace implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long tripTypeId;

	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingApplication.DATE_PATTERN)
	/* Spring */
	@DateTimeFormat(pattern = SailingApplication.DATE_PATTERN)
	private Date date;

	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingApplication.TIME_PATTERN)
	/* Spring */
	@DateTimeFormat(pattern = SailingApplication.TIME_PATTERN)
	private Date departure;

	private long bookedPlaces;
}
