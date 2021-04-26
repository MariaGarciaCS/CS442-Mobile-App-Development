package com.marigarci.civicadvocacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //TODO: officials adapter
    //TODO: officials List

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recylcer + Adapter
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }
}