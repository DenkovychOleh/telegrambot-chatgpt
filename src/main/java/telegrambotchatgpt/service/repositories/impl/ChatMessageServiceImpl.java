package telegrambotchatgpt.service.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import telegrambotchatgpt.dao.ChatMessageDAO;
import telegrambotchatgpt.entities.AppUser;
import telegrambotchatgpt.entities.ChatMessage;
import telegrambotchatgpt.service.repositories.ChatMessageService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageDAO chatMessageDAO;


    @Override
    public void save(ChatMessage chatMessage) {
        chatMessageDAO.save(chatMessage);
    }

    @Override
    public String getChatByAppUser(AppUser appUser) {
        Optional<List<ChatMessage>> chatMessageByAppUser = chatMessageDAO.findAllByUser(appUser);
        List<ChatMessage> chatMessages = chatMessageByAppUser.orElse(Collections.emptyList());
        return buildChatString(chatMessages);
    }

    @Transactional
    @Override
    public void saveChatMessage(Message message, AppUser appUser) {
        ChatMessage chatMessage = buildChatMessageFromMessage(message, appUser);
        save(chatMessage);
    }

    @Transactional
    @Override
    public void saveGptResponseMessage(AppUser appUser, String message, String author) {
        ChatMessage chatMessage = buildChatMessageFromGptResponseMessage(appUser, message, author);
        save(chatMessage);
    }


    private String buildChatString(List<ChatMessage> chatMessages) {
        StringBuilder stringBuilder = new StringBuilder();
        chatMessages.forEach(chatMessage ->
                stringBuilder.append(String.format("%s %s: %s%n", chatMessage.getMessageTime(), chatMessage.getMessageAuthor(), chatMessage.getMessage()))
        );
        return stringBuilder.toString().isEmpty() ? "The chat is empty" : stringBuilder.toString();
    }

    private ChatMessage buildChatMessageFromGptResponseMessage(AppUser appUser, String message, String author) {
        return ChatMessage.builder()
                .user(appUser)
                .messageTime(LocalDateTime.now())
                .messageAuthor(author)
                .message(message)
                .build();
    }

    private ChatMessage buildChatMessageFromMessage(Message message, AppUser appUser) {
        return ChatMessage.builder()
                .user(appUser)
                .messageTime(getMessageTimeFromMessage(message))
                .messageAuthor(appUser.getUsername())
                .message(message.getText())
                .build();
    }

    private LocalDateTime getMessageTimeFromMessage(Message message) {
        Instant instant = getInstantFromMessageDate(message.getDate());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    private Instant getInstantFromMessageDate(Integer date) {
        return Instant.ofEpochSecond(date);
    }
}
