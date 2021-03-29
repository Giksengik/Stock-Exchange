package com.ru.stockexchange.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



    @Entity(tableName = "companies")
public class Request {
        @PrimaryKey
        @NonNull
        @SerializedName("ticker")
        @Expose
        public String requestString;

            public Request(@NonNull String requestString) {
                    this.requestString = requestString;
            }
    }
