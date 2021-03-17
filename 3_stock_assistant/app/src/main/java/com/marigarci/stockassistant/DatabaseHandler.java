package com.marigarci.stockassistant;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StockAppDB";
    private static final String TABLE_NAME = "StockTable";

    // DB Columns
    private static final String SYMBOL = "Symbol";
    private static final String COMPANY = "Company";

    private final SQLiteDatabase database;
    
    private static final String SQL_CREATE_TABLE = 
            "CREATE TABLE" + TABLE_NAME + " (" +
                    SYMBOL + "TEXT not null unique," +
                    COMPANY + " TEXT not null unique)";

    DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: Setup DONE");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: Making New DB");
    }

    public ArrayList<Stock> loadStock(){

    }

    public void dumpDbToLog(){

    }

    public void shutDown(){

    }

    public void deleteStock(String symbol){

    }
}
