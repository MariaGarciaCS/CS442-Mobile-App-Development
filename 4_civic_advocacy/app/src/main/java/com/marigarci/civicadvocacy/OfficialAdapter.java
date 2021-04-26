package com.marigarci.civicadvocacy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialsViewHolder> {
    private static final String TAG = "OfficialsAdapter";
    private ArrayList<Official> oList;
    private MainActivity ma;

    public OfficialAdapter(ArrayList<Official> oList, MainActivity ma){
        this.oList = oList;
        this.ma = ma;
    }

    @Override
    public OfficialsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW ViewHolder");

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_row, parent, false);

        //TODO: clicking on entries
//        itemView.setOnClickListener(ma);
//        itemView.setOnLongClickListener(ma);

        return  new OfficialsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfficialsViewHolder holder, int position) {
        Official official = oList.get(position);
        if (official.getParty() == null) holder.name.setText(official.getName());
        else holder.name.setText(official.getName()+'('+official.getParty()+')');
        holder.office.setText(official.getOffice());
    }

    @Override
    public int getItemCount() {
        return oList.size();
    }


}
