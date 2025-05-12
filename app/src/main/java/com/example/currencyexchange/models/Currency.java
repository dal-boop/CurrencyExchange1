    package com.example.currencyexchange.models;

    import com.google.gson.annotations.SerializedName;

    public class Currency {
        @SerializedName("code")
        public String code;

        @SerializedName("name")
        public String name;

        @SerializedName("strCode")
        public String strCode;
    }