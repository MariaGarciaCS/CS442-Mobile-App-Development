package com.marigarci.civicadvocacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OfficialAdapter oAdapter;
    private ArrayList<Official> olist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recylcer + Adapter
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        oAdapter = new OfficialAdapter(olist, this);
        recyclerView.setAdapter(oAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}