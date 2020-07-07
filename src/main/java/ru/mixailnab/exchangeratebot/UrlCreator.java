package ru.mixailnab.exchangeratebot;

import java.util.Map;

public class UrlCreator {
    private static final String PAGE_URL = "https://cbr.ru/currency_base/daily/?UniDbQuery.Posted=True&UniDbQuery.To=replacement";
    private static final String REPLACE_MASK = "replacement";
    private Map<String, String> citiesLinks;

    public UrlCreator(Map<String, String> citiesLinks) {
        this.citiesLinks = citiesLinks;
    }

    public String createURL(String city, String currency) {
        if (city != null && currency != null) {
            return citiesLinks.get(city).replaceAll(REPLACE_MASK, currency);
        }
        return null;
    }

    public String createURLCentralBank(String date) {
        if (date != null) {
            return PAGE_URL.replaceAll(REPLACE_MASK, date);
        }
        return null;
    }
}
