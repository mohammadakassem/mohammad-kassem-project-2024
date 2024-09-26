package FinalProject.FinalProject.repository;

import FinalProject.FinalProject.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
