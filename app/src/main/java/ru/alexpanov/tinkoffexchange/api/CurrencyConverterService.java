package ru.alexpanov.tinkoffexchange.api;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyConverterService {

    @GET("currencies")
    Single<ResponseBody> getAllCurrencies();

    @GET("convert")
    Single<ResponseBody> convertCurrency(
            @Query("q") String query,
            @Query("compact") String compact
    );
}
