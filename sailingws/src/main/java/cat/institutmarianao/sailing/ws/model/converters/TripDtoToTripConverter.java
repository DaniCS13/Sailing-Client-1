package cat.institutmarianao.sailing.ws.model.converters;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Booking;
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.dto.TripDto;
import cat.institutmarianao.sailing.ws.service.ActionService;
import cat.institutmarianao.sailing.ws.service.TripTypeService;
import cat.institutmarianao.sailing.ws.service.UserService;

@Component
public class TripDtoToTripConverter implements Converter<TripDto, Trip> {

	@Autowired
	private ActionService actionService;

	@Autowired
	private TripTypeService tripTypeService;

	@Autowired
	private UserService userService;

	@Override
	public Trip convert(TripDto tripDto) {
		if (tripDto == null) {
			return null;
		}

		Trip trip = new Trip();
		copyCommonProperties(tripDto, trip);

		List<Action> tracking = null;
		if (trip.getId() != null) {
			tracking = actionService.findByTripId(trip.getId());
		}
		if (tracking != null) {
			trip.setTracking(tracking);
		} else {
			Booking booking = new Booking();
			booking.setTrip(trip);
			booking.setPerformer(trip.getClient());
			trip.setTracking(Arrays.asList(booking));
		}

		return trip;
	}

	private void copyCommonProperties(TripDto tripDto, Trip trip) {
		BeanUtils.copyProperties(tripDto, trip);
		User user = userService.getByUsername(tripDto.getClientUsername());
		if (user instanceof Client client) {
			trip.setClient(client);
		}
		TripType tripType = tripTypeService.getById(tripDto.getTypeId());
		trip.setType(tripType);
	}
}
