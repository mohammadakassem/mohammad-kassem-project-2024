package FinalProject.FinalProject.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import FinalProject.FinalProject.model.Area;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;
@Repository
public interface AreasRepository extends JpaRepository<Area, UUID> {

    void deleteAll();
    List<Area> findAll();
}