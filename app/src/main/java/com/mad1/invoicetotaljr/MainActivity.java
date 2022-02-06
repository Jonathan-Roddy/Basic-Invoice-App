package com.mad1.invoicetotaljr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    // define variables for the widgets
    public EditText subtotalEditText;
    public TextView discountPercent;
    public TextView discountAmount;
    public TextView totalTextView;

    // define the SharedPreferences object
    private SharedPreferences savedValues;
    // calculate invoice and total
    float invoiceAmount = 0, discountPercentDouble = 0;
    // define instance variables that should be saved
    private String subtotalString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to the widgets
        subtotalEditText = (EditText) findViewById(R.id.etxSubtotal);
        discountPercent = (TextView) findViewById(R.id.discountPercent);
        discountAmount = (TextView) findViewById(R.id.discountAmount);
        totalTextView = (TextView) findViewById(R.id.totalText);

        // set the listeners
        subtotalEditText.setOnEditorActionListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("subtotalString", subtotalString);
        editor.putFloat("discountPercentDouble", discountPercentDouble);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        subtotalString = savedValues.getString("subtotalString", "");
        //discountAmount = savedValues.getFloat("discountAmount", 0.10f);

        // set the bill amount on its widget
        //billAmountEditText.setText(billAmountString);

        // calculate and display
        calculateAndDisplay();
    }

    public void calculateAndDisplay() {
        // get the bill amount
        subtotalString = subtotalEditText.getText().toString();
        float billAmount;
        if (subtotalString.equals("")) {
            billAmount = 0;
        }
        else {
            billAmount = Float.parseFloat(subtotalString);
        }

        if(billAmount >= 200) discountPercentDouble = (float) 0.20;
        else if(billAmount <= 199 && billAmount >= 100) discountPercentDouble = (float) 0.10;
        else if(billAmount <= 99 && billAmount >= 0) discountPercentDouble = (float) 0.00;
        else discountPercentDouble = (float) 0.00;

        float totalAmount = billAmount * discountPercentDouble;

        // display the other results with formatting
        NumberFormat percent = NumberFormat.getPercentInstance();
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        discountPercent.setText(percent.format(discountPercentDouble));
        discountAmount.setText(currency.format(totalAmount));
        totalTextView.setText(currency.format(billAmount));
    }

   @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}