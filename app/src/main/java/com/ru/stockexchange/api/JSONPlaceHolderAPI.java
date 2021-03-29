package com.ru.stockexchange.api;


import com.ru.stockexchange.api.models.RequestResult;
import com.ru.stockexchange.api.models.StockQuote;
import com.ru.stockexchange.database.entities.Company;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONPlaceHolderAPI {
    //Использую для каждого типа запросов отдельный ключ, чтобы обойти лимит
    String apiKey = "c19kgfn48v6r1n798980";
    String apiKey2 = "c1e1u6v48v6sjvgg0b2g";
    String apiKey3 = "c1e2t9v48v6sjvgg0vhg";

    @GET("/api/v1/quote?token="+apiKey)
    Call<StockQuote> getStockQuoteWithSymbol(@Query("symbol") String symbol);

    @GET("/api/v1/stock/profile2?token="+apiKey3)
    Call<Company> getCompanyProfileBySymbol(@Query("symbol") String symbol);

    @GET("/api/v1/search?token="+apiKey2)
    Call<RequestResult> getNewCompaniesByRequest(@Query("q") String query);
}
