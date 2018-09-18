package ru.alexpanov.tinkoffexchange.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Currency.class, ConverterResult.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    public abstract CurrencyDao currencyDao();

    public abstract ConverterResultDao converterResultDao();
}
