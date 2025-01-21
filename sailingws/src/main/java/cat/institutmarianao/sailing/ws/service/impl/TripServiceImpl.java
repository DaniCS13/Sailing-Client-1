package cat.institutmarianao.sailing.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.repository.TripRepository;
import cat.institutmarianao.sailing.ws.service.TripService;
import jakarta.validation.constraints.Positive;

@Service
@Validated
public class TripServiceImpl implements TripService {
	@Autowired
	private TripRepository tripRepository;

	@Override
	public Trip getById(@Positive Long id) {
		return tripRepository.findById(id).orElseThrow(NotFoundException::new);
	}

	@Override
	public List<Trip> findAll() {
		return tripRepository.findAll();
	}

	@Override
	public List<Trip> findAllByClientUsername(String username) {
		return tripRepository.findAllByClientUsernameOrderByDateAscDepartureAsc(username);
	}

	@Override
	public List<Trip> findAllByStatus(Status status) {
		return tripRepository.findAllByStatusOrderByDateAscDepartureAsc(status);
	}

	@Override
	public Trip save(Trip trip) {
		Trip ret = tripRepository.saveAndFlush(trip);
		return ret;
	}

}
