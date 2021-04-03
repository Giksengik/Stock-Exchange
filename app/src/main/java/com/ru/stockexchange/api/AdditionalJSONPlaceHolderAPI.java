package com.ru.stockexchange.api;

import com.ru.stockexchange.api.models.HistoricalPrices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdditionalJSONPlaceHolderAPI {
    String apiKey = "74f3b2cf790264cddbe5fc799ede0862";
    @GET("/v1/eod?access_key="+apiKey)
    Call<HistoricalPrices> getData(@Query("symbols") String symbol, @Query("date_from") String from,
                                   @Query("date_to") String to);
}
