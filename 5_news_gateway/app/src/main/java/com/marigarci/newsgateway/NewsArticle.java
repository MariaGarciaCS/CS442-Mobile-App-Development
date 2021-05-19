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

public class NewsArticle implements Runnable{
    private final String URL_ARTICLE_HEAD = "https://newsapi.org/v2/top-headlines?pageSize=50&sources=";
    private final String URL_ARTICLE_TAIL = "&apiKey=";
    private final String KEY = "d019ad37db19495b81cb83d8adcde4ec";
    private static final String TAG = "NewsArticle";
    static NewsService newsService = new NewsService();
    public String source_data;


    public NewsArticle(String source_data) {

        this.source_data=source_data;

    }


    private ArrayList<Article> parseJSON(String s) {
        try {
            Log.d(TAG, "parseJSON");
            ArrayList<Article> articleList = new ArrayList<>();
            JSONObject jObjMain = new JSONObject(s);

            JSONArray articles = jObjMain.getJSONArray("articles");
            Log.d(TAG, "ArticalLength " + articles.length());
            for (int i = 0; i < articles.length(); i++) {
                JSONObject source_object = (JSONObject) articles.get(i);
                articleList.add(new Article(source_object.getString("author"), source_object.getString("title"), source_object.getString("description"), source_object.getString("urlToImage"), source_object.getString("publishedAt"), source_object.getString("url")));
            }
            return articleList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        Uri dataUri;
        dataUri = Uri.parse(URL_ARTICLE_HEAD + source_data.toLowerCase() + URL_ARTICLE_TAIL + KEY);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            Log.d(TAG, "run2: "+url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("User-Agent","");
            int HTTP_NOT_FOUND = conn.getResponseCode();
            if (HTTP_NOT_FOUND == 404) {

                handleResults(null);
            } else {
                Log.d(TAG, "run2: "+HTTP_NOT_FOUND);
                conn.setRequestMethod("GET");

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                handleResults(sb.toString());

            }
        } catch (Exception e) {
            handleResults(null);
        }
    }

    private void handleResults(String s) {
        if (s == null) {
            Log.d(TAG, "handleResultsNewsArticle: Failure in data download");
            return;}

        ArrayList<Article> articleList = parseJSON(s);

        newsService.setArticles(articleList);
    }
}
