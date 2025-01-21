package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Booking;
import cat.institutmarianao.sailing.ws.model.Cancellation;
import cat.institutmarianao.sailing.ws.model.Done;
import cat.institutmarianao.sailing.ws.model.Rescheduling;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.dto.ActionDto;
import cat.institutmarianao.sailing.ws.model.dto.BookingDto;
import cat.institutmarianao.sailing.ws.model.dto.CancellationDto;
import cat.institutmarianao.sailing.ws.model.dto.DoneDto;
import cat.institutmarianao.sailing.ws.model.dto.ReschedulingDto;
import cat.institutmarianao.sailing.ws.service.TripService;
import cat.institutmarianao.sailing.ws.service.UserService;

@Component
public class ActionDtoToActionConverter implements Converter<ActionDto, Action> {

	@Autowired
	private TripService tripService;

	@Autowired
	private UserService userService;

	@Override
	public Action convert(ActionDto actionDto) {
		if (actionDto == null) {
			return null;
		}

		if (actionDto instanceof BookingDto bookingDto) {
			Booking reception = new Booking();
			copyCommonProperties(bookingDto, reception);
			return reception;
		}
		if (actionDto instanceof CancellationDto cancellationDto) {
			Cancellation cancellation = new Cancellation();
			copyCommonProperties(cancellationDto, cancellation);
			return cancellation;
		}
		if (actionDto instanceof DoneDto doneDto) {
			Done delivery = new Done();
			copyCommonProperties(doneDto, delivery);
			return delivery;
		}
		if (actionDto instanceof ReschedulingDto reschedulingDto) {
			Rescheduling delivery = new Rescheduling();
			copyCommonProperties(reschedulingDto, delivery);
			return delivery;
		}
		return null;
	}

	private void copyCommonProperties(ActionDto actionDto, Action action) {
		BeanUtils.copyProperties(actionDto, action);
		action.setDate(actionDto.getDate());
		User performer = userService.getByUsername(actionDto.getPerformer());
		action.setPerformer(performer);
		Trip trip = tripService.getById(actionDto.getTripId());
		action.setTrip(trip);
	}
}
