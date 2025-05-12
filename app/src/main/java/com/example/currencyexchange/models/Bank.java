package com.example.currencyexchange.models;

import java.util.List;

public class Bank {
    public String name;
    public int logoRes;
    public List<CurrencyRate> rates;
    public boolean isExpanded;

    public Bank(String name, int logoRes, List<CurrencyRate> rates) {
        this.name = name;
        this.logoRes = logoRes;
        this.rates = rates;
        this.isExpanded = false;
    }
}
