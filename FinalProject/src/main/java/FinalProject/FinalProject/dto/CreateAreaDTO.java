package FinalProject.FinalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAreaDTO {
    private String name;
    private String location;
    private LocalDateTime openingTime; // Ensure this is LocalDateTime
    private LocalDateTime closingTime; // Ensure this is LocalDateTime
    private String image;
    private int totalSpots;
    private String description;

    // Getters and Setters
}
