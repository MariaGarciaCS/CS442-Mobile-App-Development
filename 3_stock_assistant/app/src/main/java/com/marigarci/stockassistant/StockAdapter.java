package com.marigarci.stockassistant;

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

        //TODO: Add clicks
//        itemView.setOnClickListener(mainAct);
//        itemView.setOnLongClickListener(mainAct);


        return  new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock s = stockList.get(position);

        holder.symbol.setText((s.getSymbol()));
        holder.company.setText(s.getCompany());
        holder.price.setText(s.getPriceStr());
        holder.priceChange.setText(s.getpChangeStr());
        holder.percentChange.setText("(" + s.getPercentStr() + "%)");
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
