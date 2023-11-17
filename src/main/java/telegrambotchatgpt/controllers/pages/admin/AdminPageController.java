package telegrambotchatgpt.controllers.pages.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telegrambotchatgpt.dto.AppUserDTO;
import telegrambotchatgpt.service.AdminPageService;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminPageController {

    private final AdminPageService adminPageService;

    @GetMapping("/my-info")
    public ResponseEntity<String> openSecurePage() {
        return ResponseEntity.ok("Hello from admin page");
    }

    @PutMapping("/username/{username}/update-role/{role}")
    public ResponseEntity<String> updateRole(
            @PathVariable(value = "username") String username,
            @PathVariable(value = "role") String newRole) {
        return adminPageService.updateRoleByUsername(username, newRole);
    }

    @GetMapping("/chat/username/{username}")
    public ResponseEntity<String> getChatByUsername(@PathVariable(value = "username") String username) {
        return adminPageService.getChatByUsername(username);
    }

    @GetMapping("/app-users")
    public ResponseEntity<List<AppUserDTO>> getAppUserList(@RequestParam(value = "role", required = false) String role) {
        return adminPageService.getAppUserListByRole(role);
    }

    @PostMapping("/send-message/{username}")
    public ResponseEntity<String> sendMessageToAppUser(@PathVariable(value = "username") String username, @RequestBody String message) {
        return adminPageService.sendMessageToAppUserByUsername(username, message);
    }

}