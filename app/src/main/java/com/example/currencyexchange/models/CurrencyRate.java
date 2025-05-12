package com.example.currencyexchange.models;

import com.google.gson.annotations.SerializedName;

public class CurrencyRate {
    @SerializedName("category")
    public String category;

    @SerializedName("fromCurrency")
    public Currency fromCurrency;

    @SerializedName("toCurrency")
    public Currency toCurrency;

    @SerializedName("buy")
    public double buy;

    @SerializedName("sell")
    public double sell;
}