package cat.institutmarianao.sailing.ws.model.converters;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.dto.TripTypeDto;

@Component
public class TripTypeDtoToTripTypeConverter implements Converter<TripTypeDto, TripType> {

	@Override
	public TripType convert(TripTypeDto tripTypeDto) {
		if (tripTypeDto == null) {
			return null;
		}

		TripType tripType = new TripType();
		BeanUtils.copyProperties(tripTypeDto, tripType);

		return tripType;
	}
}
