package telegrambotchatgpt.service.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import telegrambotchatgpt.dao.AppUserDAO;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.exceptions.DataNotFoundException;
import telegrambotchatgpt.service.repositories.AppUserService;

@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserDAO appUserDAO;

    @Override
    public AppUser findByUsername(String username) {
        return appUserDAO.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User with " + username));
    }

    @Override
    public void save(AppUser appUser) {
        appUserDAO.save(appUser);
    }

    @Transactional
    @Override
    public AppUser findOrSaveAppUser(User telegramUser) {
        return appUserDAO.findByUsername(telegramUser.getUserName())
                .orElseGet(() -> saveAppUserFromTelegramUser(telegramUser));
    }

    @Override
    public AppUser getAuthenticatedAppUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByUsername(username);
    }

    private AppUser saveAppUserFromTelegramUser(User user) {
        AppUser appUser = AppUser.builder()
                .telegramUserId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUserName())
                .role(AppUser.Roles.USER)
                .build();
        save(appUser);
        return appUser;
    }
}
