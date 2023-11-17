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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageDAO chatMessageDAO;

    private final AppUserService appUserService;


    @Override
    public void save(ChatMessage chatMessage) {
        chatMessageDAO.save(chatMessage);
    }

    @Override
    public String getChatByUsername(String username) {
        AppUser appUser = appUserService.findByUsername(username);
        Optional<List<ChatMessage>> chatMessageByAppUser = chatMessageDAO.findAllByUser(appUser);
        List<ChatMessage> chatMessages = chatMessageByAppUser.orElse(Collections.emptyList());
        return buildChatString(chatMessages);
    }

    private String buildChatString(List<ChatMessage> chatMessages) {
        StringBuilder stringBuilder = new StringBuilder();
        chatMessages.forEach(chatMessage ->
                stringBuilder.append(String.format("%s %s: %s%n", chatMessage.getMessageTime(), chatMessage.getMessageAuthor(), chatMessage.getMessage()))
        );
        return stringBuilder.toString().isEmpty() ? "The chat is empty" : stringBuilder.toString();
    }

    @Transactional
    @Override
    public void saveChatMessage(Message message) {
        ChatMessage chatMessage = buildChatMessageFromMessage(message);
        save(chatMessage);
    }

    @Transactional
    @Override
    public void saveGptResponseMessage(Long chatId, String message, String author) {
        AppUser appUserByChatId = appUserService.findByChatId(chatId);
        ChatMessage chatMessage = buildChatMessageFromGptResponseMessage(appUserByChatId, message, author);
        save(chatMessage);
    }


    private ChatMessage buildChatMessageFromGptResponseMessage(AppUser appUser, String message, String author) {
        return ChatMessage.builder()
                .user(appUser)
                .messageTime(LocalDateTime.now())
                .messageAuthor(author)
                .message(message)
                .build();
    }

    private ChatMessage buildChatMessageFromMessage(Message message) {
        return ChatMessage.builder()
                .user(getAppUserFromMessage(message))
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
