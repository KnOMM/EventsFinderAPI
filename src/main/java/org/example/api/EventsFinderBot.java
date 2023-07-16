package org.example.api;

import org.example.model.Event;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EventsFinderBot extends TelegramLongPollingBot {

    private final String greetings = "WELLCOME to events finder bot!!!";

    private final String usecase = "Click on the searching option";
    private static int state;
    private static String size;

    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = update.getMessage().getChatId();

        // We check if the update has a message and the message has text
        if (update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("Back")) {
            entryKeyboard(chatId.toString(), greetings);
        } else if (update.getMessage().getText().equals("Latest date events")) {
            EventsFinderBot.state = 1;
            SendMessage ask = new SendMessage();
            ask.setChatId(chatId.toString());
            ask.setText("Enter a number of events you want to see");
            try {
                execute(ask);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.getMessage().getText().equals("Popular events")) {
            EventsFinderBot.state = 2;
            SendMessage ask = new SendMessage();
            ask.setChatId(chatId.toString());
            ask.setText("Enter a number of events you want to see");
            try {
                execute(ask);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.getMessage().getText().equals("Nearest events")) {
            EventsFinderBot.state = 3;
            SendMessage ask = new SendMessage();
            ask.setChatId(chatId.toString());
            ask.setText("Enter a number of events you want to see");
            try {
                execute(ask);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (EventsFinderBot.state == 1 && isAllDigits(update.getMessage().getText())) {
            sendMessage(chatId.toString(), "date,desc", update.getMessage().getText());
            EventsFinderBot.state = 0;
        } else if (EventsFinderBot.state == 2 && isAllDigits(update.getMessage().getText())) {
            sendMessage(chatId.toString(), "relevance,desc", update.getMessage().getText());
            EventsFinderBot.state = 0;
        } else if (EventsFinderBot.state == 3 && isAllDigits(update.getMessage().getText())) {
            sendMessage(chatId.toString(), "date,asc", update.getMessage().getText());
            EventsFinderBot.state = 0;
        } else {
            entryKeyboard(chatId.toString(), usecase);
        }


    }

    public boolean isAllDigits(String str) {
        return str.matches("\\d+");
    }

    public void entryKeyboard(String chatId, String words) {
        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row;

        row = new KeyboardRow();
        row.add("Latest date events");
        keyboardRowList.add(row);

        row = new KeyboardRow();
        row.add("Popular events");
        keyboardRowList.add(row);

        row = new KeyboardRow();
        row.add("Nearest events");
        keyboardRowList.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(words);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String sort, String size) {
        SendPhoto sendPhoto = new SendPhoto();

        sendPhoto.setChatId(chatId);

        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(ActionType.TYPING);
        int i = 1;
        try {
            for (Event event : CallAPI.findEvents(sort, size)) {
                String eventString = i++ + ")\n" + event.toString();

                sendPhoto.setPhoto(new InputFile(event.getImg()));
                sendPhoto.setCaption(eventString);
                execute(sendChatAction);

                execute(sendPhoto);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return "EventsFinderBot";
    }

    @Override
    public String getBotToken() {
        Properties properties = new Properties();
        try {
//            properties.load(new FileInputStream("src/main/resources/credentials.properties"));
            properties.load(new FileInputStream("target/credentials.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("apiToken");
    }
}
