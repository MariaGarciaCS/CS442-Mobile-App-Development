package com.marigarci.stockassistant;

import android.net.Uri;
import android.util.Log;

public class FinancialDataLoader implements Runnable{
    private static final String TAG = "FinancialDataLoader";
    private final MainActivity mainActivity;
    private String API_KEY = "pk_f9ebb691d13c4e809c93bbf8b894d415";
    private String STOCK_SYMBOL;
    private String QUERY = "https://cloud.iexapis.com/stable/stock/" + STOCK_SYMBOL + "/quote?token=" + API_KEY;

    FinancialDataLoader(MainActivity mainActivity){this.mainActivity = mainActivity;}

    @Override
    public void run() {
        Uri dataUri = Uri.parse(QUERY);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: " + urlToUse);
    }
}
