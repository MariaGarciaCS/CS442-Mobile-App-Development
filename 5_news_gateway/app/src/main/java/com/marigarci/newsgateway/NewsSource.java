package com.marigarci.newsgateway;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsSource implements Runnable{
    private MainActivity mainActivity ;

    private final String URL_HEAD = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private final String URL_TAIL = "&apiKey=";
    private final String KEY = "f20d3ae307054456b8611ff280c2f361";
    private final String URL = URL_HEAD + URL_TAIL + KEY;
    private static final String TAG = "NewsSource";
    public String params;

    public NewsSource(MainActivity ma, String param) {
        mainActivity=ma;
        this.params=param;
    }

    private ArrayList<Source> parseJSON(String s) {
        try {
            Log.d(TAG, "parseJSON: parsing JSON");

            ArrayList<Source> sourcesList = new ArrayList<>();

            String Name = null;
            String Id = null;
            String Category = null;
            String Url = null;

            JSONObject jsonObject = new JSONObject(s);
            JSONArray sources = jsonObject.getJSONArray("sources");
            Log.d(TAG, "Length " + sources.length());

            for (int i = 0; i < sources.length(); i++) {
                JSONObject source_object = (JSONObject) sources.get(i);
                Id = source_object.getString("id");
                Name = source_object.getString("name");
                Url = source_object.getString("url");
                Category = source_object.getString("category");
                sourcesList.add(new Source(Id, Name, Url, Category));
            }

            return sourcesList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        Uri dataUri;
        if (params == "Null") {
            dataUri = Uri.parse(URL);
        } else if (params.equals("all")) {
            dataUri = Uri.parse(URL);
        } else
            dataUri = Uri.parse(URL_HEAD + params + URL_TAIL + KEY);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "run: "+urlToUse);
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent","");
            int HTTP_NOT_FOUND = conn.getResponseCode();
            if (HTTP_NOT_FOUND == 404) {
                handleResults(null);
            } else {
                Log.d(TAG, "run: Http_not_found"+HTTP_NOT_FOUND);

                conn.setRequestMethod("GET");


                InputStream is = conn.getInputStream();

                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                Log.d(TAG, "run:Here 2  ");
                handleResults(sb.toString());


            }
        } catch (Exception e) {
            Log.d(TAG, "run: Exception here "+e);
            handleResults(null);
        }
    }

    private void handleResults(String s) {
        if (s == null) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.noDataFound();
                }
            });

        } else {
            Log.d(TAG, "handleResults: Reached else");
            final ArrayList<Source> sources_data = parseJSON(s);
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assert sources_data != null;
                    mainActivity.sources_data_to_add(sources_data);
                }
            });

        }
    }
}
