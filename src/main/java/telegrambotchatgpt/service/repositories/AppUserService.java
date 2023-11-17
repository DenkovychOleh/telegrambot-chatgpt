package telegrambotchatgpt.service.repositories;

import org.telegram.telegrambots.meta.api.objects.User;
import telegrambotchatgpt.dto.AppUserDTO;
import telegrambotchatgpt.entities.AppUser;

import java.util.List;

public interface AppUserService {
    AppUser findByUsername(String username);

    void save(AppUser appUser);

    AppUser findOrSaveAppUser(User telegranUser);

    AppUser getAuthenticatedAppUser();

    AppUser findByChatId(Long chatId);

    void updateRoleByUsername(String username, String newRole);

    List<AppUserDTO> getAppUserListByRole(String role);

    List<AppUser> findAllByRole(AppUser.Roles roles);
}
