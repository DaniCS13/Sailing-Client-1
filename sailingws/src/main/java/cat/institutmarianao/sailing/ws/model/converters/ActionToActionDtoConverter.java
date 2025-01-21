package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Booking;
import cat.institutmarianao.sailing.ws.model.Cancellation;
import cat.institutmarianao.sailing.ws.model.Done;
import cat.institutmarianao.sailing.ws.model.Rescheduling;
import cat.institutmarianao.sailing.ws.model.dto.ActionDto;
import cat.institutmarianao.sailing.ws.model.dto.BookingDto;
import cat.institutmarianao.sailing.ws.model.dto.CancellationDto;
import cat.institutmarianao.sailing.ws.model.dto.DoneDto;
import cat.institutmarianao.sailing.ws.model.dto.ReschedulingDto;

@Component
public class ActionToActionDtoConverter implements Converter<Action, ActionDto> {

	@Override
	public ActionDto convert(Action actionDto) {
		if (actionDto == null) {
			return null;
		}

		if (actionDto instanceof Booking booking) {
			BookingDto bookingDto = new BookingDto();
			copyCommonProperties(booking, bookingDto);
			return bookingDto;
		}
		if (actionDto instanceof Cancellation cancellation) {
			CancellationDto cancellationDto = new CancellationDto();
			copyCommonProperties(cancellation, cancellationDto);
			return cancellationDto;
		}
		if (actionDto instanceof Done done) {
			DoneDto doneDto = new DoneDto();
			copyCommonProperties(done, doneDto);
			return doneDto;
		}
		if (actionDto instanceof Rescheduling rescheduling) {
			ReschedulingDto reschedulingDto = new ReschedulingDto();
			copyCommonProperties(rescheduling, reschedulingDto);
			return reschedulingDto;
		}
		return null;
	}

	private void copyCommonProperties(Action action, ActionDto actionDto) {
		BeanUtils.copyProperties(action, actionDto);
		actionDto.setPerformer(action.getPerformer().getUsername());
		if (action.getTrip() != null) {
			actionDto.setTripId(action.getTrip().getId());
		}
	}
}
