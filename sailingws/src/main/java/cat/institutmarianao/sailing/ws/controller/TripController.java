package cat.institutmarianao.sailing.ws.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.BookedPlace;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.dto.ActionDto;
import cat.institutmarianao.sailing.ws.model.dto.BookedPlaceDto;
import cat.institutmarianao.sailing.ws.model.dto.TripDto;
import cat.institutmarianao.sailing.ws.service.ActionService;
import cat.institutmarianao.sailing.ws.service.BookedPlaceService;
import cat.institutmarianao.sailing.ws.service.TripService;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Tag(name = "Trip", description = "Trip API")

@RestController
@RequestMapping("/trips")
@SecurityRequirement(name = "Bearer Authentication")
@Validated
public class TripController {
	@Autowired
	private ConversionService conversionService;

	@Autowired
	private TripService tripService;

	@Autowired
	private ActionService actionService;

	@Autowired
	private BookedPlaceService bookedPlaceService;

	@Operation(summary = "Retrieve all reserved trips (status is RESERVED)", description = "Retrieve all reserved trips from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TripDto.class))) }, description = "Trips retrieved ok")
	@GetMapping(value = "/find/all")
	public @ResponseBody List<TripDto> findAll() {

		List<Trip> actions = tripService.findAll();

		List<TripDto> tripsDto = new ArrayList<>(actions.size());
		for (Trip action : actions) {
			TripDto tripDto = conversionService.convert(action, TripDto.class);
			tripsDto.add(tripDto);
		}
		return tripsDto;
	}

	@Operation(summary = "Retrieve all trips by username", description = "Retrieve all trips by username from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TripDto.class))) }, description = "Trips retrieved ok")
	@GetMapping(value = "/find/all/by/client/username/{username}")
	public @ResponseBody List<TripDto> findAllByClientUsername(@PathVariable("username") String username) {

		List<Trip> trips = tripService.findAllByClientUsername(username);

		List<TripDto> tripsDto = new ArrayList<>(trips.size());
		for (Trip trip : trips) {
			TripDto tripDto = conversionService.convert(trip, TripDto.class);
			tripsDto.add(tripDto);
		}

		return tripsDto;
	}

	@Operation(summary = "Save a trip", description = "Saves a trip in the database. The response is the stored trip from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = TripDto.class)) }, description = "Trip saved ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error saving the trip. See response body for more details")
	@PostMapping(value = "/save")
	public TripDto save(@RequestBody @Validated(OnTripCreate.class) @NotNull TripDto tripDto) {
		return conversionService.convert(tripService.save(conversionService.convert(tripDto, Trip.class)),
				TripDto.class);
	}

	/* Swagger */
	@Operation(summary = "Save an action of a shipment (in its tracking)")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ActionDto.class)) }, description = "Action saved ok")
	/**/
	@PostMapping("/save/action")
	public ActionDto saveAction(@RequestBody @Validated(OnActionCreate.class) ActionDto actionDto) {
		Action action = conversionService.convert(actionDto, Action.class);
		Action savedAction = actionService.save(action);
		return conversionService.convert(savedAction, ActionDto.class);
	}

	@Operation(summary = "Get booked places", description = "Gets all booked places that a trip has")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = TripDto.class)) }, description = "Booked places retrieved ok")
	@GetMapping(value = "/bookedPlaces/{trip_type_id}/{date}")
	public List<BookedPlaceDto> bookedPlaces(@PathVariable("trip_type_id") @NotNull Long tripTypeId,
			@PathVariable("date") @NotNull @DateTimeFormat(pattern = SailingWsApplication.DATE_PATTERN) @Parameter(description = SailingWsApplication.DATE_PATTERN) Date date) {

		List<BookedPlace> bookedPlaces = bookedPlaceService.findByIdTripTypeIdAndIdDate(tripTypeId, date);

		List<BookedPlaceDto> bookedPlaceDtos = new ArrayList<>(bookedPlaces.size());
		for (BookedPlace bookedPlace : bookedPlaces) {
			BookedPlaceDto bookedPlaceDto = conversionService.convert(bookedPlace, BookedPlaceDto.class);
			bookedPlaceDtos.add(bookedPlaceDto);
		}

		return bookedPlaceDtos;
	}

	@Operation(summary = "Find tracking by trip id", description = "Gets the tracking of a trip by its id")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ActionDto.class)) }, description = "Tracking retrieved ok")

	@GetMapping("/find/tracking/by/id/{tripId}")
	public Iterable<ActionDto> findTrackingByTripId(@PathVariable("tripId") @Positive Long tripId) {
		return actionService.findByTripId(tripId).stream().map(a -> conversionService.convert(a, ActionDto.class))
				.toList();
	}

}
