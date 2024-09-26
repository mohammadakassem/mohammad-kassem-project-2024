package FinalProject.FinalProject.Controllers;

import FinalProject.FinalProject.model.Spot;
import FinalProject.FinalProject.service.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    @Autowired
    private SpotService spotService;

    @GetMapping("/available")
    public ResponseEntity<List<Spot>> getAvailableSpots(@RequestParam Optional<UUID> areaId) {

            List<Spot> availableSpots = areaId.isPresent() ?
                    spotService.getAvailableSpotsForArea(areaId.get()) :
                    spotService.getAllAvailableSpots();

            return new ResponseEntity<>(availableSpots, HttpStatus.OK);


    } @DeleteMapping
    public ResponseEntity<Void> DeleteAreas() {
        spotService.deleteAll();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Integer> getSpotNumberById(@PathVariable UUID id) {
        Optional<Spot> spot = spotService.getSpotById(id);
        if (spot.isPresent()) {
            return ResponseEntity.ok(spot.get().getSpotNumber());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}