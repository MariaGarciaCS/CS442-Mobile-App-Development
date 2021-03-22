package com.marigarci.stockassistant;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FinancialDataLoader implements Runnable{
    private static final String TAG = "FinancialDataLoader";
    private final MainActivity mainActivity;
    private String API_KEY = "pk_f9ebb691d13c4e809c93bbf8b894d415";
    private String STOCK_SYMBOL;
    private String QUERY = "https://cloud.iexapis.com/stable/stock/" + STOCK_SYMBOL + "/quote?token=" + API_KEY;

    FinancialDataLoader(MainActivity mainActivity, String symbol){this.mainActivity = mainActivity; this.STOCK_SYMBOL = symbol;}

    @Override
    public void run() {
        Stock newStock = parseStock(getFD());
    }

    private String getFD(){
        Uri dataUri = Uri.parse(QUERY);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: " + urlToUse);

        try{
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            StringBuilder sb = new StringBuilder();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();

        } catch (Exception e){
            return null;
        }
    }

    // Gets string that is stock data from web for 1 stock. Returns 1 stock object
    private Stock parseStock(String s){
        try{
            JSONObject jsonObject = new JSONObject(s);
            String symbol = jsonObject.getString("symbol");
            String company = jsonObject.getString("companyName");
            double price = Double.parseDouble(jsonObject.getString("latestPrice"));
            double change = Double.parseDouble(jsonObject.getString("change"));
            double percent = Double.parseDouble(jsonObject.getString("changePercent"));
            Stock stock = new Stock(symbol, company, price, change, percent);
            return stock;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
