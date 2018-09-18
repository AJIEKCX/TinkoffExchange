package ru.alexpanov.tinkoffexchange.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "converter_result")
public class ConverterResult {

    @PrimaryKey
    @NonNull
    private String currencyKey;

    private double value;

    public ConverterResult( @NonNull String currencyKey, double value) {
        this.currencyKey = currencyKey;
        this.value = value;
    }

    @NonNull
    public String getCurrencyKey() {
        return currencyKey;
    }

    public double getValue() {
        return value;
    }

    public String getCurrencyFromText() {
        return currencyKey.split("_")[0];
    }

    public String getCurrencyToText() {
        return currencyKey.split("_")[1];
    }
}
