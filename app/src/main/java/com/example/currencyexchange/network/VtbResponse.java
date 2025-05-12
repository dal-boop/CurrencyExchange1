package com.example.currencyexchange.network;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VtbResponse {
    @SerializedName("category")
    public Category category;

    @SerializedName("dateFrom")
    public String dateFrom;

    @SerializedName("rates")
    public List<Rate> rates;

    public static class Category {
        public int id;
        public String name;
        public String description;
        public String target;
    }

    public static class Rate {
        @SerializedName("currency1")
        public Currency currency1;

        @SerializedName("currency2")
        public Currency currency2;

        public int scale;
        public double bid;
        public double offer;
        public String tooltip;
        public Object offerTrend;
        public Object bidTrend;
    }

    public static class Currency {
        public String code;
        public String rusName;
        public String symbol;
    }
}
