package FinalProject.FinalProject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "active", nullable = false)
    private boolean isActive = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "spot_id", referencedColumnName = "id")

    private Spot spot;
    @Column(name = "area_id")
    private UUID areaId; // Area ID for the reservation
}