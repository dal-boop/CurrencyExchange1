package com.example.currencyexchange.network.raiffeisen;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RaiffeisenApi {
    @GET("oapi/currency_rate/get/")
    Call<RaiffeisenResponse> getRates(
            @Query("source") String source,
            @Query("currencies") String currencies
    );
}
