package com.ru.stockexchange.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoricalPrice {
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("data")
    @Expose
    public String symbol;
    @SerializedName("open")
    @Expose
    public double openPrice;
    @SerializedName("high")
    @Expose
    public double highPrice;
    @SerializedName("low")
    @Expose
    public double lowPrice;
    @SerializedName("close")
    @Expose
    public double closePrice;
    @SerializedName("volume")
    @Expose
    public double volume;
}
