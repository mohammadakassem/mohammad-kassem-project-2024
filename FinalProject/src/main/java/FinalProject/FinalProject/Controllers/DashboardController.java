package FinalProject.FinalProject.Controllers;

import FinalProject.FinalProject.model.Area;
import FinalProject.FinalProject.repository.AreasRepository;
import FinalProject.FinalProject.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {
    @Autowired
    private AreasRepository areaService;
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Add any necessary model attributes here
        return "HomePage"; // This should match your HTML template name
    }



    @GetMapping("/login")
    public String showLogin(Model model) {
        // Logic for login
        return "login"; // Login template
    }

    @GetMapping("/about")
    public String showAbout(Model model) {
        return "Aboutus"; // About us template
    }

    @GetMapping("/reservation")
    public String showReservation(Model model) {
        List<Area> areas = areaService.findAll(); // Fetch all areas from your service
        model.addAttribute("areas", areas); // Add areas to the model
        return "Reservationarea"; // Return the Reservationarea template
    }

    @GetMapping("/payments")
    public String showPayments(Model model) {
        return "payments"; // Payments template
    }
}
