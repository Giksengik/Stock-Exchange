package com.ru.stockexchange.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Построение графика цен не получилось сделать через основной API, т.к. для этого нужна платная подписка
public class AdditionalNetworkService {
    private static final String BASE_URL = "http://api.marketstack.com/";
    private Retrofit mRetrofit;
    public AdditionalNetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public AdditionalJSONPlaceHolderAPI getJSONApi(){
        return mRetrofit.create(AdditionalJSONPlaceHolderAPI.class);
    }
}
