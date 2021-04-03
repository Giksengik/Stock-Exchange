package com.ru.stockexchange.api;


import com.ru.stockexchange.api.models.CompanyNews;
import com.ru.stockexchange.api.models.RequestResult;
import com.ru.stockexchange.api.models.StockQuote;
import com.ru.stockexchange.database.entities.Company;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface JSONPlaceHolderAPI {
    //Использую для каждого типа запросов отдельный ключ, чтобы обойти лимит
    String apiKey = "c19kgfn48v6r1n798980";
    String apiKey2 = "c1e1u6v48v6sjvgg0b2g";
    String apiKey3 = "c1e2t9v48v6sjvgg0vhg";
    String apiKey4 = "c1k75ja37fks18c34u10";

    @GET("/api/v1/quote?token=" + apiKey)
    Call<StockQuote> getStockQuoteWithSymbol(@Query("symbol") String symbol);

    @GET("/api/v1/stock/profile2?token=" + apiKey3)
    Call<Company> getCompanyProfileBySymbol(@Query("symbol") String symbol);

    @GET("/api/v1/search?token=" + apiKey2)
    Call<RequestResult> getNewCompaniesByRequest(@Query("q") String query);
    @GET("/company-news?token="+apiKey4)
    Call<List<CompanyNews>> getCompanyNews(@Query("symbol") String symbol, @Query("from") String from, @Query("to") String to);
}
