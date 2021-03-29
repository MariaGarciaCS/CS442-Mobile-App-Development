package com.marigarci.stockassistant;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FinancialDataLoader extends AsyncTask<String, Integer, String> {
    private static final String TAG = "FinancialDataLoader";
    private final MainActivity mainActivity;

    private String API_KEY = "pk_f9ebb691d13c4e809c93bbf8b894d415";
    private String urlTemplate = "https://cloud.iexapis.com/stable/stock/";

    FinancialDataLoader(MainActivity mainActivity){
        this.mainActivity = mainActivity; }

    @Override
    protected String doInBackground(String... strings) {
        String QUERY = "https://cloud.iexapis.com/stable/stock/" + strings[0] + "/quote?token=" + API_KEY;
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

    @Override
    protected void onPostExecute(String s) {
        Stock newStock = parseStock(s);
        mainActivity.updateStockFD(newStock);
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
            Log.d(TAG, "parseStock: " + company);
            return stock;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}