package com.example.ramona.ydbrothers.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aideo on 4/13/2017.
 */

public class Models {
    public static String randomID() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        String sDateTime = sdf.format(c.getTime());
        return sDateTime;
    }

    public static String formatAmount(double num) {
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols decimalFormatSymbol = new DecimalFormatSymbols();
        decimalFormatSymbol.setGroupingSeparator(',');
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbol);
        return decimalFormat.format(num);
    }

    public static void NotifyDataSetChanged(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.FLAG_CHECK_DB, true);
        editor.commit();
    }
    public static void NotifyDebtChanged(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constant.FLAG_CHECK_DEBT, true);
        editor.commit();
    }
}
