package ru.alexpanov.tinkoffexchange.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "currency_table")
public class Currency {

    @NonNull
    @PrimaryKey
    private String name;

    public Currency(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Currency)) return false;
        Currency currency = (Currency) obj;

        return currency.name.equals(name);
    }
}
