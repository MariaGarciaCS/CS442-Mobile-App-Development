package com.marigarci.stockassistant;

public class FinancialDataLoader implements Runnable{
    private static final String TAG = "FinancialDataLoader";
    private final MainActivity mainActivity;
    private String APIKEY = "pk_f9ebb691d13c4e809c93bbf8b894d415";
    private String STOCK_SYMBOL;
    private String QUERY = "https://cloud.iexapis.com/stable/stock/" + STOCK_SYMBOL + "/quote?token=" + APIKEY;


    @Override
    public void run() {

    }
}
