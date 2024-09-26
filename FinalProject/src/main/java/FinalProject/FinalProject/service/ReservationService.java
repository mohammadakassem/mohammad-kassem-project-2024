package FinalProject.FinalProject.service;

import FinalProject.FinalProject.model.Area;
import FinalProject.FinalProject.model.Payment;
import FinalProject.FinalProject.model.Reservation;
import FinalProject.FinalProject.model.Spot;
import FinalProject.FinalProject.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpotService spotService;
    @Autowired
    private AreasRepository areasRepository;
    @Autowired
    private AreaService areasService;
    @Autowired
    private PaymentRepository paymentRepository;
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    public ReservationService(UserRepository userRepository, SpotRepository spotRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.spotRepository = spotRepository;
        this.reservationRepository = reservationRepository;
    }
    @Transactional
    public Reservation reserveSpotInArea(UUID userId, UUID areaId, LocalDateTime startTime, LocalDateTime endTime) {
        // Fetch the area by its ID
        Area area = areasRepository.findById(areaId)
                .orElseThrow(() -> new IllegalArgumentException("Area not found"));

        // Find available spots in the area
        List<Spot> availableSpots = spotService.getAvailableSpotsForArea(areaId);

        // If no spots are available, throw an exception
        if (availableSpots.isEmpty()) {
            throw new IllegalStateException("No available spots in this area");
        }

        Spot reservedSpot = null;

        // Loop through available spots to find the first one that is not reserved
        for (Spot spot : availableSpots) {
            if (!spot.isReserved()) {
                reservedSpot = spot;
                spotRepository.save(reservedSpot);
                break; // Exit the loop once a spot is reserved
            }
        }

        // If no spot was found that could be reserved, throw an exception
        if (reservedSpot == null) {
            throw new IllegalStateException("No unreserved spots available");
        }

        areasRepository.save(area); // Save the updated area

        // Create and save the reservation
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setSpot(reservedSpot); // Assign the reserved spot
        reservation.setAreaId(areaId);
        reservation.setActive(true); // Set the reservation as active

        reservation = reservationRepository.save(reservation); // Save the reservation

        // Calculate payment amount
        double paymentAmount = calculatePaymentAmount(reservation.getId());
        Payment payment = new Payment();
        payment.setUserId(userId); // Set the userId
// Create and save the payment record

        payment.setAmount(paymentAmount);
        payment.setReservation(reservation);
        payment.setStatus("PENDING"); // Or another appropriate status
        payment.setUserId(userId); // Ensure this is set

// Set the payment date
        payment.setPaymentDate(LocalDateTime.now());  // Setting the current date and time

        paymentRepository.save(payment);


        // Log reservation details
        System.out.println("Creating reservation for User ID: " + userId + " in Area ID: " + areaId);

        return reservation; // Return the saved reservation
    }


    public double calculatePaymentAmount(UUID reservationId) {
        // Logic to calculate payment amount based on reservation duration
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        long hours = Duration.between(reservation.getStartTime(), reservation.getEndTime()).toHours();
        return hours * RATE_PER_HOUR; // Assuming RATE_PER_HOUR is defined as a class constant
    }

    private static final double RATE_PER_HOUR = 3.0;


    public Reservation getReservationById(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));
    }
    public UUID findReservationIdByUser(UUID userId) {
        // Find all reservations by userId
        List<Reservation> reservations = reservationRepository.findByUserId(userId);

        if (!reservations.isEmpty()) {
            // Return the ID of the first reservation in the list
            return reservations.get(0).getId();
        } else {
            // Throw an exception or handle the case where no reservations are found
            throw new IllegalArgumentException("No reservation found for user");
        }
    }
    public List<Reservation> getReservationsByUser(UUID userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getReservationsBySpot(UUID spotId) {
        return reservationRepository.findBySpotId(spotId);
    }

    @Transactional
    public void cancelReservation(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        // Fetch spot and mark it as available
        Spot spot = reservation.getSpot();
        System.out.println("Before cancellation: Spot availability = " + spot.isAvailable());
        spot.setAvailable(true);
        spotRepository.save(spot);
        System.out.println("After cancellation: Spot availability = " + spot.isAvailable());

        // Update the available spots count in the area
        Area area = spot.getArea();

        areasRepository.save(area);

        // Delete payment if it exists
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalStateException("Payment record not found for reservation ID: " + reservationId));
        paymentRepository.delete(payment);

        // Finally, delete the reservation itself
        reservationRepository.deleteById(reservationId);
    }


    // Payment deduction logic
    @Transactional
    public void deductPayment(UUID reservationId, double deductionAmount) {
        // Fetch the payment record associated with the reservation
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalStateException("Payment record not found for reservation ID: " + reservationId));

        // Deduct the specified amount
        double newAmount = payment.getAmount() - deductionAmount;
        if (newAmount < 0) {
            newAmount = 0; // Ensure the payment amount doesn't go negative
        }
        payment.setAmount(newAmount);

        // Optionally, update the status if required
        payment.setStatus("UPDATED");

        // Save the updated payment
        paymentRepository.save(payment);
    }

    public void deleteReservation(UUID reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        Spot spot = reservation.getSpot();
        spot.setAvailable(true); // Mark the spot as available again

        reservationRepository.delete(reservation);
        spotRepository.save(spot); // Ensure the spot state is updated in the repository
    }


    @Transactional
    public void deleteAllReservations() {
        reservationRepository.deleteAll();
    }
}