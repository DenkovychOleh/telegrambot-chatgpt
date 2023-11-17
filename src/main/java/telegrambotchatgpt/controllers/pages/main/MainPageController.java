package telegrambotchatgpt.controllers.pages.main;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.service.repositories.AppUserService;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/api/v1/main")
@RestController
public class MainPageController {

    private final AppUserService appUserService;

    @GetMapping()
    public ResponseEntity<String> openSecurePage() {
        return ResponseEntity.ok("Hello from secure page");
    }

    @GetMapping("/my-role")
    public ResponseEntity<String> getRole() {
        return ResponseEntity.ok(appUserService.getAuthenticatedAppUser().getRole().name());
    }
}