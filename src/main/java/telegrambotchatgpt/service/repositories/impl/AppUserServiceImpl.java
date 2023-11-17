package telegrambotchatgpt.service.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import telegrambotchatgpt.dao.AppUserDAO;
import telegrambotchatgpt.dto.AppUserDTO;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.exceptions.DataNotFoundException;
import telegrambotchatgpt.exceptions.InvalidValueException;
import telegrambotchatgpt.service.repositories.AppUserService;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<AppUser> findAllByRole(AppUser.Roles role) {
        return appUserDAO.findAllByRole(role)
                .filter(appUser -> !appUser.isEmpty())
                .orElseThrow(() -> new DataNotFoundException("Users with " + role.name()));
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

    @Transactional
    @Override
    public void updateRoleByUsername(String username, String newRole) {
        try {
            AppUser appUser = findByUsername(username);
            AppUser.Roles role = AppUser.Roles.valueOf(newRole.toUpperCase());
            appUser.setRole(role);
            save(appUser);
        } catch (IllegalArgumentException e) {
            throw new InvalidValueException("Invalid role");
        }
    }

    @Override
    public List<AppUserDTO> getAppUserListByRole(String role) {
        List<AppUser> appUsers;

        if (role != null) {
            AppUser.Roles appUserStatus = parseUserRole(role);
            appUsers = findAllByRole(appUserStatus);
        } else {
            appUsers = appUserDAO.findAll();
        }

        return appUsers.stream()
                .map(this::buildAppUserDTOFromAppUser)
                .collect(Collectors.toList());
    }


    private AppUser.Roles parseUserRole(String role) {
        try {
            return AppUser.Roles.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidValueException("Invalid role");
        }
    }

    private AppUserDTO buildAppUserDTOFromAppUser(AppUser appUser) {
        return AppUserDTO.builder()
                .id(appUser.getId())
                .username(appUser.getUsername())
                .role(appUser.getRole().name())
                .status(appUser.getStatus().name())
                .build();
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
