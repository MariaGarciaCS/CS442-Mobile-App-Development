package com.marigarci.stockassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "MainActivity";
    private  ArrayList<Stock> stockList = new ArrayList<>();
    private  ArrayList<Stock> tmpStockList = new ArrayList<>();
    private HashMap<String, String> stockSymbols = new HashMap<String, String>();

    RecyclerView recyclerView;
    StockAdapter sAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO:REMOVE
        Stock sample1 = new Stock();
        sample1.setAll("AAPL", "Apple Inc.", 135.72, 0.38, 0.28);
        stockList.add(sample1);
        Stock sample2 = new Stock("AMZN", "Amazon.com Inc.", 845.07, 0.93, 0.11);
        stockList.add(sample2);
        Stock sample3 = new Stock("GOOG", "Alphabet Inc.", 828.07, 3.91, 0.47);
        stockList.add(sample3);


        //Recycler + Adapter
        sAdapter = new StockAdapter(stockList, this);
        recyclerView = findViewById(R.id.stockRecycler);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Options Menu---------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addStockBtn){
            //TODO: Alert Dialog to add stock, only capital letters
            Toast.makeText(this, "Add Stock pressed", Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Please enter a Stock Symbol:");
//            builder.setTitle("Stock Selection");
        }
        return super.onOptionsItemSelected(item);
    }

    //Clicking Stocks---------------------------
    @Override
    public void onClick(View v) {//open web browser
        Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        //TODO: Open web browser onclick
    }

    @Override
    public boolean onLongClick(final View v) {//Delete stock with delete confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int pos = recyclerView.getChildLayoutPosition(v);
                stockList.remove(pos);
                //TODO:Save stock to db
//                saveNoteJSON();
                sAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setMessage("Do you want to permanently delete this note?");
        builder.setTitle("Delete Note?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    //Loaders---------------------------
    public void updateData(HashMap<String,String> sList) {
        stockSymbols.putAll(sList);
        sAdapter.notifyDataSetChanged();
    }

    public void downloadFailed() {
        stockList.clear();
        sAdapter.notifyDataSetChanged();
    }
}