package com.marigarci.civicadvocacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity  implements View.OnClickListener{
    private TextView locationTxt;
    private TextView officeTxt;
    private TextView mOfficialNameTextView;
    private TextView mPartyNameTextView;
    private TextView mAddressValueTextView;
    private TextView mPhoneValueTextView;
    private TextView emailTxt;
    private TextView mWebsiteValueTextView;
    private ImageView mFaceBookImageView;
    private ImageView mTwitterImageView;
    private ImageView mYouTubeImageView;
    private ImageView mOfficialPhotoImageView;
    private myLocation mSearchLocation;
    private Official mOfficial;
    private ScrollView mScrollView;
    private ConstraintLayout mConstraintLayout;
    private ImageView logo_image;

    private TextView addresstitle_textview;
    private TextView addressvalue_textview;

    private TextView phonetitle_textview;
    private TextView phonevalue_textview;

    private TextView emailtitle_textview;

    private  TextView websitetitle_textview;
    private static final String TAG = "OfficialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        mScrollView = (ScrollView) findViewById(R.id.activity_official_scrollview);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.activity_official_constraintlayout);
        locationTxt = (TextView) findViewById(R.id.location_textview);
        officeTxt = (TextView) findViewById(R.id.officename_textview);
        mOfficialNameTextView = (TextView) findViewById(R.id.name_textview);
        mPartyNameTextView = (TextView) findViewById(R.id.partyname_textview);
        mAddressValueTextView = (TextView) findViewById(R.id.addressvalue_textview);
        mPhoneValueTextView = (TextView) findViewById(R.id.phonevalue_textview);
        emailTxt = (TextView) findViewById(R.id.emailvalue_textview);
        mWebsiteValueTextView = (TextView) findViewById(R.id.websitevalue_textview);

        mFaceBookImageView = (ImageView) findViewById(R.id.facebook_imageview);
        mTwitterImageView = (ImageView) findViewById(R.id.twitter_imageview);
        mYouTubeImageView = (ImageView) findViewById(R.id.youtube_imageview);
        mOfficialPhotoImageView = (ImageView) findViewById(R.id.officialphoto_imageview);

        //set logo of party
        logo_image =(ImageView) findViewById(R.id.logo_image);

        //address both textview
        addresstitle_textview = findViewById(R.id.addresstitle_textview);
        addressvalue_textview = findViewById(R.id.addressvalue_textview);

        //phone both textview
        phonetitle_textview = findViewById(R.id.phonetitle_textview);
        phonevalue_textview = findViewById(R.id.phonevalue_textview);


        //email textview
        emailtitle_textview = findViewById(R.id.emailtitle_textview);

        //Website Textview
        websitetitle_textview = findViewById(R.id.websitetitle_textview);


        mFaceBookImageView.setOnClickListener(this);
        mTwitterImageView.setOnClickListener(this);
        mYouTubeImageView.setOnClickListener(this);
        mOfficialPhotoImageView.setOnClickListener(this);

        mSearchLocation = getIntent().getExtras().getParcelable("SearchLocation");
        mOfficial = getIntent().getExtras().getParcelable("Official");

        locationTxt.setText(mSearchLocation.getCity() + ", " + mSearchLocation.getState() + " " + mSearchLocation.getZip());
        officeTxt.setText(mOfficial.getOffice());
        mOfficialNameTextView.setText(mOfficial.getName());
        mPartyNameTextView.setText("(" + mOfficial.getParty() + ")");
        mAddressValueTextView.setText(mOfficial.getAddress() + "\n" + mOfficial.getCity() + ", " + mOfficial.getState() + " " + mOfficial.getZip());
        mPhoneValueTextView.setText(mOfficial.getPhone());
        emailTxt.setText(mOfficial.getEmail());
        mWebsiteValueTextView.setText(mOfficial.getWebsite());
        downloadProfilePhoto();
        setActivityBackgroundColor();


        Linkify.addLinks(mWebsiteValueTextView, Linkify.WEB_URLS);
        Linkify.addLinks(mPhoneValueTextView, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(mAddressValueTextView, Linkify.MAP_ADDRESSES);
        Linkify.addLinks(emailTxt, Linkify.EMAIL_ADDRESSES);

        testUnavailableData();

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

    private void testUnavailableData(){
        if(mOfficial.getAddress() == null){

            addresstitle_textview.setVisibility(View.GONE);
            addressvalue_textview.setVisibility(View.GONE);
        }

        if(mOfficial.getPhone().toString().equals("No Data Provided")){
            phonetitle_textview.setVisibility(View.GONE);
            phonevalue_textview.setVisibility(View.GONE);
        }

        if(mOfficial.getEmail().toString().equals("No Data Provided")){
            emailtitle_textview.setVisibility(View.GONE);
            emailTxt.setVisibility(View.GONE);
        }

        if(mOfficial.getWebsite().toString().equals("No Data Provided")){
            websitetitle_textview.setVisibility(View.GONE);
            mWebsiteValueTextView.setVisibility(View.GONE);
        }

        //youtube icon
        if(mOfficial.getSocials().get("YouTube") == null){
            mYouTubeImageView.setVisibility(View.GONE);
        }

        //Twitter icon
        if(mOfficial.getSocials().get("Twitter") == null){
            mTwitterImageView.setVisibility(View.GONE);
        }

        //Facebook icon
        if(mOfficial.getSocials().get("Facebook") == null){
            mFaceBookImageView.setVisibility(View.GONE);
        }

    }

    private void setActivityBackgroundColor() {
        if (mOfficial.getParty().equalsIgnoreCase("democratic party")) {
            mScrollView.setBackgroundColor(getResources().getColor(R.color.blue));
            mConstraintLayout.setBackgroundColor(getResources().getColor(R.color.blue));
            locationTxt.setBackgroundColor(getResources().getColor(R.color.purple));
            logo_image.setImageResource(R.drawable.dem_logo);
            // click on logo
            logo_image.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // Log.d(TAG, "onClick:logo democratic");
                    String url = "https://democrats.org/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            });
        } else if (mOfficial.getParty().equalsIgnoreCase("republican party")) {
            mScrollView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            mConstraintLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            locationTxt.setBackgroundColor(getResources().getColor(R.color.purple));
            logo_image.setImageResource(R.drawable.rep_logo);

            //click on logo
            logo_image.setOnClickListener(new View.OnClickListener(){
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
            mScrollView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mConstraintLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
            locationTxt.setBackgroundColor(getResources().getColor(R.color.purple));
            logo_image.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook_imageview:
                facebookClicked(v);
                break;
            case R.id.twitter_imageview:
                twitterClicked(v);
                break;
            case R.id.youtube_imageview:
                youTubeClicked(v);
                break;
            case R.id.officialphoto_imageview:
                if (mOfficial.getImage() != null && !mOfficial.getImage().equals("") && !mOfficial.getImage().equals("No Data Provided")) {
                    Intent photoDetailIntent = new Intent(this, PhotoActivity.class);
                    photoDetailIntent.putExtra("SearchLocation", mSearchLocation);
                    photoDetailIntent.putExtra("Official", mOfficial);
                    startActivity(photoDetailIntent);
                }
                break;
        }
    }

    public void youTubeClicked(View v) {
        String name = mOfficial.getSocials().get("YouTube");
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + mOfficial.getSocials().get("Facebook");
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                urlToUse = "fb://page/" + mOfficial.getSocials().get("Facebook");
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = mOfficial.getSocials().get("Twitter");
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    private void downloadProfilePhoto() {

        Log.d(TAG, "downloadProfilePhoto: come here");
        if (mOfficial.getImage() != null && !mOfficial.getImage().equals("") && !mOfficial.getImage().equals("No Data Provided")) {
            if(checkNtwConnectivity()){
                Log.d(TAG, "downloadProfilePhoto: 1");

                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        picasso.setLoggingEnabled(true);
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

            }else {
                Log.d(TAG, "downloadProfilePhoto: 2");
                mOfficialPhotoImageView.setImageDrawable(getResources().getDrawable(R.drawable.brokenimage));
            }

        } else {
            Log.d(TAG, "downloadProfilePhoto: 3");
            mOfficialPhotoImageView.setImageDrawable(getResources().getDrawable(R.drawable.missing));
        }
    }
}