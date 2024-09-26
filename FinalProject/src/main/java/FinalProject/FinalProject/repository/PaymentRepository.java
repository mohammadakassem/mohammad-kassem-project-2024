package FinalProject.FinalProject.repository;


import FinalProject.FinalProject.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReservationId(UUID reservationId);
    @Query("SELECT SUM(p.amount) FROM Payment p")
    Double calculateTotalProfit();
    List<Payment> findByUserId(UUID userId);

}