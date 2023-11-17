package telegrambotchatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import telegrambotchatgpt.dto.AppUserDTO;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.service.repositories.AppUserService;
import telegrambotchatgpt.service.repositories.ChatMessageService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminPageService {

    private final AppUserService appUserService;

    private final ChatMessageService chatMessageService;

    private final TelegramBotService telegramBotService;

    public ResponseEntity<String> updateRoleByUsername(String username, String newRole) {
        appUserService.updateRoleByUsername(username, newRole);
        return ResponseEntity.ok("AppUser's role has been updated.");
    }

    public ResponseEntity<String> getChatByUsername(String username) {
        AppUser appUser = appUserService.findByUsername(username);
        return ResponseEntity.ok(chatMessageService.getChatByAppUser(appUser));
    }

    public ResponseEntity<List<AppUserDTO>> getAppUserListByRole(String role) {
        return ResponseEntity.ok(appUserService.getAppUserListByRole(role));
    }

    public ResponseEntity<String> sendMessageToAppUserByUsername(String username, String message) {
        AppUser appUser = appUserService.findByUsername(username);
        AppUser admin = appUserService.getAuthenticatedAppUser();
        String author = String.format("[ADMIN] %s", admin.getUsername());
        System.out.println(message);
        telegramBotService.processTextMessageByAppUser(appUser, author, message);
        return ResponseEntity.ok("The message has been sent");
    }
}
