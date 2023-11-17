package telegrambotchatgpt.controllers.pages.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import telegrambotchatgpt.service.repositories.AppUserService;
import telegrambotchatgpt.service.repositories.ChatMessageService;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminPageController {

    private final ChatMessageService chatMessageService;
    private final AppUserService appUserService;

    @GetMapping("/my-info")
    public ResponseEntity<String> openSecurePage() {
        return ResponseEntity.ok("Hello from admin page");
    }

    @PutMapping("/username/{username}/update-role/{role}")
    public ResponseEntity<String> updateRole(
            @PathVariable(value = "username") String username,
            @PathVariable(value = "role") String newRole) {
        appUserService.updateRoleByUsername(username, newRole);
        return ResponseEntity.ok("User's role has been updated.");
    }

    @GetMapping("/chat/username/{username}")
    public ResponseEntity<String> getChatByUsername(@PathVariable(value = "username") String username) {
        return ResponseEntity.ok(chatMessageService.getChatByUsername(username));
    }

    @GetMapping("/chat-json/username/{username}")
    public ResponseEntity<String> getJsonChatByUsername(@PathVariable(value = "username") String username) {
        return ResponseEntity.ok(chatMessageService.getChatByUsername(username));
    }
}