package telegrambotchatgpt.controllers.pages.admin;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminPageController {

    @GetMapping("/my-info")
    public ResponseEntity<String> openSecurePage() {
        return ResponseEntity.ok("Hello from admin page");
    }
}