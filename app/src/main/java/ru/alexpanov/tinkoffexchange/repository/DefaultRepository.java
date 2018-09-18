package ru.alexpanov.tinkoffexchange.repository;

import android.arch.persistence.room.EmptyResultSetException;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import ru.alexpanov.tinkoffexchange.api.CurrencyConverterService;
import ru.alexpanov.tinkoffexchange.db.AppDatabase;
import ru.alexpanov.tinkoffexchange.db.ConverterResult;
import ru.alexpanov.tinkoffexchange.db.Currency;
import ru.alexpanov.tinkoffexchange.utils.TextUtils;

public class DefaultRepository implements Repository {

    private AppDatabase database;
    private CurrencyConverterService service;

    public DefaultRepository(AppDatabase database, CurrencyConverterService service) {
        this.database = database;
        this.service = service;
    }

    @Override
    public Single<List<Currency>> getAllCurrencies() {
        return service.getAllCurrencies()
                .map(ResponseBody::string)
                .map(JSONObject::new)
                .map(jsonObject -> jsonObject.getJSONObject("results"))
                .toObservable()
                .flatMap(jsonObject -> Observable.fromIterable(new JsonIterable(jsonObject)))
                .map(Currency::new)
                .collect((Callable<List<Currency>>) ArrayList::new, List::add)
                .onErrorResumeNext(throwable -> database.currencyDao().getAllCurrency())
                .flatMap(currencies -> currencies.size() > 0 ? Single.just(currencies) : Single.error(new EmptyResultSetException("Result is empty")))
                .doOnSuccess(database.currencyDao()::insertCurrencies);
    }

    @Override
    public Single<ConverterResult> convert(Currency from, Currency to) {
        String query = TextUtils.currencyKeyFormat(from, to);

        Observable<JSONObject> jsonObjectObservable = service.convertCurrency(query, "ultra")
                .map(ResponseBody::string)
                .map(JSONObject::new)
                .toObservable();

        Observable<String> keyObservable = jsonObjectObservable.
                flatMap(jsonObject -> Observable.fromIterable(new JsonIterable(jsonObject)));

        return Observable.combineLatest(jsonObjectObservable, keyObservable,
                (jsonObject, key) -> new ConverterResult(key, jsonObject.getDouble(key)))
                .singleOrError()
                .onErrorResumeNext(database.converterResultDao().getConverterResultByKey(query))
                .doOnSuccess(database.converterResultDao()::insertResult);
    }


    private static class JsonIterable implements Iterable<String> {

        private JSONObject jsonObject;

        JsonIterable(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @NonNull
        @Override
        public Iterator<String> iterator() {
            return jsonObject.keys();
        }
    }
}
