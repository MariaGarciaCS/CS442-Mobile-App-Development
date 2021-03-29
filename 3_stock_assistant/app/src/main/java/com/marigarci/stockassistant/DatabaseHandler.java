package com.marigarci.stockassistant;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StockAppDB"; // from 15/18
    private static final String TABLE_NAME = "StockTable";
    private static final String SYMBOL = "Symbol";
    private static final String COMPANY = "Company";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANY + " TEXT not null)";
    private SQLiteDatabase database;

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: Creator DONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // only called if DB DNE
        Log.d(TAG, "onCreate: Making New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public ArrayList<Stock> loadStocks(){

        Log.d(TAG, "loadStocks: START");
        ArrayList<Stock> stocks = new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_NAME,
                new String[] {SYMBOL, COMPANY},
                null,
                null,
                null,
                null,
                null);
        if (cursor != null){
            cursor.moveToFirst(); // imp
            for(int i = 0; i < cursor.getCount(); i ++){
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                Stock s = new Stock(symbol, company);
                stocks.add(s);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadStocks: DONE");
        return stocks;
    }

    public void addStock(Stock stock) {
        Log.d(TAG,"Adding stock: " + stock.getSymbol());
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getSymbol());
        values.put(COMPANY, stock.getCompany());

        deleteStock(stock.getSymbol());
        database.insert(TABLE_NAME, null, values);
        Log.d(TAG,"Adding stock complete ");
    }

    public void deleteStock(String symbol){
        Log.d(TAG, "deleteStock: " + symbol);
        database.delete(TABLE_NAME,SYMBOL + " = ?", new String[]{symbol});
    }


    public void dumpDbToLog(){
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.d(TAG, "dumpDbToLog: vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);

                Log.d(TAG, "dumpDbToLog: " +
                        String.format("%s %-18s", SYMBOL + ":", symbol) +
                        String.format("%s %-18s", COMPANY + ":", company));
                cursor.moveToNext();
            }
            cursor.close();
        }

        Log.d(TAG, "dumpDbToLog: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    public void shutDown() { database.close();}
}