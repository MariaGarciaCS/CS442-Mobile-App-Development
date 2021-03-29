package com.marigarci.stockassistant;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {
    private static final String TAG = "StockAdapter";
    private ArrayList<Stock> stockList;
    private MainActivity mainAct;


    StockAdapter(ArrayList<Stock> sList, MainActivity ma){
        this.stockList = sList;
        mainAct = ma;
    }


    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW ViewHolder");

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);


        return  new StockViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock s = stockList.get(position);

        int color = Color.WHITE;
        String change = "";

        if (s.getpChange() > 0){
             color = Color.GREEN;
             change = String.format("\u25B2 %.2f", s.getpChange());

        }
        else if (s.getpChange() < 0){
             color = Color.RED;
             change = String.format("\u25BC %.2f", s.getpChange());
        }

        holder.symbol.setText((s.getSymbol()));
        holder.company.setText(s.getCompany());
        holder.price.setText(s.getPriceStr());
        holder.priceChange.setText(change);
        holder.percentChange.setText(" (" + s.getPercentStr() +")");

        holder.symbol.setTextColor(color);
        holder.company.setTextColor(color);
        holder.price.setTextColor(color);
        holder.priceChange.setTextColor(color);
        holder.percentChange.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
