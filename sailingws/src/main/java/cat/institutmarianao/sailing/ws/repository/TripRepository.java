package cat.institutmarianao.sailing.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;

public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {

	List<Trip> findAllByClientUsernameOrderByDateAscDepartureAsc(String username);

	List<Trip> findAllByStatusOrderByDateAscDepartureAsc(Status status);
}
