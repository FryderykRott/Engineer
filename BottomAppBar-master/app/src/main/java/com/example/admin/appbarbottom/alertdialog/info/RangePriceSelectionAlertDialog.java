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

public class RangePriceSelectionAlertDialog extends MyDialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;

    EditText toPriceEditText;
    EditText fromPriceEditText;

    private OnAlertDialogRangePriceListener ls;

    //    public static final int
    public RangePriceSelectionAlertDialog (Activity a, OnAlertDialogRangePriceListener ls) {
        super(a, MyDialog.CALENDAR_RANGE_SELCTION_ALERT_DIALOG);
        this.ls = ls;
        parent_activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.alert_dialog_price_range_selection);
        fromPriceEditText = findViewById(R.id.warranty_bar);
        toPriceEditText = findViewById(R.id.to_price_edit_text);

        ok = findViewById(R.id.button_ok);
        cancel = findViewById(R.id.button_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                String fromNumberString = fromPriceEditText.getText().toString();
                String toNumberString = toPriceEditText.getText().toString();

                if(fromNumberString.isEmpty() || toNumberString.isEmpty()){
                    break;
                }


                ls.OnAlertDialogRangeDateListener(this, convertToFloat(fromNumberString), convertToFloat(toNumberString));
                break;
            case R.id.button_cancel:

                break;
            default:
                break;

        }
        dismiss();
    }

    private float convertToFloat(String numberString){
        return Float.parseFloat(numberString);
    }


}

