package FinalProject.FinalProject.repository;


import FinalProject.FinalProject.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.UUID;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUserId(UUID userId); // Example for userId
    List<Reservation> findBySpotId(UUID spotId);



}