package com.marigarci.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    public double tipPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();

        switch (v.getId()){
            case R.id.twelvePercent:
                if (checked){tipPercent = 0.12;}
            case R.id.fifteenPercent:
                if (checked){tipPercent = 0.15;}
            case R.id.eighteenPercent:
                if (checked){tipPercent = 0.18;}
            case R.id.twentyPercent:
                if (checked){tipPercent = 0.2;}
        }
    }
}