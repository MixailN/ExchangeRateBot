package ru.mixailnab.exchangeratebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class ExchangeRateBot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger();

    public ExchangeRateBot(DefaultBotOptions options) {
        super(options);
    }

    public void onUpdateReceived(Update update) {
        InfoGetter infoGetter = InfoGetter.getInstance();
        MessageParser messageParser = new MessageParser(infoGetter.getCitiesNames(), infoGetter.getCurrenciesNames());
        UrlCreator urlCreator = new UrlCreator(infoGetter.getCitiesLinks());
        if (update.getMessage() != null && update.getMessage().hasText()) {
            logger.info("Message was received.");
            String text = update.getMessage().getText();
            if (text.startsWith("/start")) {
                sendMessage(update.getMessage(), "Ну как там с деньгами? Я Михал Палыч Тереньтев, могу проконсультировать вас по денежному вопросу. Напишите /help, чтобы узнать список команд.");
                logger.info("Sent start message.");
                return;
            }
            if (text.startsWith("/help")) {
                sendMessage(update.getMessage(), "Чтобы узнать курс валют в местных банках введите сообщение, содержащее город и название валюты." +
                        "Также вы можете получить курс валюты в ЦБ по дате(например цб евро 20.12.2018). Прошу обратить внимание, что при вводе несуществующей даты, будет показана последняя дата по данным ЦБ.\n" +
                        "Список доступных городов и валют /city и /currency соответственно.");
                logger.info("Sent help message.");
                return;
            }
            if (text.startsWith("/city")) {
                sendMessage(update.getMessage(), "Доступные города: \n" + infoGetter.showCitiesList());
                logger.info("Sent list of cities.");
                return;
            }
            if (text.startsWith("/currency")) {
                sendMessage(update.getMessage(), "Доступные валюты: \n" + infoGetter.showCurrenciesList());
                logger.info("Sent list of currencies.");
                return;
            }
            String city = messageParser.findCity(update.getMessage().getText());
            String currency = messageParser.findCurrency(update.getMessage().getText());
            String answer = "";

            if (update.getMessage().isUserMessage() && (city == null || currency == null)) {
                if (city == null) {
                    answer += "Проверьте правильность написания названия города! Список доступных городов /city. \n";
                }
                if (currency == null) {
                    answer += "Проверьте правильность написания названия валюты! Список доступных валют /currency \n";
                }

            } else if (city.equals("ЦБ")) {
                String date = messageParser.findDate(update.getMessage().getText());
                if (date == null) {
                    answer = "Ошибка в указании даты. Введите существующую дату в формате dd.mm.yyyy";
                } else {
                    CentralBankParser centralBankParser = new CentralBankParser(urlCreator.createURLCentralBank(date));
                    answer = centralBankParser.getCentralBankRate(currency);
                    if (answer == null) {
                        answer = "Данную валюту не знали в то время! (" + date + ")";
                    }
                }
            } else {
                BankParser bankParser = new BankParser(urlCreator.createURL(city, currency));
                answer = bankParser.getBankList();
                if (answer == null) {
                    answer = "Такую валюту не обменивают в указанном городе! (" + city + ")";
                }
            }
            sendMessage(update.getMessage(), answer);
            logger.info("Sent answer message.");
        } else if (update.getMessage() != null && !update.getMessage().hasText() && update.getMessage().isUserMessage()) {
            sendMessage(update.getMessage(), "Я понимаю только текстовые сообщения!");
            logger.info("Sent answer message about wrong format");
        }
    }

    public void sendMessage(Message message, String text) {
        long chatId = message.getChatId();
        try {
            execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return InfoGetter.getInstance().getBotConfig().get("username");
    }

    public String getBotToken() {
        return InfoGetter.getInstance().getBotConfig().get("token");
    }
}
