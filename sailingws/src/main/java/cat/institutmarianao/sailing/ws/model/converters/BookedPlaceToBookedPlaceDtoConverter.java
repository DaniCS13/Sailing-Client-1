package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.BookedPlace;
import cat.institutmarianao.sailing.ws.model.dto.BookedPlaceDto;

@Component
public class BookedPlaceToBookedPlaceDtoConverter implements Converter<BookedPlace, BookedPlaceDto> {

	@Override
	public BookedPlaceDto convert(BookedPlace bookedPlace) {
		if (bookedPlace == null) {
			return null;
		}
		BookedPlaceDto bookedPlaceDto = new BookedPlaceDto();
		bookedPlaceDto.setTripTypeId(bookedPlace.getId().getTripTypeId());
		bookedPlaceDto.setDate(bookedPlace.getId().getDate());
		bookedPlaceDto.setDeparture(bookedPlace.getId().getDeparture());
		bookedPlaceDto.setBookedPlaces(bookedPlace.getBookedPlaces());
		return bookedPlaceDto;
	}

}
