package com.ru.stockexchange.api.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoricalPrices {
    @SerializedName("pagination")
    @Expose
    public Pagination pagination;
    @SerializedName("data")
    @Expose
    public List<HistoricalPrice> prices;
}