package ru.alexpanov.tinkoffexchange.ui;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.alexpanov.tinkoffexchange.R;
import ru.alexpanov.tinkoffexchange.db.ConverterResult;
import ru.alexpanov.tinkoffexchange.db.Currency;
import ru.alexpanov.tinkoffexchange.repository.Repository;
import ru.alexpanov.tinkoffexchange.utils.TextUtils;
import ru.alexpanov.tinkoffexchange.utils.Utils;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private CompositeDisposable disposables = new CompositeDisposable();
    private Repository repository;

    public void init(Repository repository) {
        this.repository = repository;
    }

    public void convert(String from, String to, List<Currency> currencies) {
        if (validateData(from, to, currencies)) {
            convert(new Currency(from), new Currency(to));
        }
    }

    private boolean validateData(String from, String to, List<Currency> currencies) {
        if (TextUtils.isEmpty(from)) {
            getViewState().onValidateFailed(R.string.field_empty, InputField.CURRENCY_FROM);
            return false;
        }
        if (TextUtils.isEmpty(to)) {
            getViewState().onValidateFailed(R.string.field_empty, InputField.CURRENCY_TO);
            return false;
        }
        Currency currencyFrom = new Currency(from);
        if (!currencies.contains(currencyFrom)) {
            getViewState().onValidateFailed(R.string.field_error, InputField.CURRENCY_FROM);
            return false;
        }
        Currency currencyTo = new Currency(to);
        if (!currencies.contains(currencyTo)) {
            getViewState().onValidateFailed(R.string.field_error, InputField.CURRENCY_TO);
            return false;
        }

        return true;
    }

    private void convert(Currency from, Currency to) {
        getViewState().showProgress();
        repository.convert(from, to)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ConverterResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(ConverterResult result) {
                        getViewState().hideProgress();
                        getViewState().onReceiveResult(from, to, result.getValue());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideProgress();
                        getViewState().onConverterError(Utils.getErrorTextRes(e));
                        Log.e(TAG, "onError: " + e.getMessage());
                    }
                });
    }

    public void getCurrencies() {
        repository.getAllCurrencies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Currency>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(List<Currency> currencies) {
                        getViewState().onCurrenciesLoad(currencies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                        getViewState().onLoadCurrencyError(Utils.getErrorTextRes(e));
                    }

                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
