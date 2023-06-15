package org.example.api;

import org.example.model.Event;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EventsFinderBot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText() && !update.getMessage().getText().equals("1")) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            Long chatId = update.getMessage().getChatId();
            SendPhoto sendPhoto = new SendPhoto();

            message.setChatId(chatId.toString());
            sendPhoto.setChatId(chatId.toString());

            SendChatAction sendChatAction = new SendChatAction();
            sendChatAction.setChatId(chatId.toString());
            sendChatAction.setAction(ActionType.TYPING);
            try {
                for (Event event : CallAPI.findEvents()) {
                    String eventString = event.toString();

                    sendPhoto.setPhoto(new InputFile(event.getImg()));
                    sendPhoto.setCaption(eventString);


//                    message.setText(eventString);
                    execute(sendChatAction);
//                    execute(message);
                    execute(sendPhoto);
                }
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
//            sendCustomKeyboard(update.getMessage().getChatId().toString());
            sendInlineKeyboard(update.getMessage().getChatId().toString());
        }
    }

    public void sendCustomKeyboard(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Custom message text");

        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Row 1 Button 1");
        row.add("Row 1 Button 2");
        row.add("Row 1 Button 3");
        // Add the first row to the keyboard
        keyboard.add(row);
        // Create another keyboard row
        row = new KeyboardRow();
        // Set each button for the second line
        row.add("Row 2 Button 1");
        row.add("Row 2 Button 2");
        row.add("Row 2 Button 3");
        // Add the second row to the keyboard
        keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);

        try {
            // Send the message
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendInlineKeyboard(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Inline model below.");

        // Create InlineKeyboardMarkup object
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        // Create the keyboard (list of InlineKeyboardButton list)
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        // Create a list for buttons
        List<InlineKeyboardButton> Buttons = new ArrayList<InlineKeyboardButton>();
        // Initialize each button, the text must be written
        InlineKeyboardButton youtube = new InlineKeyboardButton("youtube");
        // Also must use exactly one of the optional fields,it can edit  by set method
        youtube.setUrl("https://www.youtube.com");
        // Add button to the list
        Buttons.add(youtube);
        // Initialize each button, the text must be written
        InlineKeyboardButton github = new InlineKeyboardButton("github");
        // Also must use exactly one of the optional fields,it can edit  by set method
        github.setUrl("https://github.com");
        // Add button to the list
        Buttons.add(github);
        keyboard.add(Buttons);
        inlineKeyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            // Send the message
            execute(message);
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
            properties.load(new FileInputStream("src/main/resources/credentials.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("apiToken");
    }
}
