package ru.mixailnab.exchangeratebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BankParser {
    private static final String ODD_CLASSNAME = "productBank tr-turn tr-link  row body odd";
    private static final String EVEN_CLASSNAME = "productBank tr-turn tr-link  row body even";
    private static final Logger logger = LogManager.getLogger();
    private Document doc;

    public BankParser(String link) {
        try {
            logger.info("Trying to get page " + link);
            doc = Jsoup.connect(link).get();
            logger.info("Page was successfully received.");
        } catch (IOException e) {
            logger.error("Error was occurred when bot was getting " + link + " " + e.getMessage());
        }
    }

    public TreeMap<String, String> getHtmlInfo() {
        Element table = doc.getElementsByTag("tbody").get(1);
        Element firsttable = doc.getElementsByTag("tbody").get(0);
        if (firsttable.children().size() > 3) {
            logger.warn("Currency doesn't exchange there.");
            return null;
        }
        Elements tableChildren = table.select("tr[class=" + EVEN_CLASSNAME + "],tr[class=" + ODD_CLASSNAME + "]");

        TreeMap<String, String> map = new TreeMap<String, String>();
        for (var tmp : tableChildren) {
            Elements tmpChildren = tmp.children();
            map.put(tmpChildren.get(0).text() + ":\n", "Покупка: " + tmpChildren.get(1).text() + "Продажа: " + tmpChildren.get(2).text() + "\n");
        }
        return map;
    }

    public String getBankList() {
        TreeMap<String, String> map = getHtmlInfo();
        StringBuilder result = new StringBuilder();
        logger.info("Trying to get bank list.");
        try {
            Set set = map.entrySet();
            Iterator it = set.iterator();
            int counter = 0;
            while (it.hasNext() && counter < 5) {
                Map.Entry tmp = (Map.Entry) it.next();
                result.append(tmp.getKey()).append(tmp.getValue());
                counter++;
            }
            logger.info("Bank list was successfully received.");
            return String.valueOf(result);
        } catch (Exception e) {
            logger.error("Error was occurred when bot was getting bank list " + e.getMessage());
        }
        return null;
    }
}
