package com.ru.stockexchange.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ru.stockexchange.database.entities.Company;
import com.ru.stockexchange.database.entities.Request;

import java.util.List;

@Dao
public interface RequestDAO {

    @Query("SELECT * FROM companies")
    LiveData<List<Request>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRequest(Request request);

}
