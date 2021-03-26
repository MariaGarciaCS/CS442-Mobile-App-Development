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

        //TODO Swipe refresh layout
    }

    //TODO: OnRefresh
    public void onRefresh(){
        if (connected()){
            //Get stocks from DB
            ArrayList<Stock> temp = databaseHandler.loadStocks();
            stockList.clear();

            for(int i = 0; i < temp.size(); i++){
                String symbol = temp.get(i).getSymbol();
                new FinancialDataLoader(ma).execute(symbol);
            }

            Collections.sort(stockList);
            sAdapter.notifyDataSetChanged();
        }
        else{
            netErrorDialog();
            //TODO: Swipe refresh
        }
    }

    @Override
    protected void onResume() {
        if (connected()){
            //Get stocks from DB
            ArrayList<Stock> temp = databaseHandler.loadStocks();
            stockList.clear();

            for(int i = 0; i < temp.size(); i++){
                String symbol = temp.get(i).getSymbol();
                new FinancialDataLoader(ma).execute(symbol);
            }

            Collections.sort(stockList);
            sAdapter.notifyDataSetChanged();
        }
        else{
            netErrorDialog();
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
                addStockDialog();
            }
            else{
                netErrorDialog();
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
    public boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
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

    //Dialogs---------------------------
    public void addStockDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a Stock");
        builder.setTitle("New Stock");

        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS );
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String symbol = et.getText().toString();
                new FinancialDataLoader(ma).execute(symbol);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void netErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("A Network Connection is Needed to Add Stocks");
        builder.setTitle("No Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void existingStocksDialog(String symb){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stock Symbol " + symb + " already exists");
        builder.setTitle("Existing Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void multipleResultsDialog(ArrayList<Stock> r){
        final ArrayList<Stock> results = r;

        final CharSequence[] sArray = new CharSequence[results.size()];
        for(int i = 0; i < results.size(); i++){
            sArray[i] = results.get(i).getSymbol() + " - " + results.get(i).getCompany();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");

        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                ArrayList<Stock> selected = new ArrayList<>();
                selected.add(results.get(which));
                updateData(selected);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // cancel
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Loaders---------------------------

    public void updateSymbols(HashMap<String,String> sList) {
        if (sList != null){
            stockSymbols.clear();
            stockSymbols.putAll(sList);
        }
        Log.d(TAG, "updateData: NUMSYMBOLS " + stockSymbols.size());
    }

    public void updateData(ArrayList<Stock> sList){
        if (sList != null){
            databaseHandler.addAll(sList);
            ArrayList<Stock> temp = databaseHandler.loadStocks();
            stockList.clear();

            if (temp != null){
                for (int i = 0; i < temp.size(); i++){
                    String symbol = temp.get(i).getSymbol();
                    new FinancialDataLoader(ma).execute(symbol);
                }
            }
            Collections.sort(stockList);
            sAdapter.notifyDataSetChanged();
        }
    }

    public void downloadFailed(){
        stockList.clear();
        sAdapter.notifyDataSetChanged();
    }
}