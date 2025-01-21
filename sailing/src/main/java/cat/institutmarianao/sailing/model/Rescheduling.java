package cat.institutmarianao.sailing.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import cat.institutmarianao.sailing.SailingApplication;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/* Lombok */
@Data
@EqualsAndHashCode(callSuper = true)
public class Rescheduling extends Action {
	private static final long serialVersionUID = 1L;

	/* Validation */
	@NotNull
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingApplication.DATE_PATTERN)
	/* Spring */
	@DateTimeFormat(pattern = SailingApplication.DATE_PATTERN)
	private Date newDate;

	/* Validation */
	@NotNull
	/* JSON */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingApplication.TIME_PATTERN)
	/* Spring */
	@DateTimeFormat(pattern = SailingApplication.TIME_PATTERN)
	private Date newDeparture;

	public Rescheduling() {
		super(Type.RESCHEDULING);
	}
}
