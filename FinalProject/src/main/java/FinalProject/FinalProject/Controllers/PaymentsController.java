package FinalProject.FinalProject.Controllers;

import FinalProject.FinalProject.model.Payment;
import FinalProject.FinalProject.service.PaymentsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/payments")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentService;

    @GetMapping("/user")
    public String redirectToShowPayments(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");

        // Check if user ID is null
        if (userId == null) {
            return "redirect:/error"; // Redirect to a specific error page
        }

        // Redirect to the show method with userId
        return "redirect:/payments/show?userId=" + userId;
    }

    @GetMapping("/show")
    public String showPaymentsByUserId(@RequestParam UUID userId, Model model) {
        List<Payment> payments = paymentService.getPaymentsByUserId(userId);

        // Calculate the total sum of payments
        double totalPayments = payments.stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        model.addAttribute("payments", payments); // Add payments to the model
        model.addAttribute("totalPayments", totalPayments); // Add the total sum to the model

        return "payments"; // Return the Thymeleaf view that will display the payments
    }


}
