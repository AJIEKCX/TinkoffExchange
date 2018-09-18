package ru.alexpanov.tinkoffexchange.utils;

import android.app.Activity;
import android.arch.persistence.room.EmptyResultSetException;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import ru.alexpanov.tinkoffexchange.R;

public class Utils {

    private Utils() {

    }

    @StringRes
    public static int getErrorTextRes(Throwable e) {
        if (e instanceof HttpException || e instanceof EmptyResultSetException) {
            return R.string.http_error;
        } else if (e instanceof SocketTimeoutException) {
            return R.string.timeout_error;
        } else if (e instanceof IOException) {
            return R.string.connection_error;
        }
        return R.string.unknown_error;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
