package telegrambotchatgpt.service.repositories;

import org.telegram.telegrambots.meta.api.objects.Message;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.entities.ChatMessage;

public interface ChatMessageService {
    void saveChatMessage(Message message, AppUser appUser);

    void saveGptResponseMessage(AppUser chatId, String message, String author);

    void save(ChatMessage chatMessage);

    String getChatByAppUser(AppUser appUser);
}
