package com.marigarci.stockassistant;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder {
    TextView symbol;
    TextView company;
    TextView price;
    TextView priceChange;
    TextView percentChange;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);

        symbol = itemView.findViewById(R.id.Symbol);
        company = itemView.findViewById(R.id.Company);
        price = itemView.findViewById(R.id.Price);
        priceChange = itemView.findViewById(R.id.Change);
        percentChange = itemView.findViewById(R.id.Percent);
    }
}
