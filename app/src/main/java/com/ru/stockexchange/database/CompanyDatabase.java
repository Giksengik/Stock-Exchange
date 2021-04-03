package com.ru.stockexchange.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.google.gson.internal.$Gson$Preconditions;
import com.ru.stockexchange.database.DAO.CompanyDAO;
import com.ru.stockexchange.database.entities.Company;

@Database(entities = {Company.class}, version = 1, exportSchema = false)
public abstract class CompanyDatabase extends RoomDatabase {
    public abstract CompanyDAO companyDAO();
    private static CompanyDatabase INSTANCE;

    public static CompanyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CompanyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CompanyDatabase.class, "companyDatabase")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                    if(INSTANCE.companyDAO().count() == 0){
                        INSTANCE.populateInitialData();
                    }
                }
            }
        }
        return INSTANCE;
    }
    private void populateInitialData() {
        new Thread(() -> {
            if (companyDAO().count() == 0) {
                Company[] companiesDowJones = new Company []{new Company("AXP","American Express","USD")
                        , new Company("AMGN","Amgen","USD"), new Company("BA","Boeing","USD"),
                         new Company("CVX","Chevron", "USD"),
                         new Company("AAPL","Apple","USD"),
                        new Company("KO", "Coca-Cola", "USD"),
                         new Company("INTC","Intel","USD"),
                        new Company("IBM","IBM","USD"), new Company("JNJ","Johnson & Johnson","USD"),
                        new Company("JPM","JP Morgan","USD"),
                        new Company("MRK","Merck&Co","USD"), new Company("MSFT","Microsoft","USD"),
                        new Company("NKE","Nike","USD"), new Company("HD","Home Depot","USD"),
                        new Company("PG","Procter&Gamble","USD"), new Company("CRM","Salesforce","USD"),
                        new Company("HON","Honeywell","USD"), new Company("TRV","Travelers","USD"),
                        new Company("UNH","United Health","USD"), new Company("WMT","Wallmart","USD"),
                        new Company("VZ","Verizon","USD"),new Company("WBA","Wallgreens Boots","USD"),
                        new Company("V","Visa A","USD"),

                };
                companyDAO().insertAll(companiesDowJones);
            }
        }).start();
    }}
