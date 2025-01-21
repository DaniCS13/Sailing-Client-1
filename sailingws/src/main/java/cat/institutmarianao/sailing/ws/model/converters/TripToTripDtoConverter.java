package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.dto.TripDto;

@Component
public class TripToTripDtoConverter implements Converter<Trip, TripDto> {

	@Override
	public TripDto convert(Trip trip) {
		if (trip == null) {
			return null;
		}
		TripDto tripDto = new TripDto();
		copyCommonProperties(trip, tripDto);
		return tripDto;
	}

	private void copyCommonProperties(Trip trip, TripDto tripDto) {
		BeanUtils.copyProperties(trip, tripDto);
		tripDto.setClientUsername(trip.getClient().getUsername());
		tripDto.setTypeId(trip.getType().getId());
	}
}
