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

    @PutMapping("/update-role/{status}")
    public ResponseEntity<String> updateRole(@PathVariable("status") String newRole) {
        try {
            AppUser authenticatedAppUser = appUserService.getAuthenticatedAppUser();
            AppUser.Roles role = AppUser.Roles.valueOf(newRole.toUpperCase());
            authenticatedAppUser.setRole(role);
            appUserService.save(authenticatedAppUser);
            return ResponseEntity.ok("User's role has been updated. New role " + role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role");
        }
    }


    @GetMapping("/my-authentication")
    public ResponseEntity<String> getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String s = "Authorities: " + authentication.getAuthorities();
        return ResponseEntity.ok(s);
    }
}