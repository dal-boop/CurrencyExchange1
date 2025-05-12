package com.example.currencyexchange.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlfaApi {
    @GET("api/v1/scrooge/currencies/alfa-rates")
    Call<AlfaResponse> getRates(
            @Query("clientType.eq")            String clientType,
            @Query("currencyCode.in")          String currencyList,
            @Query("date.lte")                 String dateLte,
            @Query("lastActualForDate.eq")     boolean lastActual,
            @Query("rateType.in")              String rateTypes
    );
}
