package com.marigarci.tipcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
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

    public DecimalFormat df2 = new DecimalFormat("0.00");



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
        radioGroup = findViewById(R.id.tipPercentBtn);
    }

    //BUNDLE
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("BILL_TOTAL", billTotal.getText().toString());
        outState.putString("TIP_AMOUNT", tipAmount.getText().toString());
        outState.putString("NUM_PEOPLE", numPeople.getText().toString());
        outState.putString("TOTAL_WITH_TIP", totalWithTip.getText().toString());
        outState.putString("TOTAL_PER_PERSON", totalPerPerson.getText().toString());
        outState.putString("OVERAGE", overage.getText().toString());
        //TODO: radiobtn
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        billTotal.setText(savedInstanceState.getString("BILL_TOTAL"));
        tipAmount.setText(savedInstanceState.getString("TIP_AMOUNT"));
        numPeople.setText(savedInstanceState.getString("NUM_PEOPLE"));
        totalWithTip.setText(savedInstanceState.getString("TOTAL_WITH_TIP"));
        totalPerPerson.setText(savedInstanceState.getString("TOTAL_PER_PERSON"));
        overage.setText(savedInstanceState.getString("OVERAGE"));
    }


    //BUTTONS
    public void onRadioButtonClicked(View v){
        //check if empty
        if (billTotal.getText().toString().isEmpty()){
            radioGroup.clearCheck();
        }
        else{
            //Get User Input
            billTotalVal = Double.parseDouble(billTotal.getText().toString());

            if(v.getId() == R.id.twelvePercent){tipPercentVal = .12; }
            else if(v.getId() == R.id.fifteenPercent){tipPercentVal = .15;}
            else if(v.getId() == R.id.eighteenPercent){tipPercentVal = .18;}
            else if(v.getId() == R.id.twentyPercent){tipPercentVal = .2;}

            //Calculate Values
            tipAmountVal = calcTip(tipPercentVal, billTotalVal);
            totalWithTipVal = calcTotalWTip(tipAmountVal, billTotalVal);

            //Display Values
            tipAmount.setText("$" + df2.format(tipAmountVal));
            totalWithTip.setText("$" + df2.format(totalWithTipVal));
        }
    }

    public void onGoClicked(View v){
        //Check if empty
        String numPeopleStr = numPeople.getText().toString();
        if (numPeopleStr.isEmpty() || numPeople.getText().toString().equals("0")){
            Toast numPeopleError = Toast.makeText(this, "Number of people cannot be zero or empty", Toast.LENGTH_SHORT);
            numPeopleError.show();

        }
        else {
            //Get Value
            numPeopleVal = Integer.parseInt(numPeople.getText().toString());

            //Calculate Values
            totalPerPersonVal = calcTotalPerPerson(numPeopleVal, totalWithTipVal);
            overageVal = calcOverage(numPeopleVal, totalPerPersonVal, totalWithTipVal);

            //Display Values
            totalPerPerson.setText("$" + df2.format(totalPerPersonVal));
            overage.setText("$" + df2.format(overageVal));
        }
    }

    public void onClearClicked(View v){
        billTotal.getText().clear();
        numPeople.getText().clear();
        tipAmount.setText(R.string.startValue);
        totalWithTip.setText(R.string.startValue);
        totalPerPerson.setText(R.string.startValue);
        overage.setText(R.string.startValue);
        radioGroup.clearCheck();
    }

    //CALCULATIONS
    public double calcTip (double percent, double bill){
        return bill * percent;
    }
    public double calcTotalWTip(double tip, double bill){
        return tip + bill;
    }
    public double calcTotalPerPerson(int numPeople, double bill){
        BigDecimal cost = new BigDecimal(bill / numPeople).setScale(2, RoundingMode.UP);
        return cost.doubleValue();
    }
    public double calcOverage(int numPeople, double costPerPerson, double bill){
        return (numPeople * costPerPerson) - bill;
    }
}