package telegrambotchatgpt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegrambotchatgpt.entities.AppUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByTelegramUserId(Long chatId);

    Optional<List<AppUser>> findAllByRole(AppUser.Roles roles);

    List<AppUser> findAll();
}
