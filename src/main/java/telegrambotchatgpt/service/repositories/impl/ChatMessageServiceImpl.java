package telegrambotchatgpt.service.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import telegrambotchatgpt.dao.ChatMessageDAO;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.entities.ChatMessage;
import telegrambotchatgpt.service.repositories.AppUserService;
import telegrambotchatgpt.service.repositories.ChatMessageService;

import java.time.*;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageDAO chatMessageDAO;

    private final AppUserService appUserService;


    @Override
    public void save(ChatMessage chatMessage) {
        chatMessageDAO.save(chatMessage);
    }

    @Transactional
    @Override
    public void saveChatMessage(Message message) {
        ChatMessage chatMessage = buildChatMessageFromMessage(message);
        save(chatMessage);
    }

    @Transactional
    @Override
    public void saveGptResponseMessage(Long chatId, String message) {
        ChatMessage chatMessage = buildChatMessageFromGptResponseMessage(chatId, message);
        save(chatMessage);
    }


    private ChatMessage buildChatMessageFromGptResponseMessage(Long chatId, String message) {
        return ChatMessage.builder()
                .chatId(chatId)
                .messageTime(LocalDateTime.now())
                .messageAuthor("gpt-3.5-turbo")
                .message(message)
                .build();
    }

    private ChatMessage buildChatMessageFromMessage(Message message) {
        return ChatMessage.builder()
                .user(getAppUserFromMessage(message))
                .chatId(message.getChatId())
                .messageTime(getMessageTimeFromMessage(message))
                .messageAuthor(getAppUserFromMessage(message).getUsername())
                .message(message.getText())
                .build();
    }

    private AppUser getAppUserFromMessage(Message message) {
        return appUserService.findOrSaveAppUser(message.getFrom());
    }

    private LocalDateTime getMessageTimeFromMessage(Message message) {
        Instant instant = getInstantFromMessageDate(message.getDate());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private Instant getInstantFromMessageDate(Integer date) {
        return Instant.ofEpochSecond(date);
    }
}
