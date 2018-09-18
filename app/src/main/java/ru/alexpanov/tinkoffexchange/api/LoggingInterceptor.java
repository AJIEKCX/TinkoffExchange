package ru.alexpanov.tinkoffexchange.api;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import ru.alexpanov.tinkoffexchange.BuildConfig;

import static okhttp3.logging.HttpLoggingInterceptor.Level;

public class LoggingInterceptor implements Interceptor {

    private HttpLoggingInterceptor interceptor;

    public LoggingInterceptor() {
        interceptor = new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        return interceptor.intercept(chain);
    }
}
