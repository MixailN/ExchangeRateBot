package ru.mixailnab.exchangeratebot;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.util.ArrayList;
import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        logger.info("Bot is starting.");
        ApiContextInitializer.init();
        Main m = new Main();

        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        ExchangeRateBot exchangeRateBot = new ExchangeRateBot(botOptions);

        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(exchangeRateBot);
            logger.info("Bot successfully started");
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
