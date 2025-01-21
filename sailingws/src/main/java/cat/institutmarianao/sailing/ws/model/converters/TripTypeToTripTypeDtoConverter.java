package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.dto.TripTypeDto;

@Component
public class TripTypeToTripTypeDtoConverter implements Converter<TripType, TripTypeDto> {

	@Override
	public TripTypeDto convert(TripType tripType) {
		if (tripType == null) {
			return null;
		}
		TripTypeDto tripTypeDto = new TripTypeDto();
		BeanUtils.copyProperties(tripType, tripTypeDto);
		return tripTypeDto;
	}
}
