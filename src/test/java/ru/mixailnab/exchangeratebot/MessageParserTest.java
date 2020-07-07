package ru.mixailnab.exchangeratebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class MessageParserTest {
    private static final Logger logger = LogManager.getLogger();
    private MessageParser messageParser = new MessageParser(InfoGetter.getInstance().getCitiesNames(), InfoGetter.getInstance().getCurrenciesNames());
    @Test
    public void test_findCitySimple() {
        Assert.assertEquals("Москва", messageParser.findCity("москва"));
    }

    @Test
    public void test_findCityShort() {
        Assert.assertEquals("Москва", messageParser.findCity("мск"));
    }

    @Test
    public void test_findCityAdjective() {
        Assert.assertEquals("Москва", messageParser.findCity("московский"));
    }

    @Test
    public void test_findCityEmpty() {
        Assert.assertNull(messageParser.findCity(""));
    }

    @Test
    public void test_findCityWrong() {
        Assert.assertNull(messageParser.findCity("канальчик"));
    }

    @Test
    public void test_findCurrencySimple() {
        Assert.assertEquals("eur", messageParser.findCurrency("хочу eвро"));
    }

    @Test
    public void test_findCurrencyNotSimple() {
        Assert.assertEquals("gbp", messageParser.findCurrency("фунтики"));
    }

    @Test
    public void test_findCurrencyStrange() {
        Assert.assertEquals("gbp", messageParser.findCurrency("немного стерлингов"));
    }

    @Test
    public void test_findCurrencyWrong() {
        Assert.assertNull(messageParser.findCurrency("гиена"));
    }

    @Test
    public void test_findCurrencyEmpty() {
        Assert.assertNull(messageParser.findCurrency(""));
    }

    @Test
    public void test_findDate() {
        Assert.assertEquals("20.12.2019",messageParser.findDate("20.12.2019"));
    }

    @Test
    public void test_findDateWrongFormat() {
        Assert.assertNull(messageParser.findDate("20.12.219"));
    }

    @Test
    public void test_findDateWrongDate() {
        Assert.assertNull(messageParser.findDate("35.12.2019"));
    }
}