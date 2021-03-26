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
    private SwipeRefreshLayout swipe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recycler + Adapter
        sAdapter = new StockAdapter(stockList, this);
        recyclerView = findViewById(R.id.stockRecycler);
        recyclerView.setAdapter(sAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Database
        databaseHandler = new DatabaseHandler(this);

        //Download Stock Symbols and Names
        SymbolLoader symbLoader = new SymbolLoader(this);
        new Thread(symbLoader).start();




//        if (connected()){
//            //TODO add stocks in temp list to stocklist
//        }
//        else{
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("No Network Connection");
//            builder.setMessage("Need A Network Connection To Display Stocks.");
//            AlertDialog dialog = builder.create();
//            dialog.show();
//
//            //TODO: Display stocks with values set to zero
//            //TODO: Sort STocks
//
//        }
    }

    @Override
    protected void onResume() {
        if (connected()){
            //Get stocks from DB
            databaseHandler = new DatabaseHandler(this);
            ArrayList<Stock> temp = databaseHandler.loadStocks();
            stockList.clear();


            for(int i = 0; i < temp.size(); i++){
                String symbol = temp.get(i).getSymbol();
//                FinancialDataLoader fd = new FinancialDataLoader(this, symbol);
//                new Thread(fd).start();
                new FinancialDataLoader(ma).execute(symbol);
            }

            Collections.sort(stockList);
            sAdapter.notifyDataSetChanged();
        }
        else{
            netError();
        }
        super.onResume();
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
            //Check connection
            if (connected()){
                if (stockSymbols.isEmpty()){
                    //TODO: ADD Symb
                }
                addStockDialog();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No Network Connection");
                builder.setMessage("Need A Network Connection To Add Stocks.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return true;
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
    public Boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(netInfo != null && netInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }
    //Add Stock---------------------------
    public void addStock(Stock s){
        Log.d(TAG, "addStock: " + s.getCompany());
        ArrayList<Stock> temp = databaseHandler.loadStocks();

        for(int i = 0; i < temp.size(); i ++){
            if(temp.get(i).getSymbol().equals( s.getSymbol())){
                s.setCompany( temp.get(i).getCompany());
            }
        }
        stockList.add(s);
        Collections.sort(stockList);
        sAdapter.notifyDataSetChanged();
    }

    private void findStock(String symbol) {
        HashMap<String, String> found = new HashMap<String, String>();
        for (String symb : stockSymbols.keySet()) {
            if (symb.toUpperCase().indexOf(symbol.toUpperCase()) == 0) {
                found.put(symb, stockSymbols.get(symb));
            }
        }
//        int count = Collections.frequency(new ArrayList<String>(stockSymbols.values()), symbol);

//        //One stock found
//        if (count == 1){
//            //TODO
//            addStock(symbol);
//        }
//        //Many stocks found
//        else if (count > 1){
////            AlertDialog.Builder builder = new AlertDialog.Builder(this);
////            ArrayList<String> array = new ArrayList<String>();
////            for (String k : found.keySet()) {
////                array.add(k + " - " + found.get(k));
////            }
////            builder.setTitle("Choose a stock");
////            final CharSequence[] sArray = array.toArray(new CharSequence[0]);
////            builder.setItems(sArray, new DialogInterface.OnClickListener() {
////                public void onClick(DialogInterface dialog, int which) {
////                    addStock(sArray[which].toString());
////
////                }
////            });
////
////            builder.setNegativeButton("Nevermind", null);
////            AlertDialog dialog = builder.create();
////            dialog.show();
//        }
//        //No stocks found
//        else{
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//            builder1.setTitle("Symbol Not Found: " + symbol);
//            AlertDialog dialog1 = builder1.create();
//            dialog1.show();
//        }

    }

    //Dialogs---------------------------

    public void addStockDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_CAP_CHARACTERS );
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s = et.getText().toString();
                boolean exists = false;

                for(int j = 0; j < stockList.size() ;j++){
                    String symbol = stockList.get(j).getSymbol();
                    if(symbol.equals(s)){exists = true;}
                }
                if(exists){
                    ma.multipleStocksDialog(s);
                }
                else {
//                    FinancialDataLoader fd2 = new FinancialDataLoader(ma, s);
//                    new Thread(fd2).start();
                    new FinancialDataLoader(ma).execute(s);
                    sAdapter.notifyDataSetChanged();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        builder.setMessage("Enter a Stock");
        builder.setTitle("New Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void netError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A Network Connection is Needed to Add Stocks");
        builder.setTitle("No Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void multipleStocksDialog(String symb){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stock Symbol " + symb + " already exists");
        builder.setTitle("Existing Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Loaders---------------------------

    public void loadStock(){
        stockList.clear();

        sAdapter.notifyDataSetChanged();
    }
    public void updateStock(Stock newStock){
        stockList.add(newStock);
    }

    public void updateData(HashMap<String,String> sList) {
        stockSymbols.clear();
        stockSymbols.putAll(sList);
        Log.d(TAG, "updateData: NUMSYMBOLS " + stockSymbols.size());
        sAdapter.notifyDataSetChanged();
    }

    public void updateFinancialData(Stock s){
        stockList.add(s);
    }

    public void downloadFailed(){
        stockList.clear();
        sAdapter.notifyDataSetChanged();
    }
}