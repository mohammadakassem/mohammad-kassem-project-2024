package FinalProject.FinalProject.repository;


import FinalProject.FinalProject.model.Area;
import FinalProject.FinalProject.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface SpotRepository extends JpaRepository<Spot, UUID> {

    List<Spot> findByArea(Area area); // Adjusted to Integer


    // Find spots by availability status
    List<Spot> findByAvailableTrue();

    void deleteAll();
    List<Spot> findByAreaIdAndAvailableTrue(UUID areaId);
}