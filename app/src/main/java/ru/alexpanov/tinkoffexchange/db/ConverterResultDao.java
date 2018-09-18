package ru.alexpanov.tinkoffexchange.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Single;

@Dao
public interface ConverterResultDao {

    @Query("SELECT * FROM converter_result WHERE currencyKey = :key")
    Single<ConverterResult> getConverterResultByKey(String key);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertResult(ConverterResult result);
}
