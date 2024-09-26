package FinalProject.FinalProject.Controllers;


import FinalProject.FinalProject.model.Feedback;
import FinalProject.FinalProject.service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Use this if you have Spring Security for user authentication
import org.springframework.security.core.userdetails.UserDetails; // Use this for getting user details
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping // Now accesses /feedback directly
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        model.addAttribute("feedbackList", feedbackService.getAllFeedback());
        return "feedback"; // Thymeleaf template name
    }

    @PostMapping // Still accesses /feedback for POST
    public String submitFeedback(HttpSession session, Feedback feedback, RedirectAttributes redirectAttributes) {
        // Retrieve userId from the session
        UUID userId = (UUID) session.getAttribute("userId");

        // Check if user ID is null
        if (userId == null) {
            return "redirect:/error"; // Redirect to a specific error page if userId is not found
        }

        feedbackService.saveFeedback(userId, feedback); // Save feedback with userId
        redirectAttributes.addFlashAttribute("message", "Feedback submitted successfully!");
        return "redirect:/feedback?success=true"; // Redirect with success parameter
    }
}
