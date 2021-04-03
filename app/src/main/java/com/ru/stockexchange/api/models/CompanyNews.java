package com.ru.stockexchange.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyNews {
//    @SerializedName("category")
//    @Expose
    String category;
//    @SerializedName("datetime")
//    @Expose
    int datetime;
//    @SerializedName("headline")
//    @Expose
    String headline;
//    @SerializedName("id")
//    @Expose
    int id;
//    @SerializedName("image")
//    @Expose
    String image;
//    @SerializedName("related")
//    @Expose
    String related;
//    @SerializedName("source")
//    @Expose
    String source;
//    @SerializedName("summary")
//    @Expose
    String summary;
//    @SerializedName("url")
//    @Expose
    String url;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDatetime() {
        return datetime;
    }

    public void setDatetime(int datetime) {
        this.datetime = datetime;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
