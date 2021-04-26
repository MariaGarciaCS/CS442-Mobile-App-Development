package com.marigarci.civicadvocacy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainAct";
    private MainActivity ma;
    private RecyclerView recyclerView;
    private OfficialAdapter oAdapter;
    private ArrayList<Official> olist = new ArrayList<>();

    private ConnectivityManager cm;

    private Locator locator;

    private TextView locationTxt;
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this;

        locationTxt = (TextView) findViewById(R.id.locationTxt);

        //Recylcer + Adapter
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        oAdapter = new OfficialAdapter(olist, this);
        recyclerView.setAdapter(oAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Warning
        warning = (TextView) findViewById(R.id.ma_warning);

        //Network Check
        if (connected()){
            locator = new Locator(this);
        } else {
            showWarning( "Data cannot be accessed/loaded without an internet connection");
        }
        if (locationTxt.getText().toString().equals("No Data For Location")) showWarning("No location data. Restart App.");
    }

    //Options Menu---------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.info_btn:
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.search_btn:
                searchBtn();

            default:return super.onOptionsItemSelected(item);
        }
    }

    //Main Activity Actions ---------------------------
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("location", locationTxt.getText().toString());
        intent.putExtra("official", olist.get(pos));
        startActivityForResult(intent, 1);
    }


    public void searchBtn() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a City, State or Zip code");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText inputTextView = (EditText) view.findViewById(R.id.inputTxt);
                String input = inputTextView.getText().toString();

                OfficialLoader asyncDataLoader = new OfficialLoader(ma);
                asyncDataLoader.execute(input);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //Location----------------------------------------------------------------

    public void setLocation(double lat, double lon){
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat,lon, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses == null){
            Log.d(TAG, "setLocation: address is null");

        }else{
            //TODO:Set Address
        }

        OfficialLoader ol = new OfficialLoader(this);

        //todo: set address
        //ol.execute(locationTextView.getText().toString());
    }

    public void setLocationTxt(String loc){
        locationTxt.setText(loc);
    }

    //Officials---------------------------
    public void addOfficial(Official of){
        olist.add(of);
        oAdapter.notifyDataSetChanged();
    }

    public void clearOfficial(){ olist.clear();
    }

    //Network Connection-------------------
    public boolean connected(){
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo.isConnectedOrConnecting() && networkInfo !=null){
            return true;
        }else {
            return false;
        }
    }

    //Warnings-----------------------------
    public void showWarning(String msg){
        warning.setVisibility(View.VISIBLE);
        warning.setText(msg);
    }

    public void closeWarning(){
        warning.setVisibility(View.INVISIBLE);
    }

}