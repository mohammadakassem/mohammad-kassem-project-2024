package FinalProject.FinalProject.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "spots", uniqueConstraints = @UniqueConstraint(columnNames = {"spotNumber", "area_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int spotNumber; // Unique identifier for the spot within the area

    private boolean available; // Availability status of the spot

    @Column(name = "is_reserved", nullable = false)
    private boolean isReserved; // Indicates if the spot is reserved

    @ManyToOne
    @JoinColumn(name = "area_id")
    @JsonBackReference
    private Area area;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations;

    public boolean isAvailable() {
        return available && !isReserved;
    }

    public void reserveSpot() {
        if (isAvailable()) {
            this.isReserved = true;
            this.available = false;
        } else {
            throw new IllegalStateException("Spot is already reserved");
        }
    }
}
