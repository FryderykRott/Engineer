package com.example.admin.appbarbottom.alertdialog.info;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.appbarbottom.R;

public class PriceSelectionAlertDialog extends MyDialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;

    EditText priceEditText;

    private OnAlertDialogPriceListener ls;

    //    public static final int
    public PriceSelectionAlertDialog (Activity a, OnAlertDialogPriceListener ls) {
        super(a, MyDialog.CALENDAR_RANGE_SELCTION_ALERT_DIALOG);
        this.ls = ls;
        parent_activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.alert_dialog_price_selection);
        priceEditText = findViewById(R.id.warranty_bar);

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

                ls.onAlertDialogPriceListener(this, convertToFloat(fromNumberString));
                break;
            case R.id.button_cancel:

                break;
            default:
                break;

        }
        dismiss();
    }

    private float convertToFloat(String numberString){
        if(numberString.isEmpty())
            return 0.f;

        return Float.parseFloat(numberString);
    }


}

