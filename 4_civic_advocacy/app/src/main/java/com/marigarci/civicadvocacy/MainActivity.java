package com.marigarci.civicadvocacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private int MY_PERM_REQUEST_CODE = 12345;
    private Location start;
    private boolean showingInfo = false;

    private RecyclerView recyclerView;
    private OfficialAdapter oAdapter;
    private TextView myLocation;
    private myLocation locationSearch;
    private TextView netErrorTextView;

    private List<Official> mOfficialList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLocation = (TextView) findViewById(R.id.location_textview);
        netErrorTextView = (TextView) findViewById(R.id.errorTextView);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new Divider(this));

        oAdapter = new OfficialAdapter(this, mOfficialList);
        recyclerView.setAdapter(oAdapter);

        callOfficialLoader();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search_btn:
                showEnterZipDialog();
                return true;
            case R.id.info_btn:
                Intent aboutIntent = new Intent(this, About.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onDestroy: Inside On Stop!");
        saveToSharedPref("");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No Permission yet
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERM_REQUEST_CODE);
            //Log.d(TAG, "checkPermission: ACCESS_FINE_LOCATION Permission requested, awaiting response.");
            return false; // Do not yet have permission - but I just asked for it
        } else {
            //Log.d(TAG, "checkPermission: Already have ACCESS_FINE_LOCATION Permission for this app.");
            return true;  // I already have this permission
        }
    }

    private void callOfficialLoader() {
        SharedPreferences prefs = getSharedPreferences("SharedPref", MODE_PRIVATE);
        String location = prefs.getString("location", "");
        Log.d(TAG, "callOfficialLoader: Location::" + location);
        if (!location.equalsIgnoreCase("")) {

            OfficialLoader Dataloader = new OfficialLoader(this,location);
            new Thread(Dataloader).start();

        } else {
            boolean havePermission = checkPermission();
            if (havePermission) {
                findLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERM_REQUEST_CODE) {
            if (grantResults.length == 0) {
                Log.d(TAG, "onRequestPermissionsResult: Somehow I got an empty 'grantResults' array");
                return;
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "Fine location permission granted");
                findLocation();

            } else {
                Toast.makeText(this, "Address cannot be acquired from the provided latitude/longitude", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    public void findLocation() {
        start = null;
        long timeNow = System.currentTimeMillis();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(this, "No myLocation services available", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String providerName : locationManager.getAllProviders()) {
            sb.append("PROVIDER: ").append(providerName).append("\n");

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Log.d(TAG, "findLocation: "+providerName);
            //String bestProvider = locationManager.getBestProvider(criteria, true);

            android.location.Location loc = locationManager.getLastKnownLocation(providerName);
            Log.d(TAG, "findLocation: "+ loc);

            if (loc != null) {
                sb.append("myLocation found:\n");
                sb.append("  Accuracy: ").append(loc.getAccuracy()).append("m\n");
                sb.append("  Time: ").append((timeNow - loc.getTime()) / 1000).append("sec\n");
                sb.append("  Latitude: ").append(loc.getLatitude()).append("\n");
                sb.append("  Longitude: ").append(loc.getLongitude()).append("\n\n");
                if (start == null || loc.getAccuracy() < start.getAccuracy()) {
                    start = new Location(loc);
                }
            } else {
                sb.append("No location for ").append(providerName).append("\n");
            }
        }
        if (start == null)
            sb.append("No location provider available :(");
        else
            sb.append(String.format(Locale.US, "\n\nBest Provider: %s (%.2f)",
                    start.getProvider(), start.getAccuracy()));
        getInfo();

    }

    public void getInfo() {
        int numResults = 10;
        if (start == null) {
            myLocation.setText("No Data for Location");
            netErrorTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        if (!showingInfo) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses;
            StringBuilder sb = new StringBuilder();
            try {
                addresses = geocoder.getFromLocation(
                        start.getLatitude(), start.getLongitude(), numResults);
                Log.d(TAG, "getInfo:1 Addresses"+addresses);
                for (Address a : addresses) {
                    if (TextUtils.isDigitsOnly(a.getFeatureName().replace("-", ""))) {
                        saveToSharedPref(a.getPostalCode());
                        callOfficialLoader();
                        break;
                    } else
                        sb.append(a.getFeatureName()).append("\n");
                }

            } catch (Exception e) {
                Toast.makeText(this, "Address cannot be acquired from provided latitude/longitude", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            showingInfo = true;
        } else {
            findLocation();
            showingInfo = false;
        }

    }

    public void getOfficialsInfo(myLocation myLoc, List<Official> officials) {
        mOfficialList.clear();
        locationSearch = myLoc;
        if (myLocation != null && officials != null) {
            recyclerView.setVisibility(View.VISIBLE);
            netErrorTextView.setVisibility(View.GONE);
            myLocation.setText(myLoc.getCity() + ", " + myLoc.getState() + " " + myLoc.getZip());
            mOfficialList.addAll(officials);
        } else {
            recyclerView.setVisibility(View.GONE);
            netErrorTextView.setVisibility(View.VISIBLE);
            myLocation.setText("No Data For Location");
        }
        oAdapter.notifyDataSetChanged();
    }

    public void showEnterZipDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.location_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText enterCityEditText = (EditText) dialogView.findViewById(R.id.location_edittext);
        enterCityEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        dialogBuilder.setTitle("Enter a city, State or a Zip code");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                saveToSharedPref(enterCityEditText.getText().toString());
                callOfficialLoader();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog enterStockSymbolEditTextDialog = dialogBuilder.create();
        enterStockSymbolEditTextDialog.show();
    }

    public myLocation getmyLocation() {
        return locationSearch;
    }

    private void saveToSharedPref(String address) {
        Log.d(TAG, "saveToSharedPref: ");
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("location", address);
        editor.apply();

        SharedPreferences prefs = getSharedPreferences("SharedPref", MODE_PRIVATE);
        String location = prefs.getString("location", "");
        Log.d(TAG, "saveToSharedPref: Location::" + location);
    }

}