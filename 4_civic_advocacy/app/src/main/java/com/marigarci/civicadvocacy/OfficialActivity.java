package com.marigarci.civicadvocacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    private Intent intent;
    private Official official;

    private TextView locationTxt;
    private TextView officeTxt;
    private TextView nameTxt;
    private TextView partyTxt;
    private TextView addressTxt;
    private TextView phoneTxt;
    private TextView emailTxt;
    private TextView webTxt;

    private ImageButton headShotImg;
    private ImageButton facebookImg;
    private ImageButton twitterImg;
    private ImageButton youtubeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        //TextViews
        locationTxt = (TextView) findViewById(R.id.oa_location);
        officeTxt = (TextView) findViewById(R.id.oa_office);
        nameTxt = (TextView) findViewById(R.id.oa_name);
        partyTxt = (TextView) findViewById(R.id.oa_party);
        addressTxt = (TextView) findViewById(R.id.oa_address);
        phoneTxt = (TextView) findViewById(R.id.oa_phone);
        emailTxt = (TextView) findViewById(R.id.oa_email);
        webTxt = (TextView) findViewById(R.id.oa_website);
        headShotImg = (ImageButton) findViewById(R.id.oa_photo);

        //Images
        facebookImg = (ImageButton) findViewById(R.id.facebook);
        twitterImg = (ImageButton) findViewById(R.id.twitter);
        youtubeImg = (ImageButton) findViewById(R.id.youtube);

        intent = getIntent();
        locationTxt.setText(intent.getCharSequenceExtra("location"));
        official = (Official) intent.getSerializableExtra("official");
        officeTxt.setText(official.getOffice());
        nameTxt.setText(official.getName());

        if (official.getParty() != null) {

            if (official.getParty().equals("Republican")){
                getWindow().getDecorView().setBackgroundColor(Color.RED);
                partyTxt.setText('('+official.getParty()+')');
            }
            else if (official.getParty().equals("Democratic")) {
                partyTxt.setText('('+official.getParty()+')');
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            }
            else getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        } else getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        if (official.getAddress() != null) addressTxt.setText(official.getAddress());
        if (official.getPhone() != null) phoneTxt.setText(official.getPhone());
        if (official.getEmail() != null) emailTxt.setText(official.getEmail());
        if (official.getWebsite() != null) webTxt.setText(official.getWebsite());

        if (official.getImage() != null){
            Picasso.get().load(official.getImage())
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(headShotImg);
        }

        if (official.getSocials() == null) {
            facebookImg.setVisibility(View.INVISIBLE);
            youtubeImg.setVisibility(View.INVISIBLE);
            twitterImg.setVisibility(View.INVISIBLE);
        }else{
            if (official.getSocials().getFacebook() == null)
                facebookImg.setVisibility(View.INVISIBLE);
            if (official.getSocials().getYoutube() == null)
                youtubeImg.setVisibility(View.INVISIBLE);
            if (official.getSocials().getTwitter() == null)
                twitterImg.setVisibility(View.INVISIBLE);
        }
    }
}