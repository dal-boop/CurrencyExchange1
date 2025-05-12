package com.example.currencyexchange.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TinkoffResponse {
    @SerializedName("payload")
    public Payload payload;

    public static class Payload {
        @SerializedName("rates")
        public List<CurrencyRate> rates;
    }
}