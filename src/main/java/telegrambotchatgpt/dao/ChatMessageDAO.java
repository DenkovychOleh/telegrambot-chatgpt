package telegrambotchatgpt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.entities.ChatMessage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageDAO extends JpaRepository<ChatMessage, Long> {
    Optional<List<ChatMessage>> findAllByUser(AppUser appUser);
}
