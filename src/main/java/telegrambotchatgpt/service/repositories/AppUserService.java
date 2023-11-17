package telegrambotchatgpt.service.repositories;

import org.springframework.security.core.context.SecurityContextHolder;
import org.telegram.telegrambots.meta.api.objects.User;
import telegrambotchatgpt.entities.AppUser;

public interface AppUserService {
    AppUser findByUsername(String username);

    void save(AppUser appUser);

    AppUser findOrSaveAppUser(User telegranUser);

    AppUser getAuthenticatedAppUser();

    AppUser findByChatId(Long chatId);

    void updateRoleByUsername(String username, String newRole);
}
