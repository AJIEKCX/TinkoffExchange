package ru.alexpanov.tinkoffexchange.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM currency_table")
    Single<List<Currency>> getAllCurrency();

    @Query("SELECT * FROM currency_table WHERE name = :name")
    Single<Currency> getCurrencyByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertCurrencies(List<Currency> currencyList);
}
