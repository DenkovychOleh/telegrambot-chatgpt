package telegrambotchatgpt.service.repositories;

import org.telegram.telegrambots.meta.api.objects.User;
import telegrambotchatgpt.entities.AppUser;

public interface AppUserService {
    AppUser findByUsername(String username);

    void save(AppUser appUser);

    AppUser saveAppUserFromTelegramUser(User telegranUser);

    AppUser findOrSaveAppUser(User telegranUser);
}
