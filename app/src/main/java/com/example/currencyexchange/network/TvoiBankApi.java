package com.example.currencyexchange.network;

import retrofit2.Call;
import retrofit2.http.POST;

public interface TvoiBankApi {
    @POST("api/v1/currency.get")
    Call<TvoiBankResponse> getRates();
}
