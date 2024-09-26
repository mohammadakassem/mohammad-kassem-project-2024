package FinalProject.FinalProject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId; // New field for user ID





    @Column(length = 1000)
    private String message;

    // Constructors, getters, and setters

    public Feedback() {}

    public Feedback(UUID userId, String name, String email, String message) {
        this.userId = userId;

        this.message = message;
    }


}
