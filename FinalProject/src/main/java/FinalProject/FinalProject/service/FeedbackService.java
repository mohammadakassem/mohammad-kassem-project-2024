package FinalProject.FinalProject.service;

import FinalProject.FinalProject.model.Feedback;
import FinalProject.FinalProject.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;


    public void saveFeedback(UUID userId, Feedback feedback) {
        feedback.setUserId(userId); // Set userId in the feedback object
        feedbackRepository.save(feedback); // Save the feedback in the repository
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll(); // Retrieve all feedback
    }
}
