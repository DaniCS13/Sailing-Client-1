package cat.institutmarianao.sailing.services.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cat.institutmarianao.sailing.model.Action;
import cat.institutmarianao.sailing.model.BookedPlace;
import cat.institutmarianao.sailing.model.Done;
import cat.institutmarianao.sailing.model.Rescheduling;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;

@Service
@PropertySource("classpath:application.properties")
public class TripServiceImpl implements TripService {

	private static final String TRIPS_SERVICE = "/trips";
	private static final String TRIPS_FIND_ALL = TRIPS_SERVICE + "/find/all";
	private static final String TRIPS_FIND_BY_USERNAME = TRIPS_FIND_ALL + "/by/client/username/";
	private static final String TRIPS_BOOKED_PLACES = TRIPS_FIND_ALL + "/bookedPlaces/";
	private static final String TRIP_TYPES_ALL = "/triptypes";
	private static final String TRIP_TYPES_FIND_ALL = TRIP_TYPES_ALL + "/find/all";
	private static final String TRIP_TYPES_GROUP = TRIP_TYPES_ALL + "/find/all/group";
	private static final String TRIP_TYPES_PRIVATE = TRIP_TYPES_ALL + "/find/all/private";
	private static final String TRIP_TYPE_BY_ID = TRIP_TYPES_ALL + "/get/by/id/";

	@Value("${webService.host}")
	private String webServiceHost;

	@Value("${webService.port}")
	private String webServicePort;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Trip> findAll() {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_FIND_ALL;
		ResponseEntity<Trip[]> response = restTemplate.getForEntity(uri, Trip[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<Trip> findAllByClientUsername(String username) {
		final String baseUri = webServiceHost + ":" + webServicePort + TRIPS_FIND_BY_USERNAME + username;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("username", username);

		ResponseEntity<Trip[]> response = restTemplate
				.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), Trip[].class);
		return Arrays.asList(response.getBody());

	}

	/*
	 * @Override public Trip save(Trip trip) { final String uri = webServiceHost +
	 * ":" + webServicePort + TRIPS_SAVE; HttpHeaders headers = new HttpHeaders();
	 * headers.setContentType(MediaType.APPLICATION_JSON); HttpEntity<Trip> request
	 * = new HttpEntity<>(trip, headers); return restTemplate.postForObject(uri,
	 * request, Trip.class); }
	 */
	@Override
	public List<BookedPlace> findBookedPlacesByTripIdAndDate(Long id, Date date) {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_BOOKED_PLACES + id + "/" + date;
		ResponseEntity<BookedPlace[]> response = restTemplate.getForEntity(uri, BookedPlace[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIP_TYPES_FIND_ALL;
		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllGroupTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIP_TYPES_GROUP;
		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllPrivateTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIP_TYPES_PRIVATE;
		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public TripType getTripTypeById(Long id) {
		final String uri = webServiceHost + ":" + webServicePort + TRIP_TYPE_BY_ID + id;
		return restTemplate.getForObject(uri, TripType.class, id);
	}
	/*
	 * @Override public List<Action> findTrackingById(Long id) { final String uri =
	 * webServiceHost + ":" + webServicePort + TRACKING_BY_ID;
	 * ResponseEntity<Action[]> response = restTemplate.getForEntity(uri,
	 * Action[].class, id); return Arrays.asList(response.getBody()); }
	 * 
	 * @Override public Action track(Action action) { final String uri =
	 * webServiceHost + ":" + webServicePort + TRACK_ACTION; HttpHeaders headers =
	 * new HttpHeaders(); headers.setContentType(MediaType.APPLICATION_JSON);
	 * HttpEntity<Action> request = new HttpEntity<>(action, headers); return
	 * restTemplate.postForObject(uri, request, Action.class); }
	 */

	@Override
	public Trip save(Trip trip) {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_SERVICE + "/save";
		return restTemplate.postForObject(uri, trip, Trip.class);
	}

	@Override
	public List<Action> findTrackingById(Long id) {
		final String uri = webServiceHost + ":" + webServicePort + "/tracking/" + id;
		ResponseEntity<Action[]> response = restTemplate.getForEntity(uri, Action[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public Action track(Action action) {
		final String uri = webServiceHost + ":" + webServicePort + "/tracking";
		return restTemplate.postForObject(uri, action, Action.class);
	}

	@Override
	public void cancelTrip(Long tripId) {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_SERVICE + "/cancel/" + tripId;
		restTemplate.postForObject(uri, null, Void.class);
	}

	@Override
	public void markTripAsDone(Done done) {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_SERVICE + "/done";
		restTemplate.postForObject(uri, null, Void.class);
	}

	@Override
	public void rescheduleTrip(Rescheduling rescheduling) {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_SERVICE + "/reschedule";
		restTemplate.postForObject(uri, rescheduling, Void.class);
	}

	@Override
	public List<Date> findAvailableDatesForTripType(Long tripTypeId) {
		final String uri = webServiceHost + ":" + webServicePort + "/trips/available-dates/" + tripTypeId;
		ResponseEntity<Date[]> response = restTemplate.getForEntity(uri, Date[].class);
		return Arrays.asList(response.getBody());
	}

}
