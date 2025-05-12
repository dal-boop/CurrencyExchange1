package com.example.currencyexchange.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SovcomApi {
    @GET("v1/currency/{kladrId}")
    Call<SovcomResponse> getRates(@Path("kladrId") String kladrId);
}
