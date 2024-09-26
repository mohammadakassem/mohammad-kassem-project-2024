package FinalProject.FinalProject.service;

import FinalProject.FinalProject.model.Payment;
import FinalProject.FinalProject.model.Reservation;
import FinalProject.FinalProject.repository.PaymentRepository;
import FinalProject.FinalProject.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;
import java.util.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentsService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public Payment createPayment(UUID reservationId, double amount, String status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(amount);
        payment.setStatus(status);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }




    @Transactional
    public Payment getPaymentByReservationId(UUID reservationId) {
        return paymentRepository.findByReservationId(reservationId)
                .orElse(null);
    }

    @Transactional
    public ResponseEntity<String> processPayment(UUID reservationId, Map<String, Double> request) {
        // Fetch the reservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        // Check if the reservation is active
        if (!reservation.isActive()) {
            throw new IllegalStateException("Cannot process payment for an inactive reservation.");
        }

        // Fetch the payment record associated with the reservation
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Payment record not found"));

        // Set the payment amount to the value from the request (or default to 0)
        double totalAmount = request.getOrDefault("amount", 0.0);
        payment.setAmount(totalAmount);

        // Mark the payment as completed
        payment.setStatus("COMPLETED");
        paymentRepository.save(payment);

        return ResponseEntity.ok("Payment processed successfully!");
    }
    @Transactional
    public List<Payment> getPaymentsByUserId(UUID userId) {
        return paymentRepository.findByUserId(userId);
    }
    // Additional methods for handling payments, such as updating status, etc.

}