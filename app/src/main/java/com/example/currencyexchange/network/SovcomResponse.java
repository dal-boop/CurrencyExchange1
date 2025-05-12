package com.example.currencyexchange.network;

import com.google.gson.annotations.SerializedName;

public class SovcomResponse {
    @SerializedName("usd") public CurrencyPair usd;
    @SerializedName("eur") public CurrencyPair eur;
    @SerializedName("cny") public CurrencyPair cny;
    @SerializedName("gbp") public CurrencyPair gbp;
    @SerializedName("chf") public CurrencyPair chf;
    @SerializedName("aed") public CurrencyPair aed;
    @SerializedName("thb") public CurrencyPair thb;

    public static class CurrencyPair {
        @SerializedName("buy")  public double buy;
        @SerializedName("sell") public double sell;
    }
}
