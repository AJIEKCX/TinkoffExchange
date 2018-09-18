package ru.alexpanov.tinkoffexchange;

import android.app.Application;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import ru.alexpanov.tinkoffexchange.di.AppComponent;
import ru.alexpanov.tinkoffexchange.di.AppModule;
import ru.alexpanov.tinkoffexchange.di.DaggerAppComponent;
import ru.alexpanov.tinkoffexchange.di.DataModule;

public class App extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .build();

        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e.printStackTrace();
            }
        });
    }
}
