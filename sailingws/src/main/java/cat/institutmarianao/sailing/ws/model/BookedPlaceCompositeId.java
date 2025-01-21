package cat.institutmarianao.sailing.ws.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/*JPA*/
@Embeddable
/* Lombok */
@Data
@NoArgsConstructor
public class BookedPlaceCompositeId {
	/* Validation */
//	@NotNull
//	@ManyToOne(fetch = FetchType.EAGER)
//	private TripType tripType;

	/* Validation */
	@NotNull
	/* JPA */
	@Column(name = "trip_type_id")
	private Long tripTypeId;

	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.DATE)
	private Date date;

	/* JPA */
	@Temporal(TemporalType.TIME)
	private Date departure;
}
