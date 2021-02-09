package com.marigarci.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private EditText billTotal;
    private EditText numPeople;
    private TextView tipAmount;
    private TextView totalWithTip;
    private TextView totalPerPerson;
    private TextView overage;
    public double billTotalVal;
    public double tipPercentVal;
    public double tipAmountVal;
    public double totalWithTipVal;
    public int numPeopleVal;
    public double totalPerPersonVal;
    public double overageVal;

    public DecimalFormat df2 = new DecimalFormat("#.##");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        billTotal = findViewById(R.id.billTotalValue);
        numPeople = findViewById(R.id.numPeopleValue);
        tipAmount = findViewById(R.id.tipAmountValue);
        totalWithTip = findViewById(R.id.totalWithTipValue);
        totalPerPerson = findViewById(R.id.totalPerPersonValue);
        overage = findViewById(R.id.overageValue);
    }

    public void onRadioButtonClicked(View v){
        if(v.getId() == R.id.twelvePercent){tipPercentVal = .12; }
        else if(v.getId() == R.id.fifteenPercent){tipPercentVal = .15;}
        else if(v.getId() == R.id.eighteenPercent){tipPercentVal = .18;}
        else if(v.getId() == R.id.twentyPercent){tipPercentVal = .2;}
    }

    public void onGoClicked(View v){
        //Get User Input
        billTotalVal = Double.parseDouble(billTotal.getText().toString());
        numPeopleVal = Integer.parseInt(numPeople.getText().toString());

        //Calculate Values
        tipAmountVal = calcTip(tipPercentVal, billTotalVal);
        totalWithTipVal = calcTotalWTip(tipAmountVal, billTotalVal);
        totalPerPersonVal = calcTotalPerPerson(numPeopleVal, billTotalVal);
        overageVal = calcOverage(numPeopleVal, totalPerPersonVal, billTotalVal);

        //Display Values
        tipAmount.setText("$" + df2.format(tipAmountVal));
        totalWithTip.setText("$" + df2.format(totalWithTipVal));
        totalPerPerson.setText("$" + df2.format(totalPerPersonVal));
        overage.setText("$" + df2.format(overageVal));
    }

    public void onClearClicked(View v){
        billTotal.getText().clear();
        numPeople.getText().clear();
        tipAmount.setText(R.string.startValue);
        totalWithTip.setText(R.string.startValue);
        totalPerPerson.setText(R.string.startValue);
        overage.setText(R.string.startValue);
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
    public double calcOverage(int numPeople, double costPerPerson, double billTotal){
        return 0;
    }
}