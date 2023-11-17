package telegrambotchatgpt.controllers.pages.main;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import telegrambotchatgpt.service.repositories.AppUserService;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/api/v1/main")
@RestController
public class MainPageController {

    private final AppUserService appUserService;

    @GetMapping("/my-role")
    public ResponseEntity<String> getAuthorizedAppUserRole() {
        return ResponseEntity.ok(appUserService.getAuthenticatedAppUser().getRole().name());
    }
}