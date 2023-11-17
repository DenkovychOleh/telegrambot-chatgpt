package telegrambotchatgpt.service.repositories;

import org.telegram.telegrambots.meta.api.objects.Message;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.entities.ChatMessage;

import java.util.Optional;

public interface ChatMessageService {
    void saveChatMessage(Message message);

    void saveGptResponseMessage(Long chatId, String message, String author);

    void save(ChatMessage chatMessage);

    String getChatByUsername(String username);
}
