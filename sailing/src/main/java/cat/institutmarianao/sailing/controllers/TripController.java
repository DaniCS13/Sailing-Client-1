package cat.institutmarianao.sailing.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jakarta.validation.constraints.Positive;

@Controller
@SessionAttributes({ "trip", "tripType", "freePlaces", "tripFreePlaces" })
@RequestMapping(value = "/trips")
public class TripController {

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
		// Get trip types and the corresponding available dates
		TripType tripType = tripService.getTripTypeById(tripTypeId);
		List<Date> availableDates = tripService.findAvailableDatesForTripType(tripTypeId); // Implement in TripService

		// Store in session attributes
		ModelAndView modelAndView = new ModelAndView("selectDate");
		modelAndView.addObject("tripType", tripType);
		modelAndView.addObject("availableDates", availableDates);

		return modelAndView;
	}

	@PostMapping("/book/book_departure")
	public String bookSelectDeparture(@Validated(OnTripCreateDate.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces, ModelMap modelMap) {
		if (result.hasErrors()) {
			return "selectDate"; // Return to date selection if validation fails
		}

		// Now, prepare for departure selection, passing free places information
		modelMap.addAttribute("tripType", tripType);
		modelMap.addAttribute("selectedDate", trip.getDepartureDate());
		modelMap.addAttribute("freePlaces", freePlaces.get(trip.getDepartureDate())); // Show available places for the
																						// selected date

		return "selectDeparture";
	}

	@PostMapping("/book/book_places")
	public String bookSelectPlaces(@Validated(OnTripCreateDeparture.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap) {
		if (result.hasErrors()) {
			return "selectDeparture"; // Return to departure selection if validation fails
		}

		// Prepare to select places based on available free places
		modelMap.addAttribute("tripType", tripType);
		modelMap.addAttribute("selectedDate", trip.getDepartureDate());
		modelMap.addAttribute("availablePlaces", freePlaces.get(trip.getDepartureDate())); // Show available places

		return "selectPlaces";
	}

	@PostMapping("/book/book_save")
	public String bookSave(@Validated(OnTripCreate.class) @ModelAttribute("trip") Trip trip, BindingResult result,
			@SessionAttribute("tripType") TripType tripType, @SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap, SessionStatus sessionStatus) {
		if (result.hasErrors()) {
			return "selectPlaces"; // Return to place selection if validation fails
		}

		// Save the trip and complete the booking
		tripService.save(trip); // Implement the save logic in the TripService
		sessionStatus.setComplete(); // Clear session data after booking is done

		return "bookingConfirmation"; // Redirect to a confirmation page
	}

	@GetMapping("/booked")
	public ModelAndView booked() {
		// Obtener todos los viajes reservados
		List<Trip> bookedTrips = tripService.findAllBookedTrips(); // Implementa el método `findAllBookedTrips()` en el
																	// servicio

		ModelAndView modelAndView = new ModelAndView("bookedTrips"); // Redirige a una vista llamada "bookedTrips"
		modelAndView.addObject("bookedTrips", bookedTrips); // Pasamos los viajes reservados al modelo

		return modelAndView;
	}

	@PostMapping("/cancel")
	public String cancelTrip(@Validated Cancellation cancellation) {
		// Add cancellation action to trip tracking
		tripService.cancelTrip(cancellation); // Implement this in the TripService

		return "cancellationConfirmation"; // Redirect to a confirmation page
	}

	@PostMapping("/done")
	public String doneTrip(@Validated(OnActionCreate.class) Done done) {
		// Add done action to trip tracking
		tripService.markTripAsDone(done); // Implement this in the TripService

		return "doneConfirmation"; // Redirect to a confirmation page
	}

	@PostMapping("/reschedule")
	public String saveAction(@Validated(OnActionCreate.class) Rescheduling rescheduling) {
		// Add reschedule action to trip tracking
		tripService.rescheduleTrip(rescheduling); // Implement this in the TripService

		return "rescheduleConfirmation"; // Redirect to a confirmation page
	}

	@GetMapping("/tracking/{id}")
	public String showContentPart(@PathVariable(name = "id", required = true) @Positive Long id, ModelMap modelMap) {
		// Retrieve the tracking actions for the given trip ID
		List<Action> actions = tripService.getTrackingByTripId(id); // Implement in TripService

		modelMap.addAttribute("actions", actions);

		return "tracking"; // Redirect to a page where actions are displayed
	}
}
