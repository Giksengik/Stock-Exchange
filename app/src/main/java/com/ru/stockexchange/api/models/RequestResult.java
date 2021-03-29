package com.ru.stockexchange.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ru.stockexchange.database.entities.Company;

import java.util.List;

public class RequestResult {
     @SerializedName("result")
     @Expose
     public List<Company> companies;

     @SerializedName("count")
     @Expose
     public int count;
}
