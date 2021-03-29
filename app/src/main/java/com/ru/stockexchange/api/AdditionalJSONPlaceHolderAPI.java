package com.ru.stockexchange.api;

import com.ru.stockexchange.api.models.HistoricalPrices;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdditionalJSONPlaceHolderAPI {
    String apiKey = "33f8f8a330680897bb77996c930e337b";
    @GET("/v1/eod?access_key="+apiKey)
    Call<HistoricalPrices> getData(@Query("symbols") String symbol, @Query("date_from") String from,
                                   @Query("date_to") String to);
}
