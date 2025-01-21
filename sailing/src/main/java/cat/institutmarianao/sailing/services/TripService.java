package cat.institutmarianao.sailing.services;

import java.util.Date;
import java.util.List;

import cat.institutmarianao.sailing.model.Action;
import cat.institutmarianao.sailing.model.BookedPlace;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import jakarta.validation.constraints.NotNull;

public interface TripService {
	List<Trip> findAll();

	List<Trip> findAllByClientUsername(String username);

	Trip save(Trip trip);

	List<BookedPlace> findBookedPlacesByTripIdAndDate(@NotNull Long id, @NotNull Date date);

	List<TripType> getAllTripTypes();

	List<TripType> getAllGroupTripTypes();

	List<TripType> getAllPrivateTripTypes();

	TripType getTripTypeById(Long id);

	List<Action> findTrackingById(Long id);

	Action track(Action action);

}
