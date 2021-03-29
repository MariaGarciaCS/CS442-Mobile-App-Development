package com.marigarci.stockassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "MainActivity";
    private static final String url = "https://www.marketwatch.com/investing/stock/";
    private  ArrayList<Stock> stockList = new ArrayList<>();
    private HashMap<String, String> stockSymbols = new HashMap<String, String>();
    public MainActivity ma = this;


    private RecyclerView recyclerView;
    private StockAdapter sAdapter;

    //New stuff
    private DatabaseHandler databaseHandler;
    private SwipeRefreshLayout swiper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recycler + Adapter
        sAdapter = new StockAdapter(stockList, this);
        recyclerView = findViewById(R.id.stockRecycler);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Swipe Refresh
        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        //Connection
        if (!connected()){
            netErrorDialog();
            return;
        }

        //Download Stock Symbols and Names
        SymbolLoader symbolLoader = new SymbolLoader(this);
        new Thread(symbolLoader).start();

        //Database
        databaseHandler = new DatabaseHandler(this);
        ArrayList<Stock> temp = databaseHandler.loadStocks();
        databaseHandler.dumpDbToLog();

        if (!temp.isEmpty()){
            for(int i = 0; i < temp.size(); i++){
                String symbol = temp.get(i).getSymbol();
                Log.d(TAG, "onCreate: Symbol in temp: " + symbol);
                String company = temp.get(i).getCompany();
                Log.d(TAG, "onCreate: Name in temp: " + company);
                FinancialDataLoader task = new FinancialDataLoader(MainActivity.this);
                task.execute(symbol);
            }
        }

        recyclerView.computeVerticalScrollOffset();

    }

    private void doRefresh(){
        //no connection
        if(!connected())
        {
            swiper.setRefreshing(false);
            netErrorDialog();
            return;
        }

        stockList.clear();
        ArrayList <Stock> temp = databaseHandler.loadStocks();
        if(!temp.isEmpty())
        {
            for(int i = 0 ; i < stockList.size(); i++)
            {
                FinancialDataLoader task = new FinancialDataLoader(MainActivity.this);
                task.execute(stockList.get(i).getSymbol(), stockList.get(i).getCompany());
            }
        }
        swiper.setRefreshing(false);
    }


    @Override
    protected void onDestroy() {
        databaseHandler.shutDown();
        super.onDestroy();
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
            if (!connected()){
                netErrorDialog();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add Stock");
                builder.setMessage("Enter Stock Symbol");

                final EditText stockSymbol = new EditText(this);
                stockSymbol.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                stockSymbol.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                stockSymbol.setGravity(Gravity.CENTER_HORIZONTAL);

                builder.setView(stockSymbol);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String stockSymbolText = stockSymbol.getText().toString();
                        if(!stockSymbolText.isEmpty()){ searchStock(stockSymbolText);}
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Clicking Stocks---------------------------
    @Override
    public void onClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        final Stock s = stockList.get(pos);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(this.url + s.getSymbol()));
        startActivity(i);
    }

    @Override
    public boolean onLongClick(final View v) {//Delete stock with delete confirmation
        int pos = recyclerView.getChildLayoutPosition(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHandler.deleteStock(stockList.get(pos).getSymbol());
                stockList.remove(pos);
                sAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setMessage("Remove this stock?");
        builder.setTitle("Remove Stock?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    //Network & Connection -----------------------
    public boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    //Add Stock---------------------------
    public void updateSymbols(HashMap<String,String> sList) {
        if (sList != null){
            stockSymbols.clear();
            stockSymbols.putAll(sList);
        }
        Log.d(TAG, "updateData: NUMSYMBOLS " + stockSymbols.size());
    }

    private void addStock(String s) {
            String[] ss = s.split(" - ");
            String symbol = ss[0].trim();
            String name = (ss.length > 1) ? ss[1] : "";
            for (Stock stock : stockList) {
                if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Duplicate Stock");
                    builder.setMessage(String.format("Stock Symbol %s is already displayed.", symbol));
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
            }
            databaseHandler.addStock(new Stock(symbol, name, 0.0, 0.0, 0.0));
            FinancialDataLoader task = new FinancialDataLoader(MainActivity.this);
            task.execute(symbol);
        }

    public void updateStockFD(Stock stock) {
        Log.d(TAG, "updateStockFD: new stock added" + stock.getSymbol());
        stockList.add(stock);
        databaseHandler.addStock(stock);
        Collections.sort(stockList);
        sAdapter.notifyDataSetChanged();
    }

    //Dialogs---------------------------
    private void netErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A Network Connection is Needed to Add Stocks");
        builder.setTitle("No Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //Loaders---------------------------
    private void searchStock(final String symbol) {
        HashMap<String, String> result = new HashMap<String, String>();
        for (String s : stockSymbols.keySet()) {
            if (s.toUpperCase().indexOf(symbol.toUpperCase()) == 0) {
                result.put(s, stockSymbols.get(s));
            }
        }
        switch (result.size()) {
            case 0:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Symbol Not Found: " + symbol);
                builder1.setMessage("Data for stock symbol");
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
            case 1:
                // add it
//                Toast.makeText(this, "One found", Toast.LENGTH_SHORT).show();
                addStock(symbol);
                break;
            default:
                // show list dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                ArrayList<String> array = new ArrayList<String>();
                for (String k : result.keySet()) {
                    array.add(k + " - " + result.get(k));
                }
                builder.setTitle("Make a selection");
                final CharSequence[] sArray = array.toArray(new CharSequence[0]);
                builder.setItems(sArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addStock(sArray[which].toString());

                    }
                });

                builder.setNegativeButton("Nevermind", null);
                AlertDialog dialog = builder.create();
                dialog.show();
        }

    }


    public void downloadFailed(){
        stockList.clear();
        sAdapter.notifyDataSetChanged();
    }
}