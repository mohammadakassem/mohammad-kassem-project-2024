package FinalProject.FinalProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String status; // e.g., "PAID", "PENDING"
    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private UUID userId; // Ensure this field is not nullable


}