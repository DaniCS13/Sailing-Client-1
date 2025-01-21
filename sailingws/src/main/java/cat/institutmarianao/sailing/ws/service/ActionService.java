package cat.institutmarianao.sailing.ws.service;

import java.util.List;

import cat.institutmarianao.sailing.ws.model.Action;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public interface ActionService {

	/**
	 * Tracking
	 */
	List<Action> findByTripId(@Positive Long tripId);

	Action save(@NotNull @Valid Action action);

}
