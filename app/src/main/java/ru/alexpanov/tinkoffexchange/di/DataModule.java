package ru.alexpanov.tinkoffexchange.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import ru.alexpanov.tinkoffexchange.BuildConfig;
import ru.alexpanov.tinkoffexchange.api.CurrencyConverterService;
import ru.alexpanov.tinkoffexchange.api.LoggingInterceptor;
import ru.alexpanov.tinkoffexchange.db.AppDatabase;
import ru.alexpanov.tinkoffexchange.repository.DefaultRepository;
import ru.alexpanov.tinkoffexchange.repository.Repository;

@Module
public class DataModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BuildConfig.ENDPOINT)
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    CurrencyConverterService provideService(Retrofit retrofit) {
        return retrofit.create(CurrencyConverterService.class);
    }

    @Provides
    @Singleton
    AppDatabase provideDataBase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "app_database")
                .build();
    }

    @Provides
    @Singleton
    Repository provideRepository(AppDatabase database, CurrencyConverterService service) {
        return new DefaultRepository(database, service);
    }
}
