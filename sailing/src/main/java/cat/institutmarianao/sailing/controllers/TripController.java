package cat.institutmarianao.sailing.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cat.institutmarianao.sailing.model.BookedPlace;
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
		ModelAndView trips = new ModelAndView("book_date");
		TripType tripType = tripService.getTripTypeById(tripTypeId);

		trips.addObject("tripType", tripType);

		return trips;
	}

	@PostMapping("/book/book_departure")
	public String bookSelectDeparture(@Validated(OnTripCreateDate.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces, ModelMap modelMap) {

		if (result.hasErrors()) {
			modelMap.addAttribute("trip", trip);
			modelMap.addAttribute("tripType", tripType);
			modelMap.addAttribute("freePlaces", freePlaces);
			return "book_date";
		}

		modelMap.addAttribute("trip", trip);
		modelMap.addAttribute("tripType", tripType);
		modelMap.addAttribute("freePlaces", freePlaces);

		return "book_departure";
	}

	@PostMapping("/book/book_places")
	public String bookSelectPlaces(@Validated(OnTripCreateDeparture.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, ModelMap modelMap) {

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> System.out.println(error.toString()));
			modelMap.addAttribute("trip", trip);
			return "book_departure";
		}

		TripType tripType = (TripType) modelMap.getAttribute("tripType");
		if (tripType == null) {
			throw new IllegalStateException("TripType no encontrado en la sesión");
		}

		List<BookedPlace> bookedPlaces = tripService.findBookedPlacesByTripIdAndDate(tripType.getId(), trip.getDate());

		long bookedSeats = bookedPlaces.isEmpty() ? 0 : bookedPlaces.get(0).getBookedPlaces();
		long availablePlaces = tripType.getMaxPlaces() - bookedSeats;

		modelMap.addAttribute("bookedPlaces", bookedPlaces);
		modelMap.addAttribute("trip", trip);
		modelMap.addAttribute("tripType", tripType);
		modelMap.addAttribute("tripFreePlaces", availablePlaces);

		return "book_places";
	}

	@PostMapping("/book/book_save")
	public String bookSave(@Validated(OnTripCreate.class) @ModelAttribute("trip") Trip trip, BindingResult result,
			@SessionAttribute("tripType") TripType tripType, @SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap, SessionStatus sessionStatus,
			Authentication authentication) {

		if (authentication != null) {
			String username = authentication.getName();
			trip.setClientUsername(username);
		}

		if (tripType != null && tripType.getId() != null) {
			trip.setTypeId(tripType.getId());
		}

		System.out.println("Received trip2: " + trip);

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> System.out.println(error.toString()));
			modelMap.addAttribute("trip", trip);
			modelMap.addAttribute("tripType", tripType);
			modelMap.addAttribute("freePlaces", freePlaces);
			modelMap.addAttribute("tripFreePlaces", tripFreePlaces);
			return "book_places";
		}

		tripService.save(trip);
		sessionStatus.setComplete();

		return "redirect:/trips/booked";
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

		Map<Long, String> tripTypeTitle = new HashMap<>();
		Map<Long, String> categoryTitle = new HashMap<>();

		for (TripType tripType : allTripTypes) {
			tripTypeTitle.put(tripType.getId(), tripType.getTitle());
			categoryTitle.put(tripType.getId(), tripType.getCategory().name());
		}

		trips.addObject("trips", tripList);
		trips.addObject("tripTypeMap", tripTypeTitle);
		trips.addObject("tripTypeCategories", categoryTitle);
		trips.addObject("done", new Done());
		trips.addObject("rescheduling", new Rescheduling());
		trips.addObject("cancellation", new Cancellation());
		trips.addObject("role", role);

		return trips;
	}

	@PostMapping("/cancel")
	public String cancelTrip(@Validated Cancellation cancellation, Authentication authentication) {
		String username = authentication.getName();
		cancellation.setPerformer(username);
		tripService.track(cancellation);

		return "redirect:/trips/booked";
	}

	@PostMapping("/done")
	public String doneTrip(@Validated(OnActionCreate.class) Done done, Authentication authentication) {
		String username = authentication.getName();
		done.setPerformer(username);
		tripService.track(done);

		return "redirect:/trips/booked";
	}

	@PostMapping("/reschedule")
	public String saveAction(@Validated(OnActionCreate.class) Rescheduling rescheduling,
			Authentication authentication) {
		rescheduling.setDate(new Date());

		String username = authentication.getName();
		rescheduling.setPerformer(username);
		tripService.track(rescheduling);

		return "redirect:/trips/booked";
	}

	@GetMapping("/tracking/{id}")
	public String showContentPart(@PathVariable(name = "id", required = true) @Positive Long id, ModelMap modelMap) {
		modelMap.addAttribute("tripId", id);
		modelMap.addAttribute("tracking", tripService.findTrackingById(id));
		return "fragments/dialogs :: tracking_dialog_body";
	}

}
