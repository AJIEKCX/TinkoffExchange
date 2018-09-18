package ru.alexpanov.tinkoffexchange.ui;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.alexpanov.tinkoffexchange.App;
import ru.alexpanov.tinkoffexchange.R;
import ru.alexpanov.tinkoffexchange.db.Currency;
import ru.alexpanov.tinkoffexchange.repository.Repository;
import ru.alexpanov.tinkoffexchange.utils.SimpleTextWatcher;
import ru.alexpanov.tinkoffexchange.utils.TextUtils;
import ru.alexpanov.tinkoffexchange.utils.Utils;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @Inject
    Repository repository;

    @InjectPresenter
    MainPresenter presenter;

    @BindView(R.id.currency_from_input)
    TextInputLayout currencyFromLayout;

    @BindView(R.id.currency_to_input)
    TextInputLayout currencyToLayout;

    @BindView(R.id.currency_from_view)
    AutoCompleteTextView currencyFromView;

    @BindView(R.id.currency_to_view)
    AutoCompleteTextView currencyToView;

    @BindView(R.id.calculate_button)
    Button calculateButton;

    @BindView(R.id.result_view)
    TextView resultView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private List<Currency> currencies;
    private ArrayAdapter<Currency> currencyFromAdapter;
    private ArrayAdapter<Currency> currencyToAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getAppComponent().inject(this);
        ButterKnife.bind(this);
        presenter.init(repository);

        initViewAdapters();
        addTextListeners();

        if (savedInstanceState == null) {
            presenter.getCurrencies();
        }
    }

    private void initViewAdapters() {
        currencies = new ArrayList<>();
        currencyFromAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, currencies);
        currencyFromView.setAdapter(currencyFromAdapter);

        currencyToAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, currencies);
        currencyToView.setAdapter(currencyToAdapter);
    }

    private void addTextListeners() {
        currencyFromView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currencyFromLayout.setErrorEnabled(false);
                currencyFromLayout.setError(null);
            }
        });
        currencyToView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currencyToLayout.setErrorEnabled(false);
                currencyToLayout.setError(null);
            }
        });
    }

    @Override
    public void onReceiveResult(Currency from, Currency to, double value) {
        String result = TextUtils.resultFormat(from, to, value);
        resultView.setText(result);
    }

    @Override
    public void onCurrenciesLoad(List<Currency> value) {
        calculateButton.setEnabled(true);
        currencies.addAll(value);
        currencyFromAdapter.notifyDataSetChanged();
        currencyToAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.calculate_button)
    public void onCalculateButtonClick() {
        Utils.hideKeyboard(this);
        String from = currencyFromView.getText().toString();
        String to = currencyToView.getText().toString();
        presenter.convert(from, to, currencies);

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        resultView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        resultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onValidateFailed(@StringRes int res, InputField field) {
        switch (field) {
            case CURRENCY_FROM:
                currencyFromLayout.setError(getString(res));
                currencyFromLayout.requestFocus();
                break;
            case CURRENCY_TO:
                currencyToLayout.setError(getString(res));
                currencyToLayout.requestFocus();
                break;
        }
    }

    @Override
    public void onConverterError(@StringRes int message) {
        Snackbar.make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG)
                .setAction(R.string.repeat, view -> onCalculateButtonClick())
                .show();
    }

    @Override
    public void onLoadCurrencyError(@StringRes int message) {
        Snackbar.make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.repeat, view -> presenter.getCurrencies())
                .show();
    }
}
