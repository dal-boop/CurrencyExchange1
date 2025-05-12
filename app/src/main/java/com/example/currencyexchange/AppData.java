// AppData.java
package com.example.currencyexchange;

import com.github.mikephil.charting.data.Entry;
import com.example.currencyexchange.models.CurrencyRate;
import com.example.currencyexchange.models.CurrencyCbr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData {
    public static List<CurrencyCbr> cbrRates = null;
    public static List<CurrencyRate> tinkoffRates = null;

    public static Map<String, List<Entry>> historyCache = new HashMap<>();
    public static Map<Integer, List<String>> labelsCache = new HashMap<>();
}
