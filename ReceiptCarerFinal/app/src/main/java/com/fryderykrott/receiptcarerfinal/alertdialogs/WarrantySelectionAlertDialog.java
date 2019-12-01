package com.fryderykrott.receiptcarerfinal.alertdialogs;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.utils.Utils;

import java.util.Date;

public class WarrantySelectionAlertDialog extends Dialog implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;

    SeekBar warrantySeekBar;
    TextView textView;

    Date dateOfWarrantyEnding;
    private OnAlertDialogWarrantyListener ls;

    public WarrantySelectionAlertDialog (Activity a, OnAlertDialogWarrantyListener ls) {
        super(a);
        this.ls = ls;
        parent_activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.alert_dialog_warranty_selection);

        warrantySeekBar = findViewById(R.id.warranty_bar);
        warrantySeekBar.setProgress(0);
        warrantySeekBar.setOnSeekBarChangeListener(this);

        textView = findViewById(R.id.warranty_text_view);
        textView.setText("Brak gwarancji");

        ok = findViewById(R.id.button_ok);
        cancel = findViewById(R.id.button_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
//                TODO setting date of warranty from seekbar
                int days = Utils.convertWarrantyBarToDays(warrantySeekBar.getProgress());
                dateOfWarrantyEnding = Utils.convertDaysToDate(days);
                ls.onAlertDialogWarrantyCallback(dateOfWarrantyEnding);
                break;
            case R.id.button_cancel:

                break;
            default:
                break;

        }
        dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        Jesli zmeini sie progress to należy zmienić także
        textView.setText(Utils.convertWarrantyBarToString(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface OnAlertDialogWarrantyListener {
        public void onAlertDialogWarrantyCallback(Date date_of_end);
    }
}

