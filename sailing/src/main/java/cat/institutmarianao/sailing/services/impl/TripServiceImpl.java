package cat.institutmarianao.sailing.services.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
    private static final String TRIPS_FIND_BY_USERNAME = TRIPS_SERVICE + "/types/private";

    @Value("${webService.host}")
    private String webServiceHost;

    @Value("${webService.port}")
    private String webServicePort;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<TripType> getAllGroupTripTypes() {
        final String uri = buildUri(TRIPS_TYPES_GROUP);
        
        try {
            ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            System.err.println("Error al obtener los viajes en grupo: " + e.getMessage());
            return List.of(); // Devuelve una lista vacía en caso de error
        }
    }

    @Override
    public List<TripType> getAllPrivateTripTypes() {
        final String uri = buildUri(TRIPS_TYPES_PRIVATE);
        
        try {
            ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            System.err.println("Error al obtener los viajes privados: " + e.getMessage());
            return List.of();
        }
    }

    private String buildUri(String endpoint) {
        return String.format("%s:%s%s", webServiceHost.replaceAll("/$", ""), webServicePort, endpoint);
    }

	@Override
	public List<Trip> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Trip> findAllByClientUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trip save(Trip trip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookedPlace> findBookedPlacesByTripIdAndDate(@NotNull Long id, @NotNull Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TripType> getAllTripTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TripType getTripTypeById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Action> findTrackingById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action track(Action action) {
		// TODO Auto-generated method stub
		return null;
	}
}
