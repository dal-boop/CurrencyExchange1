package com.example.currencyexchange.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TvoiBankResponse {
    @SerializedName("message")
    public String message;
    @SerializedName("reresult")
    public List<CurrencyItem> reresult;

    public static class CurrencyItem {
        @SerializedName("intCode")
        public String intCode;

        @SerializedName("strCode")
        public String strCode;

        @SerializedName("nameCurrency")
        public String nameCurrency;

        @SerializedName("value")
        public double value;

        @SerializedName("dateTimeUpdata")
        public String dateTimeUpdata;
    }
}
