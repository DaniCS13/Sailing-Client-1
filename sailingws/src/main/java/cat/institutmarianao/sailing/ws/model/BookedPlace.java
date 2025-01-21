package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* JPA annotations */
@Entity
@Immutable
@Table(name = "booked_places")
/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookedPlace implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
//	@AssociationOverride(name = "id.tripType", joinColumns = @JoinColumn(name = "trip_type"))
	@AssociationOverride(name = "id.tripTypeId", joinColumns = @JoinColumn(name = "trip_type_id"))
	@AttributeOverride(name = "id.date", column = @Column(name = "date"))
	@AttributeOverride(name = "id.departure", column = @Column(name = "departure"))
	private BookedPlaceCompositeId id;

	@Column(name = "booked_places")
	private long bookedPlaces;
}
