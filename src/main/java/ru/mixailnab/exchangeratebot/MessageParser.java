package ru.mixailnab.exchangeratebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    private static final Logger logger = LogManager.getLogger();
    private String currency;
    private String city;
    private Map<String, String> cities;
    private Map<String, String> currencies;

    public MessageParser(Map<String, String> cities, Map<String, String> currencies) {
        this.city = null;
        this.currency = null;
        this.cities = cities;
        this.currencies = currencies;
    }

    public String findCity(String message) {
        message = message.toLowerCase();
        String[] words = message.split(" ");
        logger.info("Trying to find city in the dictionary.");
        for (String key : cities.keySet()) {
            for (String word : words) {
                if (word.startsWith(key)) {
                    city = cities.get(key);
                    logger.info("City was successfully found");
                    return city;
                }
            }
        }
        logger.warn("City wasn't found");
        return null;
    }

    public String findCurrency(String message) {
        message = message.toLowerCase();
        String[] words = message.split(" ");
        logger.info("Trying to find currency in the dictionary.");
        for (String key : currencies.keySet()) {
            for (String word : words) {
                if (word.startsWith(key)) {
                    currency = currencies.get(key);
                    logger.info("Currency was successfully found");
                    return currency;
                }
            }
        }
        logger.info("Currency wasn't found");
        return null;
    }

    public String findDate(String message) {
        String regex = "\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        logger.info("Trying to find date.");
        if(matcher.find()) {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date = format.parse(matcher.group());
                System.out.println(matcher.group() + "  " + format.format(date));
                if(matcher.group().equals(format.format(date))) {
                    logger.info("Date was successfully found.");
                    return format.format(date);
                }
            } catch (ParseException e) {
                logger.error("Error was occurred when date was parsing: " + e.getMessage());
            }
        }
        logger.warn("Date wasn't found.");
        return null;
    }

    public String getCity() {
        return city;
    }

    public String getCurrency() {
        return currency;
    }
}
