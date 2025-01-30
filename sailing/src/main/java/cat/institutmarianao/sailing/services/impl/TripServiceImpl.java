package cat.institutmarianao.sailing.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import cat.institutmarianao.sailing.model.Action;
import cat.institutmarianao.sailing.model.BookedPlace;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;
import jakarta.validation.constraints.NotNull;


@Service
@PropertySource("classpath:application.properties")
public class TripServiceImpl implements TripService{

	@Override
	public List<Trip> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Trip> findAllByClientUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trip save(Trip trip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookedPlace> findBookedPlacesByTripIdAndDate(@NotNull Long id, @NotNull Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TripType> getAllTripTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TripType> getAllGroupTripTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TripType> getAllPrivateTripTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TripType getTripTypeById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Action> findTrackingById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action track(Action action) {
		// TODO Auto-generated method stub
		return null;
	}

}
