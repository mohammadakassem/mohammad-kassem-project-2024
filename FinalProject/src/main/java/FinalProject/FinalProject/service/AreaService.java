package FinalProject.FinalProject.service;

import FinalProject.FinalProject.model.Area;
import FinalProject.FinalProject.model.Spot;
import FinalProject.FinalProject.repository.AreasRepository;
import FinalProject.FinalProject.repository.SpotRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AreaService {

    @Autowired
    private AreasRepository areasRepository;

    @Autowired
    private SpotRepository spotRepository;

    // Fetch all areas
    public List<Area> getAllAreas() {
        return areasRepository.findAll();
    }

    // Fetch a specific area by ID
    public Area getAreaById(UUID id) {
        return areasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Area not found with ID: " + id));
    }

    @Transactional
    public void deleteArea(UUID id) {
        // Check if the area exists
        if (!areasRepository.existsById(id)) {
            throw new IllegalArgumentException("Area not found with ID: " + id);
        }

        // Fetch the area to ensure the associated spots can be deleted
        Area area = areasRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Area not found with ID: " + id));

        // Delete related spots (this will also cascade delete reservations if properly configured)
        List<Spot> spots = area.getSpots();
        for (Spot spot : spots) {
            spotRepository.delete(spot);
        }

        // Finally, delete the area
        areasRepository.deleteById(id);
    }

    @Transactional
    public Area createArea(String name, String location, LocalDateTime openingTime, LocalDateTime closingTime, String image, int totalSpots, String description) {
        Area area = new Area();
        area.setName(name);
        area.setLocation(location);
        area.setOpeningTime(openingTime);
        area.setClosingTime(closingTime);
        area.setImage(image);
        area.setTotalSpots(totalSpots);
        area.setAvailableSpots(totalSpots);
        area.setDescription(description);
        area.setCreatedAt(LocalDateTime.now());

        area = areasRepository.save(area);

        for (int i = 1; i <= totalSpots; i++) {
            Spot spot = new Spot();
            spot.setSpotNumber(i);
            spot.setAvailable(true);
            spot.setArea(area);
            spot.setReserved(false); // Set default for isReserved

            try {
                spotRepository.save(spot);
                System.out.println("Saved spot: " + spot);
            } catch (DataIntegrityViolationException e) {
                System.err.println("Error saving spot: " + spot + " - Duplicate entry detected.");
                // You may want to continue or handle this case more gracefully
            }
        }

        return area;
    }

    public void deleteAllAreas() {
        areasRepository.deleteAll();
    }
    // Update an existing area by ID
    public Area updateArea(UUID id, Area updatedArea) {
        // Retrieve the existing area from the repository
        Area existingArea = areasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found"));

        // Update the existing area waith new values
        existingArea.setName(updatedArea.getName());
        existingArea.setLocation(updatedArea.getLocation());
        existingArea.setOpeningTime(updatedArea.getOpeningTime());
        existingArea.setClosingTime(updatedArea.getClosingTime());
        existingArea.setImage(updatedArea.getImage());
        existingArea.setDescription(updatedArea.getDescription());


        // Save the updated area back to the repository
        return areasRepository.save(existingArea);
    }

}