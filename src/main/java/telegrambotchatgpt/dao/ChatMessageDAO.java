package telegrambotchatgpt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telegrambotchatgpt.entities.ChatMessage;

@Repository
public interface ChatMessageDAO extends JpaRepository<ChatMessage, Long> {
}
