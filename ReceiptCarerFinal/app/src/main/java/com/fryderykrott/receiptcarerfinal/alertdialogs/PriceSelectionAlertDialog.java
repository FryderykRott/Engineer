package com.fryderykrott.receiptcarerfinal.alertdialogs;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fryderykrott.receiptcarerfinal.R;

public class PriceSelectionAlertDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;

    EditText priceEditText;

    private OnAlertDialogPriceListener ls;
    Double price;
    //    public static final int
    public PriceSelectionAlertDialog(Activity a, OnAlertDialogPriceListener ls, Double price) {
        super(a);
        this.ls = ls;
        parent_activity = a;
        this.price = price;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.alert_dialog_price_selection);
        priceEditText = findViewById(R.id.warranty_bar);
        priceEditText.setText(String.format("%.2f", price));

        priceEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                priceEditText.setText("");
                return true;
            }
        });
        ok = findViewById(R.id.button_ok);
        cancel = findViewById(R.id.button_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                String fromNumberString = priceEditText.getText().toString();
                Float convertedToFloat = convertToFloat(fromNumberString);

                if(convertedToFloat == null){
                    dismiss();
                    return;
                }

                ls.onAlertDialogPriceListener(this, convertedToFloat);
                break;
            case R.id.button_cancel:

                break;
            default:
                break;

        }
        dismiss();
    }

    private Float convertToFloat(String numberString){
        if(numberString.isEmpty())
            return null;
        String decimal_1 = ",";
        String decimal_2 = ".";
        numberString = numberString.replace(decimal_1, decimal_2);
        return Float.parseFloat(numberString);
    }

    public interface OnAlertDialogPriceListener {
        public void onAlertDialogPriceListener(Dialog dialog, float price);
    }
}
