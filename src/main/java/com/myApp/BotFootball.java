package com.myApp;

import au.com.bytecode.opencsv.CSVWriter;
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

    @SneakyThrows
    public static void main(String[] args) throws TelegramApiException {
        BotFootball bot = new BotFootball();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);

    }

    VotingResults votingResult = new VotingResults();

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


        Integer totalFootballers = Integer.valueOf(readFile);
        if (totalFootballers < 0) {
            totalFootballers = 0;
        }

        if(update.hasMessage()){
            Message message = update.getMessage();
            String text = message.getText();
            String userFirstName = message.getFrom().getFirstName();
            Long userId = message.getFrom().getId();
            int plusPlayers = 0;
            // We remove the player if he changes his mind to play
            int linePlayerInList = votingResult.searchRejectedPlayer(String.valueOf(userId));

            for (int i = 0; i < text.length(); i++) {
                if(text.charAt(i) == '+') {
                    plusPlayers ++;
                    totalFootballers++;
                    votingResult.addVotingResults(userId +", "+ plusPlayers);
                }

                if(text.charAt(i) == '-') {

                    if (linePlayerInList != -1) {
                        votingResult.deletePlayer(linePlayerInList);

                        totalFootballers--;
                        if (totalFootballers < 0) {
                            totalFootballers = 0;
                        }
                        plusPlayers ++;
                    }
                }
            }

            try (PrintWriter out = new PrintWriter(fileName)) {
                out.println(totalFootballers);
            }

            if (linePlayerInList == -1) {
                execute(
                        SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text(userFirstName + ", хватит ставить минусы!!!")
                        .build());
                return;
            }

            if (message.hasText() && plusPlayers > 0) {
                if (totalFootballers == 0) {
                    execute(
                            SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text("На футбол никто не идет! ")
                            .build());
                } else {
                    execute(
                            SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text("На футбол идут: " + String.valueOf(totalFootballers) + " " + String.format(userFirstName))
                            .build());
                }
            }
        }
    }

    private static final String BOT_NAME = System.getenv("username");

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    private static final String BOT_TOKEN = System.getenv("TOKEN");

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}

