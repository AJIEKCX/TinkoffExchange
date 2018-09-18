package ru.alexpanov.tinkoffexchange.utils;

import android.support.annotation.Nullable;

import java.util.Locale;

import ru.alexpanov.tinkoffexchange.db.Currency;

public class TextUtils {

    private TextUtils() {

    }

    public static boolean isEmpty(@Nullable CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }

    public static String currencyKeyFormat(Currency from, Currency to) {
        return String.format(Locale.getDefault(),"%s_%s", from, to);
    }

    public static String resultFormat(Currency from, Currency to, double value) {
        return String.format(Locale.getDefault(), "1 %1$s = %2$f %3$s", from, value, to);
    }
}
