package com.example.currencyexchange.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VtbApi {
    @GET("api/currencyrates/table/optimized")
    Call<VtbResponse> getRates(
            @Query("category") int category,
            @Query("type")     int type
    );
}
