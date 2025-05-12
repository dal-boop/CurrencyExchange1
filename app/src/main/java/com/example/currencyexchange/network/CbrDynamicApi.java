// CbrDynamicApi.java
package com.example.currencyexchange.network;

import com.example.currencyexchange.adapters.FavoritesAdapter.CbrDynamicResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CbrDynamicApi {
    @GET("scripts/XML_dynamic.asp")
    Call<CbrDynamicResponse> getHistory(
            @Query("date_req1") String start,
            @Query("date_req2") String end,
            @Query("VAL_NM_RQ") String code
    );
}
