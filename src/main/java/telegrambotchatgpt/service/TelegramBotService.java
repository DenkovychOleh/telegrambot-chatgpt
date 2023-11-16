package telegrambotchatgpt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambotchatgpt.config.BotConfig;

@Log4j
@RequiredArgsConstructor
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private final BotConfig config;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            processTextMessage(update);
        } else {
            sendMassage(update.getMessage().getChatId(), "Your message is not a text");
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMassage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        message.setParseMode("HTML");
        executeMessage(message);
    }

    public void processTextMessage(Update update) {
//        AppUser appUser = findOrSaveAppUser(update);
        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        User telegramUser = update.getMessage().getFrom();
        Long telegramUserId = telegramUser.getId();
        String userName = telegramUser.getUserName();


        String output = "Hello, " + userName;
        sendMassage(chatId, output);
    }

}
