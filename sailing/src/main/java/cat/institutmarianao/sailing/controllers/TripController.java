package cat.institutmarianao.sailing.controllers;

import java.util.Date;
import java.util.HashMap;
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
		// TODO - Prepare a dialog to select a departure date for the booked trip with
		// id tripTypeId
		return null;
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
	public ModelAndView booked() {
		// TODO - Retrieve all booked trips
		return null;
	}

	@PostMapping("/cancel")
	public String cancelTrip(@Validated Cancellation cancellation) {

		// TODO - Cancel a trip (add a CANCELLATION action to its tracking)
		return null;
	}

	@PostMapping("/done")
	public String doneTrip(@Validated(OnActionCreate.class) Done done) {

		// TODO - Do a trip (add a DONE action to its tracking)
		return null;
	}

	@PostMapping("/reschedule")
	public String saveAction(@Validated(OnActionCreate.class) Rescheduling rescheduling) {

		// TODO - Reschedule a trip (add a RESCHEDULE action to its tracking)
		return null;
	}

	@GetMapping("/tracking/{id}")
	public String showContentPart(@PathVariable(name = "id", required = true) @Positive Long id, ModelMap modelMap) {

		// TODO - Retrieve the tracking for the trip id with id id
		return null;
	}
}
