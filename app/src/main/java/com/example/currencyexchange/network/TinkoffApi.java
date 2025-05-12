package com.example.currencyexchange.network;

import com.example.currencyexchange.models.TinkoffResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TinkoffApi {
    @GET("v1/currency_rates")
    Call<TinkoffResponse> getRates();
}
