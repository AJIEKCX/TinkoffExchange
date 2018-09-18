package ru.alexpanov.tinkoffexchange.ui;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.alexpanov.tinkoffexchange.db.Currency;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {

    void onReceiveResult(Currency from, Currency to, double value);

    @StateStrategyType(SingleStateStrategy.class)
    void onCurrenciesLoad(List<Currency> currencies);

    @StateStrategyType(SkipStrategy.class)
    void onConverterError(@StringRes int message);

    void onLoadCurrencyError(@StringRes int message);

    void onValidateFailed(@StringRes int res, InputField field);

    void showProgress();

    void hideProgress();
}
