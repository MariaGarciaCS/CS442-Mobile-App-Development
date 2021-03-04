package com.marigarci.stockassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final ArrayList<Stock> stockList = new ArrayList<>();

    RecyclerView recyclerView;
    StockAdapter sAdapter;

    Stock sample1 = new Stock();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        }
        return super.onOptionsItemSelected(item);
    }
}