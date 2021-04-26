package com.marigarci.civicadvocacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

    //Options Menu---------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.info_btn:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_btn:
                //TODO: add Search

            default:return super.onOptionsItemSelected(item);
        }
    }

}