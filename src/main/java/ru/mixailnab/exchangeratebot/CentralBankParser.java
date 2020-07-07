package ru.mixailnab.exchangeratebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CentralBankParser {
    private static final Logger logger = LogManager.getLogger();
    Document doc;

    public CentralBankParser(String link) {
        try {
            logger.info("Trying to get page " + link);
            doc = Jsoup.connect(link).get();
            logger.info("Page was successfully received.");
        } catch (IOException e) {
            logger.error("Error was occurred when bot was getting " + link + " " + e.getMessage());
        }
    }

    public String getCentralBankRate(String currency) {
        if (doc.getElementsByTag("tbody").isEmpty()) {
            logger.warn("Central Bank has no information about this date.");
            return null;
        }
        logger.info("Trying to get info from Central Bank.");
        Element table = doc.getElementsByTag("tbody").get(0);
        Element button = doc.getElementsByClass("datepicker-filter_button").get(0);
        currency = currency.toUpperCase();
        String result = button.text() + "\n";
        for (var tmp : table.children()) {
            Elements tmpChildren = tmp.children();
            if (tmpChildren.get(1).text().equals(currency)) {
                result += tmpChildren.get(3).text() + " Курс: " + tmpChildren.get(4).text();
                logger.info("Info from Central Bank was successfully received.");
                return result;
            }
        }
        logger.warn("Central Bank has no information about this currency.");
        return null;
    }

}
