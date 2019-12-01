package com.fryderykrott.receiptcarerfinal.alertdialogs;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarSingleSelectionAlertDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;
    Date chossen_date = Calendar.getInstance().getTime();

    CalendarView calendarView;
    OnAlertDialogDateListener l;

    Date selectedDate;

    //    public static final int
    public CalendarSingleSelectionAlertDialog(Activity a, OnAlertDialogDateListener l, Date selectedDate) {
        super(a);

        this.selectedDate = selectedDate;

        this.l = l;
        parent_activity =  a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.alert_dialog_calendar_single_selection);
        calendarView = findViewById(R.id.warranty_bar);
        calendarView.setMaxDate(Utils.getTodaysDate().getTime());

        if(selectedDate != null)
            calendarView.setDate(selectedDate.getTime());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String data = "";

                month++;

                data += year + "-";
                if(month < 10)
                    data += "0"+ month + "-";
                else
                    data += month + "-";

                if(dayOfMonth < 10)
                    data += "0" + dayOfMonth;
                else
                    data += dayOfMonth;


                chossen_date = Utils.formatStringToDate(data);
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
                l.onAlertDialogDateListener(this, chossen_date);
                dismiss();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
            default:
                break;
        }

    }


    public interface OnAlertDialogDateListener {
        public void onAlertDialogDateListener(Dialog dialog,  Date chosenDate);
    }

}
