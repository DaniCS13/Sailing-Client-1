package cat.institutmarianao.sailing.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/")
public class HomeController {

	@Autowired
	private TripService tripService;

	@GetMapping
	public ModelAndView trips(HttpServletRequest request) throws ServletException, IOException {
		ModelAndView home = new ModelAndView("home");
		List<TripType> group = tripService.getAllGroupTripTypes();
		home.getModelMap().addAttribute("groupTrips", group);
		home.getModelMap().addAttribute("privateTrips", tripService.getAllPrivateTripTypes());

		return home;
	}
}
