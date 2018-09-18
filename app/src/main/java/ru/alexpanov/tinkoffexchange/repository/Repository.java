package ru.alexpanov.tinkoffexchange.repository;

import java.util.List;

import io.reactivex.Single;
import ru.alexpanov.tinkoffexchange.db.ConverterResult;
import ru.alexpanov.tinkoffexchange.db.Currency;

public interface Repository {
    Single<List<Currency>> getAllCurrencies();

    Single<ConverterResult> convert(Currency from, Currency to);
}
