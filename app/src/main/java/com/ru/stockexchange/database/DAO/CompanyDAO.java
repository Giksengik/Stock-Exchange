package com.ru.stockexchange.database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ru.stockexchange.database.entities.Company;


import java.util.List;

import io.reactivex.rxjava3.core.Observable;


@Dao
public interface CompanyDAO {
    // ORDER BY ticker ASC значит, что мы возвращаем компании по алфавитному порядку тикеров
    @Query("SELECT * FROM companies")
    LiveData<List<Company>> getAll();

    @Query("SELECT * FROM companies WHERE ticker LIKE :ticker " + "LIMIT 1")
    Company findByTicker(String ticker);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Company> companies);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Company ... company);

    @Delete
    void delete(Company company);

    @Query("SELECT COUNT(*) FROM companies")
    int count();

    @Query("SELECT * FROM companies WHERE isFavourite = 'FAV' ")
    LiveData<List<Company>> getAllFavourite();

    @Update
    void update(Company company);
}
