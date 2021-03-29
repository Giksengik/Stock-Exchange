package com.ru.stockexchange.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("limit")
    @Expose
    public int limit;
    @SerializedName("offset")
    @Expose
    public int offset;
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("total")
    @Expose
    public int total;
}
