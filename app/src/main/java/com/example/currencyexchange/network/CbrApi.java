package com.example.currencyexchange.network;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.ElementList;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface CbrApi {
    @GET("scripts/XML_daily.asp")
    Call<CbrResponse> getDailyRates();
}
