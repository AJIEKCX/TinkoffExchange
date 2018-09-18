package ru.alexpanov.tinkoffexchange.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.alexpanov.tinkoffexchange.ui.MainActivity;

@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface AppComponent {

    void inject(MainActivity target);
}
