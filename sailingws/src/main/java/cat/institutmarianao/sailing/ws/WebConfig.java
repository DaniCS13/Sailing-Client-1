package cat.institutmarianao.sailing.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cat.institutmarianao.sailing.ws.model.converters.ActionDtoToActionConverter;
import cat.institutmarianao.sailing.ws.model.converters.ActionToActionDtoConverter;
import cat.institutmarianao.sailing.ws.model.converters.TripDtoToTripConverter;
import cat.institutmarianao.sailing.ws.model.converters.TripToTripDtoConverter;
import cat.institutmarianao.sailing.ws.model.converters.TripTypeDtoToTripTypeConverter;
import cat.institutmarianao.sailing.ws.model.converters.TripTypeToTripTypeDtoConverter;
import cat.institutmarianao.sailing.ws.model.converters.UserDtoToUserConverter;
import cat.institutmarianao.sailing.ws.model.converters.UserToUserDtoConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	private ActionDtoToActionConverter actionDtoToActionConverter;

	@Autowired
	private ActionToActionDtoConverter actionToActionDtoConverter;

	@Autowired
	private TripDtoToTripConverter tripDtoToTripConverter;

	@Autowired
	private TripToTripDtoConverter tripToTripDtoConverter;

	@Autowired
	private TripTypeDtoToTripTypeConverter tripTypeDtoToTripTypeConverter;

	@Autowired
	private TripTypeToTripTypeDtoConverter tripTypeToTripTypeDtoConverter;

	@Autowired
	private UserToUserDtoConverter userToUserDtoConverter;

	@Autowired
	private UserDtoToUserConverter userDtoToUserConverter;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(actionDtoToActionConverter);
		registry.addConverter(actionToActionDtoConverter);
		registry.addConverter(tripDtoToTripConverter);
		registry.addConverter(tripToTripDtoConverter);
		registry.addConverter(tripTypeDtoToTripTypeConverter);
		registry.addConverter(tripTypeToTripTypeDtoConverter);
		registry.addConverter(userToUserDtoConverter);
		registry.addConverter(userDtoToUserConverter);
	}
}