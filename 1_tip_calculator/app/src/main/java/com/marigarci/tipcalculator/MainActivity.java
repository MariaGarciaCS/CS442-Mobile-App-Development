package com.marigarci.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    public double tipPercent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRadioButtonClicked(View v){
        if(v.getId() == R.id.twelvePercent){tipPercent = .12; }
        else if(v.getId() == R.id.fifteenPercent){tipPercent = .15;}
        else if(v.getId() == R.id.eighteenPercent){tipPercent = .18;}
        else if(v.getId() == R.id.twentyPercent){tipPercent = .2;}
    }

    public void onGoClicked(View v){

    }

    public void onClearClicked(View v){

    }

    public double calcTip (double percent, double bill){
        return 0;
    }
    public double calcTotalWTip(double tip, double bill){
        return 0;
    }
    public double calcTotalPerPerson(int numPeople, double bill){
        return 0;
    }
    public double calcOverage(int numPeople, double billTotal){
        return 0;
    }
}