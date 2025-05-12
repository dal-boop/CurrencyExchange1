package com.example.currencyexchange.network.raiffeisen;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RaiffeisenResponse {
    public boolean success;
    public Data data;

    public static class Data {
        public List<RateBlock> rates;
    }

    public static class RateBlock {
        public String code;
        public Title title;
        public List<Exchange> exchange;
    }

    public static class Title {
        public String english;
        public String russian;
    }

    public static class Exchange {
        public String code;
        public Title title;
        public Rates rates;
    }

    public static class Rates {
        public RateDetail sell;
        public RateDetail buy;
    }

    public static class RateDetail {
        public double value;
        public int direction;
        public int multiplier;
    }
}
