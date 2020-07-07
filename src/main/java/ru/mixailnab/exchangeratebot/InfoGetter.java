package ru.mixailnab.exchangeratebot;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class InfoGetter {
    private static final String DICTIONARY_PATH = "dictionary.json";
    private static final String CITIES_PATH = "cities.json";
    private static final String CURRENCIES_PATH = "currencies.json";
    private static final String CONFIG_PATH = "bot_config.json";
    private Map<String, String> mapForConfig;
    private Map<String, String> mapForLinks;
    private Map<String, String> mapForCityNames;
    private Map<String, String> mapForCurrencies;
    private static InfoGetter data;
    private static final Logger logger = LogManager.getLogger();

    private InfoGetter() {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        Gson gson = new Gson();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(CONFIG_PATH), StandardCharsets.UTF_8);
            logger.info("Info from " + CONFIG_PATH + " was successfully received.");
        } catch (IOException e) {
            logger.error("Error was occurred when " + CONFIG_PATH + "was getting: " + e.getMessage());
        }
        mapForConfig = gson.fromJson(result, Map.class);
        logger.info("Getting info from .json files.");
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(DICTIONARY_PATH), StandardCharsets.UTF_8);
            logger.info("Info from " + DICTIONARY_PATH + " was successfully received.");
        } catch (IOException e) {
            logger.error("Error was occurred when " + DICTIONARY_PATH + "was getting: " + e.getMessage());
        }
        mapForLinks = gson.fromJson(result, Map.class);
        result = "";
        logger.info("You're here.");
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(CITIES_PATH), StandardCharsets.UTF_8);
            logger.info("Info from " + CITIES_PATH + " was successfully received.");
        } catch (IOException e) {
            logger.error("Error was occurred when " + CITIES_PATH + "was getting: " + e.getMessage());
        }
        mapForCityNames = gson.fromJson(result, Map.class);
        result = "";
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(CURRENCIES_PATH), StandardCharsets.UTF_8);
            logger.info("Info from " + CURRENCIES_PATH + " was successfully received.");
        } catch (IOException e) {
            logger.error("Error was occurred when " + CURRENCIES_PATH + "was getting: " + e.getMessage());
        }
        mapForCurrencies = gson.fromJson(result, Map.class);
    }

    public static InfoGetter getInstance() {
        if (data == null) {
            data = new InfoGetter();
            return data;
        } else {
            return data;
        }
    }

    public Map<String, String> getBotConfig() {
        return mapForConfig;
    }

    public Map<String, String> getCitiesLinks() {
        return mapForLinks;
    }

    public String showCitiesList() {
        StringBuilder result = new StringBuilder();
        for (String tmp : mapForLinks.keySet()) {
            result.append(tmp).append("\n");
        }
        return String.valueOf(result);
    }

    public String showCurrenciesList() {
        return "Доллар,\nЕвро,\nФунт стерлингов,\nЮань,\nЙены";
    }

    public Map<String, String> getCitiesNames() {
        return mapForCityNames;
    }

    public Map<String, String> getCurrenciesNames() {
        return mapForCurrencies;
    }
}
