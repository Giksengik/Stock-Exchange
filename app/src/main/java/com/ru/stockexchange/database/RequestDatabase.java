package com.ru.stockexchange.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ru.stockexchange.database.DAO.CompanyDAO;
import com.ru.stockexchange.database.DAO.RequestDAO;
import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.database.entities.Request;

@Database(entities = {Request.class}, version = 1, exportSchema = false)
public abstract class RequestDatabase extends RoomDatabase {
    public abstract RequestDAO requestDAO();
    private static RequestDatabase INSTANCE;

    public static RequestDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RequestDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RequestDatabase.class, "requestDatabase")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
