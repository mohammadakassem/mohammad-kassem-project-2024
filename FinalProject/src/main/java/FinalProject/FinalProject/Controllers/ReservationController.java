package FinalProject.FinalProject.Controllers;


import FinalProject.FinalProject.dto.ReservationRequestDTO;
import FinalProject.FinalProject.model.Area;
import FinalProject.FinalProject.model.Reservation;
import FinalProject.FinalProject.model.User;
import FinalProject.FinalProject.repository.AreasRepository;
import FinalProject.FinalProject.service.AreaService;
import FinalProject.FinalProject.service.ReservationService;
import FinalProject.FinalProject.service.SpotService;
import FinalProject.FinalProject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private SpotService spotService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/reserve")
    public String showReserveForm(Model model, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");

        if (userId == null) {
            model.addAttribute("errorMessage", "User ID not found");
            return "login"; // Or handle this error appropriately
        }

        model.addAttribute("userId", userId); // Set userId for the form
        model.addAttribute("reservationRequest", new ReservationRequestDTO());
        model.addAttribute("areas", areaService.getAllAreas());
        return "Reservearea"; // Thymeleaf template name
    }


    @PostMapping("/reserve")
    public String reserveSpotInArea(@ModelAttribute ReservationRequestDTO requestDto, HttpSession session, Model model) {
        UUID userId = (UUID) session.getAttribute("userId"); // Get user ID from session
        System.out.println("User ID from session: " + userId); // Debugging line

        // Validate request data
        if (userId == null || requestDto.getAreaId() == null ||
                requestDto.getStartTime() == null || requestDto.getEndTime() == null) {
            model.addAttribute("errorMessage", "Please fill in all fields.");
            return "reserve"; // Return to reserve page with error
        }

        requestDto.setUserId(userId); // Set userId from session

        try {
            Reservation reservation = reservationService.reserveSpotInArea(
                    requestDto.getUserId(),
                    requestDto.getAreaId(),
                    requestDto.getStartTime(),
                    requestDto.getEndTime()
            );
            model.addAttribute("successMessage", "Reservation successful!");
            return "redirect:/reservation"; // Redirect after successful reservation
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error reserving the spot: " + e.getMessage());
            return "reserve"; // Return to reserve page with error
        }
    }



    @Autowired
    private AreaService areaService;


    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable UUID id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    // Get all reservations by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable UUID userId) {
        List<Reservation> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }

    // Get all reservations by spot ID
    @GetMapping("/spot/{spotId}")
    public ResponseEntity<List<Reservation>> getReservationsBySpot(@PathVariable UUID spotId) {
        List<Reservation> reservations = reservationService.getReservationsBySpot(spotId);
        return ResponseEntity.ok(reservations);
    }

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    // Cancel a reservation
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Delete a reservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteAllReservations() {
        reservationService.deleteAllReservations();
        return ResponseEntity.noContent().build(); // 204 No Content
    }

}