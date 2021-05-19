package com.marigarci.civicadvocacy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    private myLocation mSearchLocation;
    private Official mOfficial;
    private ImageView logo_image1;
    private TextView mOfficeNameTextView;
    private TextView mOfficialNameTextView;
    private TextView mLocationTextView;
    private ImageView mOfficialPhotoImageView;


    private ConstraintLayout mConstraintLayout;
    private static final String TAG = "PhotoDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mConstraintLayout = (ConstraintLayout)findViewById(R.id.activity_photo_detail_constraintlayout);

        mLocationTextView = (TextView) findViewById(R.id.location_textview);
        mOfficeNameTextView = (TextView) findViewById(R.id.officename_textview);
        mOfficialNameTextView = (TextView) findViewById(R.id.officialnameparty_textview);
        mOfficialPhotoImageView = (ImageView) findViewById(R.id.officialphoto_imageview);

        mSearchLocation = getIntent().getExtras().getParcelable("SearchLocation");
        mOfficial = getIntent().getExtras().getParcelable("Official");

        mLocationTextView.setText(mSearchLocation.getCity() + ", " + mSearchLocation.getState() + " " + mSearchLocation.getZip());
        mOfficeNameTextView.setText(mOfficial.getOffice());
        mOfficialNameTextView.setText(mOfficial.getName());
        //set logo of party
        logo_image1 =(ImageView) findViewById(R.id.logo_image1);

        downloadProfilePhoto();
        setActivityBackgroundColor();
    }

    public boolean checkNtwConnectivity() {
        ConnectivityManager connection_manager = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connection_manager.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }

    private void setActivityBackgroundColor() {
        Log.d(TAG, "setActivityBackgroundColor: "+mOfficial.getParty());
        if (mOfficial.getParty().equalsIgnoreCase("democratic Party")) {
            mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.blue));
            mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.blue));
            mLocationTextView.setBackgroundColor(getResources().getColor(R.color.purple));
            logo_image1.setImageResource(R.drawable.dem_logo);
            // click on logo
            logo_image1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick:logo democratic");
                    String url = "https://democrats.org/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            });
        } else if (mOfficial.getParty().equalsIgnoreCase("republican Party")) {
            mConstraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            mConstraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            mLocationTextView.setBackgroundColor(getResources().getColor(R.color.purple));
            logo_image1.setImageResource(R.drawable.rep_logo);

            //click on logo
            logo_image1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "onClick:logo republican");
                    String url = "https://www.gop.com/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            });
        } else {
            mConstraintLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
            mConstraintLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
            mLocationTextView.setBackgroundColor(getResources().getColor(R.color.purple));
            logo_image1.setVisibility(View.INVISIBLE);
        }
    }

    private void downloadProfilePhoto() {
        if (mOfficial.getImage() != null || !mOfficial.getImage().equals("")) {
            if(checkNtwConnectivity()){
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// Here we try https if the http image attempt failed
                        final String changedUrl = mOfficial.getImage().replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.missing)
                                .placeholder(R.drawable.placeholder)
                                .into(mOfficialPhotoImageView);
                    }
                }).build();
                picasso.load(mOfficial.getImage())
                        .error(R.drawable.missing)
                        .placeholder(R.drawable.placeholder)
                        .into(mOfficialPhotoImageView);
            }else{
                mOfficialPhotoImageView.setImageDrawable(getResources().getDrawable(R.drawable.brokenimage));
            }

        } else {
            Picasso.get().load(mOfficial.getImage())
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(mOfficialPhotoImageView);
        }
    }
}