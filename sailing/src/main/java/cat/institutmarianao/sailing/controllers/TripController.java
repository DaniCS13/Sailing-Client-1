package cat.institutmarianao.sailing.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import cat.institutmarianao.sailing.model.Action;
import cat.institutmarianao.sailing.model.Cancellation;
import cat.institutmarianao.sailing.model.Done;
import cat.institutmarianao.sailing.model.Rescheduling;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;
import cat.institutmarianao.sailing.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreateDate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreateDeparture;
import jakarta.servlet.ServletException;
import jakarta.validation.constraints.Positive;

@Controller
@SessionAttributes({ "trip", "tripType", "freePlaces", "tripFreePlaces" })
@RequestMapping(value = "/trips")
public class TripController {

	@Autowired
	private TripService tripService;

	@ModelAttribute("trip")
	private Trip setuoTrip() {
		return new Trip();
	}

	@ModelAttribute("tripType")
	private TripType setupTripType() {
		return new TripType();
	}

	@ModelAttribute("freePlaces")
	private Map<Date, Long> setupFreePlaces() {
		return new HashMap<>();
	}

	@ModelAttribute("tripFreePlaces")
	private Long setupTripFreePlaces() {
		return 0l;
	}

	@GetMapping("/book/{trip_type_id}")
	public ModelAndView bookSelectDate(@PathVariable(name = "trip_type_id", required = true) Long tripTypeId) {
		ModelAndView trips = new ModelAndView("book_places");
		TripType trip = tripService.getTripTypeById(tripTypeId);

		trips.addObject("trip", trip);

		return trips;
	}

	@PostMapping("/book/book_departure")
	public String bookSelectDeparture(@Validated(OnTripCreateDate.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces, ModelMap modelMap) {

		// TODO - Prepare a dialog to select a departure time for the booked trip
		// TODO - Leave all free places for the selected trip in the selected departure
		// date in session (freePlaces attribute)
		return null;
	}

	@PostMapping("/book/book_places")
	public String bookSelectPlaces(@Validated(OnTripCreateDeparture.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap) {

		// TODO - Prepare a dialog to select places for the booked trip
		return null;
	}

	@PostMapping("/book/book_save")
	public String bookSave(@Validated(OnTripCreate.class) @ModelAttribute("trip") Trip trip, BindingResult result,
			@SessionAttribute("tripType") TripType tripType, @SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap, SessionStatus sessionStatus) {

		// TODO - Saves a booking
		return null;
	}

	@GetMapping("/booked")
	public ModelAndView booked() throws ServletException, IOException {
		ModelAndView trips = new ModelAndView("trips");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication != null ? authentication.getName() : null;

		String role = authentication != null && authentication.getAuthorities() != null
				? authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("")
				: "";
		System.out.println(role);

		List<Trip> tripList;

		if ("ROLE_ADMIN".equals(role)) {
			tripList = tripService.findAll();
		} else if ("ROLE_CLIENT".equals(role)) {
			tripList = tripService.findAllByClientUsername(username);
		} else {
			tripList = new ArrayList<>();
		}

		List<TripType> allTripTypes = tripService.getAllTripTypes();

		Map<Long, Object> tripTypeMap = allTripTypes.stream()
				.collect(Collectors.toMap(TripType::getId, tripType -> tripType));

		trips.addObject("trips", tripList);

		trips.addObject("tripTypeMap", tripTypeMap);

		trips.addObject("done", new Done());
		trips.addObject("rescheduling", new Rescheduling());
		trips.addObject("cancellation", new Cancellation());

		return trips;
	}

	@PostMapping("/cancel")
	public String cancelTrip(@Validated Cancellation cancellation) {

		// TODO - Cancel a trip (add a CANCELLATION action to its tracking)
		return null;
	}

	@PostMapping("/done")
	public String doneTrip(@Validated(OnActionCreate.class) Done done) {
		System.out.println(done.toString());
		Action action = tripService.track(done);
		return "redirect:/trips/booked";
	}

	@PostMapping("/reschedule")
	public String saveAction(@Validated(OnActionCreate.class) Rescheduling rescheduling) {

		// TODO - Reschedule a trip (add a RESCHEDULE action to its tracking)
		return null;
	}

	@GetMapping("/tracking/{id}")
	public String showContentPart(@PathVariable(name = "id", required = true) @Positive Long id, ModelMap modelMap) {
		modelMap.addAttribute("tripId", id);
		modelMap.addAttribute("tracking", tripService.findTrackingById(id));
		return "fragments/dialogs :: tracking_dialog_body";
	}

	private void prepareBookDate(ModelMap modelMap, Trip trip) {
		modelMap.addAttribute("trip", trip);
		modelMap.addAttribute("tripType", tripService.getTripTypeById(trip.getTypeId()));
		modelMap.addAttribute("action", "/trips/book/book_departure");
	}

}
