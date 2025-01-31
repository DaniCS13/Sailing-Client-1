package cat.institutmarianao.sailing.services.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cat.institutmarianao.sailing.model.Action;
import cat.institutmarianao.sailing.model.BookedPlace;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;
import jakarta.validation.constraints.NotNull;

@Service
@PropertySource("classpath:application.properties")
public class TripServiceImpl implements TripService {

	private static final String TRIPS_SERVICE = "/trips";
	private static final String TRIPS_FIND_ALL = TRIPS_SERVICE + "/find/all";
	private static final String TRIPS_FIND_BY_USERNAME = TRIPS_SERVICE + "/find/by/username/{username}";
	private static final String TRIPS_SAVE = TRIPS_SERVICE + "/save";
	private static final String TRIPS_BOOKED_PLACES = TRIPS_SERVICE + "/booked-places/{id}/{date}";
	private static final String TRIPS_TYPES_ALL = TRIPS_SERVICE + "/types/all";
	private static final String TRIPS_TYPES_GROUP = TRIPS_SERVICE + "/types/group";
	private static final String TRIPS_TYPES_PRIVATE = TRIPS_SERVICE + "/types/private";
	private static final String TRIPS_TYPE_BY_ID = TRIPS_SERVICE + "/types/{id}";
	private static final String TRIPS_TRACKING = TRIPS_SERVICE + "/tracking/{id}";
	private static final String TRIPS_TRACK = TRIPS_SERVICE + "/track";

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
		final String baseUri = webServiceHost + ":" + webServicePort + TRIPS_FIND_BY_USERNAME;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("username", username);

		ResponseEntity<Trip[]> response = restTemplate.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), Trip[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public Trip save(Trip trip) {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_SAVE;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Trip> request = new HttpEntity<>(trip, headers);

		return restTemplate.postForObject(uri, request, Trip.class);
	}

	@Override
	public List<BookedPlace> findBookedPlacesByTripIdAndDate(@NotNull Long id, @NotNull Date date) {
		final String baseUri = webServiceHost + ":" + webServicePort + TRIPS_BOOKED_PLACES;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("id", id.toString());
		uriVariables.put("date", date.toString());

		ResponseEntity<BookedPlace[]> response = restTemplate.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), BookedPlace[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_TYPES_ALL;
		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllGroupTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_TYPES_GROUP;
		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllPrivateTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_TYPES_PRIVATE;
		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public TripType getTripTypeById(Long id) {
		final String baseUri = webServiceHost + ":" + webServicePort + TRIPS_TYPE_BY_ID;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("id", id.toString());

		return restTemplate.getForObject(uriTemplate.buildAndExpand(uriVariables).toUriString(), TripType.class);
	}

	@Override
	public List<Action> findTrackingById(Long id) {
		final String baseUri = webServiceHost + ":" + webServicePort + TRIPS_TRACKING;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("id", id.toString());

		ResponseEntity<Action[]> response = restTemplate.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), Action[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public Action track(Action action) {
		final String uri = webServiceHost + ":" + webServicePort + TRIPS_TRACK;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Action> request = new HttpEntity<>(action, headers);

		return restTemplate.postForObject(uri, request, Action.class);
	}
}
