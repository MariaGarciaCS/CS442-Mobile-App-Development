package com.marigarci.civicadvocacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    private String TAG = "PhotoActivity";

    private Intent intent;
    private Official official;

    private TextView locationTxt;
    private TextView officeTxt;
    private TextView nameTxt;
    private ImageView img;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        locationTxt = (TextView) findViewById(R.id.photo_location);
        officeTxt = (TextView) findViewById(R.id.photo_office);
        nameTxt = (TextView) findViewById(R.id.photo_name);
        img = (ImageView) findViewById(R.id.photo_img);

        intent = getIntent();
        official = (Official) intent.getSerializableExtra("official");
        CharSequence c = intent.getCharSequenceExtra("location");

        locationTxt.setText(intent.getCharSequenceExtra("location"));
        officeTxt.setText(official.getOffice());
        nameTxt.setText(official.getName());

        if (official.getParty() != null) {
            if (official.getParty().equals("Republican")) getWindow().getDecorView().setBackgroundColor(Color.RED);
            else if (official.getParty().equals("Democratic")) getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            else getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        } else getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        if (official.getImage() != null) {
            Picasso.get().load(official.getImage())
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(img);
        }

    }
}