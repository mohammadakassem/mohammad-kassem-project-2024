package FinalProject.FinalProject.service;


import FinalProject.FinalProject.model.Spot;
import FinalProject.FinalProject.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpotService {
    @Autowired
    private SpotRepository spotRepository;

    public List<Spot> getAllAvailableSpots() {
        List<Spot> availableSpots = spotRepository.findByAvailableTrue();
        System.out.println("All available spots count: " + availableSpots.size());
        return availableSpots;
    }

    public List<Spot> getAvailableSpotsForArea(UUID areaId) {
        List<Spot> availableSpots = spotRepository.findByAreaIdAndAvailableTrue(areaId);
        System.out.println("Available spots for area " + areaId + ": " + availableSpots.size());
        return availableSpots;
    }
    public void deleteAll() {
        spotRepository.deleteAll();
    }
    public Optional<Spot> getSpotById(UUID id) {
        return spotRepository.findById(id);
    }
}