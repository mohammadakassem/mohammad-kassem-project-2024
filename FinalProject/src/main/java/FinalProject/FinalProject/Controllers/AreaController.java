package FinalProject.FinalProject.Controllers;

import FinalProject.FinalProject.dto.CreateAreaDTO;
import FinalProject.FinalProject.model.Area;
import FinalProject.FinalProject.service.AreaService;
import FinalProject.FinalProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/areas")
public class AreaController {

    @Autowired
    private AreaService parkingService;

    @Autowired
    private UserService userService;

    // Allow only users with ROLE_OWNER to create areas

    @PostMapping("/create")
    public String createArea(@ModelAttribute CreateAreaDTO createAreaDTO, Model model) {
        Area newArea = parkingService.createArea(
                createAreaDTO.getName(),
                createAreaDTO.getLocation(),
                createAreaDTO.getOpeningTime(),
                createAreaDTO.getClosingTime(),
                createAreaDTO.getImage(),
                createAreaDTO.getTotalSpots(),
                createAreaDTO.getDescription()
        );
        model.addAttribute("area", newArea);
        return "redirect:/areas"; // Redirect to the areas list after creation
    }

    // Allow only users with ROLE_OWNER to view the create form

    @GetMapping("/create")
    public String showCreateAreaForm(Model model) {
        model.addAttribute("createAreaDTO", new CreateAreaDTO());
        return "create_area"; // Ensure this matches your Thymeleaf template name
    }

    // Allow all authenticated users to view all areas
    @GetMapping// Adjusted to handle the GET request to /areas
    public String getAreas(Model model, Authentication authentication) {
        List<Area> areas = parkingService.getAllAreas();
        model.addAttribute("areas", areas);
        return "areas"; // Thymeleaf template name
    }

    // Allow all authenticated users to view a specific area by ID
    @GetMapping("/{id}")
    public Area getAreaById(@PathVariable UUID id) {
        return parkingService.getAreaById(id);
    }


    @PutMapping("/{id}")
    public String updateArea(@PathVariable UUID id, @ModelAttribute Area updatedArea) {
        parkingService.updateArea(id, updatedArea);
        return "redirect:/areas"; // Redirect to the areas list after update
    }

    // Allow only users with ROLE_OWNER to view the edit form

    @GetMapping("/edit/{id}")
    public String showEditAreaForm(@PathVariable UUID id, Model model) {
        Area area = parkingService.getAreaById(id);
        model.addAttribute("area", area);
        model.addAttribute("createAreaDTO", new CreateAreaDTO()); // Optional, if needed
        return "editarea"; // Thymeleaf template for editing area
    }

    // Allow only users with ROLE_OWNER to delete areas

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArea(@PathVariable UUID id) {
        parkingService.deleteArea(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // Allow only users with ROLE_OWNER to delete all areas

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAreas() {
        parkingService.deleteAllAreas();
        return ResponseEntity.noContent().build();
    }
}
