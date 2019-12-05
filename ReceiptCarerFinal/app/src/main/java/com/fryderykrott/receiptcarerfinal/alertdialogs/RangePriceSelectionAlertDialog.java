package com.fryderykrott.receiptcarerfinal.alertdialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.fryderykrott.receiptcarerfinal.R;

public class RangePriceSelectionAlertDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;

    EditText toPriceEditText;
    EditText fromPriceEditText;

    private OnAlertDialogRangePriceListener ls;

    //    public static final int
    public RangePriceSelectionAlertDialog (Activity a, OnAlertDialogRangePriceListener ls) {
        super(a);
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

                boolean pass = true;
                if(fromNumberString.isEmpty()){
                    fromPriceEditText.setError("To pole nie może być puste");
                    pass = false;
                }

                if (toNumberString.isEmpty()){
                    toPriceEditText.setError("To pole nie może być puste");
                    pass = false;
                }
                if(pass){
                    float from = convertToFloat(fromNumberString);
                    float to = convertToFloat(toNumberString);

                    if(from > to){
                        fromPriceEditText.setError("'od' nie może być większe 'do'");
                        toPriceEditText.setError("'do' nie może być mniejsze 'od'");
                        pass = false;
                    }

                }

                if (pass){
                    ls.OnAlertDialogRangeDateListener(this, convertToFloat(fromNumberString), convertToFloat(toNumberString));
                    dismiss();
                }
                break;
            case R.id.button_cancel:
                dismiss();
                break;
            default:
                break;

        }

    }

    private float convertToFloat(String numberString){
        return Float.parseFloat(numberString);
    }

    public interface OnAlertDialogRangePriceListener {
        public void OnAlertDialogRangeDateListener(Dialog dialog, float from, float to);
    }

}

