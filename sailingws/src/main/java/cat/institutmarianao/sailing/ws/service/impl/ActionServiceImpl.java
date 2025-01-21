package cat.institutmarianao.sailing.ws.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.repository.ActionRepository;
import cat.institutmarianao.sailing.ws.service.ActionService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@Service
public class ActionServiceImpl implements ActionService {

	@Autowired
	private Validator validator;

	@Autowired
	private ActionRepository actionRepository;

	/**
	 * Tracking
	 */
	@Override
	public List<Action> findByTripId(@Positive Long tripId) {
		return actionRepository.findByTripIdOrderByDateDesc(tripId);
	}

	@Override
	public Action save(@NotNull @Valid Action action) {
		Long tripId = action.getTrip().getId();

		List<Action> tracking = actionRepository.findByTripIdOrderByDateDesc(tripId);
		tracking.add(0, action);
		Trip trip = new Trip();
		trip.setTracking(tracking);

		Set<ConstraintViolation<Trip>> errors = validator.validateProperty(trip, "tracking");

		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
		return actionRepository.save(action);
	}

}
