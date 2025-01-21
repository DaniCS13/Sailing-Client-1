package cat.institutmarianao.sailing.ws.service;

import java.util.List;

import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import jakarta.validation.constraints.Positive;

public interface TripService {

	Trip getById(@Positive Long id);

	List<Trip> findAll();

	List<Trip> findAllByClientUsername(String username);

	List<Trip> findAllByStatus(Status status);

	Trip save(Trip trip);

}
