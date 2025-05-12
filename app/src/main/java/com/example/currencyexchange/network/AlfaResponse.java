package com.example.currencyexchange.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AlfaResponse {
    @SerializedName("data")
    public List<CurrencyBlock> data;

    public static class CurrencyBlock {
        @SerializedName("currencyCode")
        public String currencyCode;

        @SerializedName("rateByClientType")
        public List<RateByClientType> rateByClientType;
    }

    public static class RateByClientType {
        @SerializedName("clientType")
        public String clientType;

        @SerializedName("ratesByType")
        public List<RatesByType> ratesByType;
    }

    public static class RatesByType {
        @SerializedName("rateType")
        public String rateType;

        @SerializedName("lastActualRate")
        public LastActualRate lastActualRate;
    }

    public static class LastActualRate {
        @SerializedName("buy")
        public Value buy;
        @SerializedName("sell")
        public Value sell;
    }

    public static class Value {
        @SerializedName("originalValue")
        public double originalValue;
    }
}
