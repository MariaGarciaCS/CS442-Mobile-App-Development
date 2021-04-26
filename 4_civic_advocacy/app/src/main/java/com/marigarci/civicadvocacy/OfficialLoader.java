package com.marigarci.civicadvocacy;

import android.os.AsyncTask;
import android.util.Log;

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

public class OfficialLoader extends AsyncTask<String, Void, String> {
    private String urlPrefix = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDc-ojj6lx4hMY9RrqfmxaD8JhfmEiPRaA&address=";
    private MainActivity ma;

    public OfficialLoader(MainActivity ma){
        this.ma = ma;
    }

    @Override
    protected String doInBackground(String... params) {
        String location = params[0];

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlPrefix + location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        parseJson(s);
    }

    private void parseJson(String s) {


        try {
            JSONObject jObjMain = new JSONObject(s);
            JSONObject jNormalInput = jObjMain.getJSONObject("normalizedInput");

            String locationText = jNormalInput.getString("city")+", "+jNormalInput.getString("state")+" "+jNormalInput.getString("zip");
            ma.setLocationTxt(locationText);
            JSONArray jArrayOffices = jObjMain.getJSONArray("offices");
            JSONArray jArrayOfficials = jObjMain.getJSONArray("officials");

            int length = jArrayOffices.length();
            ma.clearOfficial();

            for (int i = 0; i<length; i++){
                JSONObject jObj = jArrayOffices.getJSONObject(i);
                String officeName = jObj.getString("name");

                JSONArray indicesStr = jObj.getJSONArray("officialIndices");
                ArrayList<Integer> indices = new ArrayList<>();

                for (int j = 0; j<indicesStr.length(); j++){
                    int pos = Integer.parseInt(indicesStr.getString(j));
                    Official official = new Official(officeName);
                    JSONObject jOfficial = jArrayOfficials.getJSONObject(pos);

                    official.setName(jOfficial.getString("name"));

                    JSONArray jAddresses = jOfficial.getJSONArray("address");
                    JSONObject jAddress = jAddresses.getJSONObject(0);

                    String address="";

                    if (jAddress.has("line1")) address+=jAddress.getString("line1")+'\n';
                    if (jAddress.has("line2")) address+=jAddress.getString("line2")+'\n';
                    if (jAddress.has("line3")) address+=jAddress.getString("line3")+'\n';
                    if (jAddress.has("city")) address+=jAddress.getString("city")+", ";
                    if (jAddress.has("state")) address+=jAddress.getString("state")+' ';
                    if (jAddress.has("zip")) address+=jAddress.getString("zip");

                    official.setAddress(address);

                    if (jOfficial.has("party")) official.setParty(jOfficial.getString("party"));
                    if (jOfficial.has("phones")) official.setPhone(jOfficial.getJSONArray("phones").getString(0));
                    if (jOfficial.has("urls")) official.setWebsite(jOfficial.getJSONArray("urls").getString(0));
                    if (jOfficial.has("emails")) official.setEmail(jOfficial.getJSONArray("emails").getString(0));

                    if (jOfficial.has("channels")){
                        Socials soc = new Socials();

                        JSONArray jChannels = jOfficial.getJSONArray("channels");
                        for (int k = 0; k<jChannels.length(); k++){
                            JSONObject jChannel = jChannels.getJSONObject(k);
                            if (jChannel.getString("type").equals("Facebook")) soc.setFacebook(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("Twitter")) soc.setTwitter(jChannel.getString("id"));
                            if (jChannel.getString("type").equals("YouTube")) soc.setYoutube(jChannel.getString("id"));
                        }
                        official.setSocials(soc);
                    }

                    if (jOfficial.has("photoUrl")) official.setImage(jOfficial.getString("photoUrl"));
                    ma.addOfficial(official);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            return;
        }
    }


}
