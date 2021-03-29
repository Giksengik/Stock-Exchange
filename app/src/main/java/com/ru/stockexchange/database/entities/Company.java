package com.ru.stockexchange.database.entities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

@Entity(tableName = "companies")
public  class Company implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("ticker")
    @Expose
    public String ticker;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("currency")
    @Expose
    public String currency;

    @SerializedName("exchange")
    @Expose
    public String exchange;

    @SerializedName("marketCapitalization")
    @Expose
    public double markerCapitalization;

    @SerializedName("weburl")
    @Expose
    public String webUrl;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("finnhubIndustry")
    @Expose
    public String finnhubIndustry;
    @SerializedName("logo")
    @Expose
    public String logoURL;

    public double price;
    public double priceChange;
    public String isFavourite;
    public String iconString;

    @Ignore
    @SerializedName("type")
    @Expose
    public String type;

    @Ignore
    @SerializedName("symbol")
    @Expose
    public String symbol;


    public Company(@NonNull String ticker, String name, String currency) {
        isFavourite = "FAV";
        this.ticker = ticker;
        this.currency = currency;
        this.name = name;
        this.price = 0;
        priceChange = 0;
    }
    public Company()
    {
        currency = "";
        priceChange = 0;
        isFavourite = "NOTFAV";
        price = 0;
        country = "";
        currency = "";
        webUrl = "";
        markerCapitalization = 0;

        ticker = null;
    }

    @NotNull
    @Override
    public String toString() {
        return "Company{" +
                "ticker='" + ticker + '\'' +
                ", country='" + country + '\'' +
                ", currency='" + currency + '\'' +
                ", exchange='" + exchange + '\'' +
                ", markerCapitalization=" + markerCapitalization +
                ", webUrl='" + webUrl + '\'' +
                ", finnhubIndustry='" + finnhubIndustry + '\'' +
                '}';
    }
}
