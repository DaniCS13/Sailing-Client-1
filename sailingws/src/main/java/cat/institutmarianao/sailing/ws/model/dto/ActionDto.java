package cat.institutmarianao.sailing.ws.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionUpdate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/* JSON annotations */
/*
 * Maps JSON data to OpeningDto, AssignmentDto, InterventionDto or CloseDto instance
 * depending on property type
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({ @Type(value = BookingDto.class, name = Action.BOOKING),
		@Type(value = ReschedulingDto.class, name = Action.RESCHEDULING),
		@Type(value = CancellationDto.class, name = Action.CANCELLATION),
		@Type(value = DoneDto.class, name = Action.DONE) })
/* Validation */
/* Lombok */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class ActionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Validation */
	@Null(groups = { OnTripCreate.class, OnActionCreate.class }) // Must be null on inserts
	@NotNull(groups = OnActionUpdate.class) // Must be not null on updates
	/* Lombok */
	@EqualsAndHashCode.Include
	protected Long id;

	/* Validation */
	@NotNull
	/* Lombok */
	@NonNull
	protected Action.Type type;

	/* Validation */
	@NotBlank
	protected String performer;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SailingWsApplication.DATE_TIME_PATTERN)
	protected Date date = new Date();

	/* Validation */
	@Null(groups = OnTripCreate.class) // The JSON do not have the trip reference (trip has no id yet)
	@NotNull(groups = { OnActionCreate.class })
	/* JSON */
	protected Long tripId;

	private String comments;
}
