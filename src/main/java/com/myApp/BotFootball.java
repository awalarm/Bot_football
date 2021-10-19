package com.myApp;

import lombok.SneakyThrows;

import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


public class BotFootball extends TelegramLongPollingBot {

    public static void main(String[] args) throws TelegramApiException {
        BotFootball bot = new BotFootball();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }
    
    String fileName = "sum.txt";
    File pathFile = new File("./sum.txt");
    long lastModifiedDateFile = TimeUnit.MILLISECONDS.toDays(pathFile.lastModified());

    long unixTime = System.currentTimeMillis();
    long unixTimeInDay = TimeUnit.MILLISECONDS.toDays(unixTime);

    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {

        if (lastModifiedDateFile != unixTimeInDay) {
            try (PrintWriter out = new PrintWriter(fileName)) {
                out.println(0);
            }
        }

        String readFile =  readUsingFiles(fileName).replace("\n", "").replace("\r", "");


        Integer sum = Integer.valueOf(readFile);
        if (sum < 0) {
            sum = 0;
        }

        if(update.hasMessage()){
            Message message = update.getMessage();
            String text = message.getText();
            int plusPlayers = 0;

            for (int i = 0; i < text.length(); i++) {
                if(text.charAt(i) == '+') {
                    plusPlayers ++;
                    sum++;
                }

                if(text.charAt(i) == '-') {
                    sum--;
                    if (sum < 0) {
                        sum = 0;
                    }
                    plusPlayers ++;
                }
            }

            try (PrintWriter out = new PrintWriter(fileName)) {
                out.println(sum);
            }

            if (message.hasText() && plusPlayers > 0) {
                execute(
                    SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text("Go to football: " + String.valueOf(sum))
                    .build());
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "@FreebiecomeBot";
    }

    @Override
    public String getBotToken() {
        return "559077433:AAHlWXMBf7tYHWJhejSYTzbcXqVJOmoACt0";
    }




}

