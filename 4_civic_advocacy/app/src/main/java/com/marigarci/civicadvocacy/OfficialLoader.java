package com.marigarci.civicadvocacy;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OfficialLoader implements Runnable {
    private static final String TAG = "OfficialLoader";
    private MainActivity mainActivity;
    private List<Official> officials = null;
    private myLocation location = null;
    public String locationrec;

    public OfficialLoader(MainActivity ma,String locationrec) {
        mainActivity = ma;
        this.locationrec=locationrec;
    }



    private void parseJSON(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject normalizedInputJSONObject = jsonObject.getJSONObject("normalizedInput");

            //Get location data from JSON
            location = new myLocation();
            officials = new ArrayList<>();

            location.setCity(normalizedInputJSONObject.getString("city"));
            location.setState(normalizedInputJSONObject.getString("state"));
            location.setZip(normalizedInputJSONObject.getString("zip"));

            JSONArray officialJSONArray = jsonObject.getJSONArray("officials");
            for (int i = 0; i < officialJSONArray.length(); i++) {
                Official official = new Official();
                JSONObject officialJSONObject = officialJSONArray.getJSONObject(i);

                official.setName(officialJSONObject.getString("name"));
                if (officialJSONObject.has("address")) {
                    JSONObject addressJSON = officialJSONObject.getJSONArray("address").getJSONObject(0);
                    official.setAddress(addressJSON.getString("line1"));
                    if (addressJSON.has("line2")) {
                        official.setAddress(official.getAddress() + ", " + addressJSON.getString("line2"));
                    }
                    if (addressJSON.has("line3")) {
                        official.setAddress(official.getAddress() + ", " + addressJSON.getString("line3"));
                    }
                    official.setCity(addressJSON.getString("city"));
                    official.setState(addressJSON.getString("state"));
                    official.setZip(addressJSON.getString("zip"));
                }
                if (officialJSONObject.has("party")) {
                    official.setParty(officialJSONObject.getString("party"));
                } else {
                    official.setParty("Unknown");
                }
                if (officialJSONObject.has("phones")) {
                    official.setPhone(officialJSONObject.getJSONArray("phones").get(0).toString());
                } else {
                    official.setPhone("No Data Provided");
                }
                if (officialJSONObject.has("urls")) {
                    official.setWebsite(officialJSONObject.getJSONArray("urls").get(0).toString());
                } else {
                    official.setWebsite("No Data Provided");
                }
                if (officialJSONObject.has("emails")) {
                    official.setEmail(officialJSONObject.getJSONArray("emails").get(0).toString());
                } else {
                    official.setEmail("No Data Provided");
                }
                if (officialJSONObject.has("photoUrl")) {
                    official.setImage(officialJSONObject.getString("photoUrl"));
                } else {
                    official.setImage("No Data Provided");
                }

                if (officialJSONObject.has("channels")) {
                    JSONArray channelsJSONArray = officialJSONObject.getJSONArray("channels");
                    HashMap<String, String> channels = new HashMap<>();
                    for (int j = 0; j < channelsJSONArray.length(); j++) {
                        JSONObject channelJSONObject = channelsJSONArray.getJSONObject(j);
                        channels.put(channelJSONObject.getString("type"), channelJSONObject.getString("id"));
                    }
                    official.setSocials(channels);
                }
                officials.add(official);
            }

            JSONArray officeJSONArray = jsonObject.getJSONArray("offices");
            for (int i = 0; i < officeJSONArray.length(); i++) {
                JSONObject officeJSONObject = officeJSONArray.getJSONObject(i);

                JSONArray officialIndices = officeJSONObject.getJSONArray("officialIndices");
                for (int j = 0; j < officialIndices.length(); j++) {
                    officials.get(officialIndices.getInt(j)).setOffice(officeJSONObject.getString("name"));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {



        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL("https://www.googleapis.com/civicinfo/v2/representatives?key=" +
                    "AIzaSyDc-ojj6lx4hMY9RrqfmxaD8JhfmEiPRaA" +
                    "&address=" +
                    locationrec);


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "doInBackground: Response Code: " + conn.getResponseCode() + ", " + conn.getResponseMessage());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            handleResults(null);
        }
        if (sb == null) {

        } else if (sb.toString().isEmpty()) {

        } else {
            handleResults(sb.toString());
        }
    }

    private void handleResults(final String s) {
        if (s == null) {
            Log.d(TAG, "handleResults: Failure in data download");
            return;
        }
        parseJSON(s);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.getOfficialsInfo(location, officials);
            }
        });

    }

}