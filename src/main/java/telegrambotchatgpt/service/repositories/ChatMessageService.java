package telegrambotchatgpt.service.repositories;

import org.telegram.telegrambots.meta.api.objects.Message;
import telegrambotchatgpt.entities.ChatMessage;

public interface ChatMessageService {
    void saveChatMessage(Message message);

    void saveGptResponseMessage(Long chatId, String message);

    void save(ChatMessage chatMessage);
}
