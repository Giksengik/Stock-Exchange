package com.ru.stockexchange.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockQuote {
    @SerializedName("c")
    @Expose
    public double currentPrice;

    @SerializedName("o")
    @Expose
    public double openPriceOfTheDay;

    @SerializedName("h")
    @Expose
    public double highestPriceOfTheDay;

    @SerializedName("l")
    @Expose
    public double lowestPriceOfTheDay;

    @SerializedName("pc")
    @Expose
    public double previousClosePrice;

    @Override
    public String toString() {
        return "StockQuote{" +
                "currentPrice=" + currentPrice +
                ", openPriceOfTheDay=" + openPriceOfTheDay +
                ", highestPriceOfTheDay=" + highestPriceOfTheDay +
                ", lowestPriceOfTheDay=" + lowestPriceOfTheDay +
                ", previousClosePrice=" + previousClosePrice +
                '}';
    }
}
