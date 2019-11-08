package com.example.admin.appbarbottom.alertdialog.info;

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

import com.example.admin.appbarbottom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarSingleSelectionAlertDialog extends MyDialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;
    Date chossen_date = Calendar.getInstance().getTime();

    CalendarView calendarView;
    OnAlertDialogDateListener l;
    //    public static final int
    public CalendarSingleSelectionAlertDialog(Activity a, OnAlertDialogDateListener l) {
        super(a, MyDialog.CALENDAR_SINGLE_SELECTION_ALERT_DIALOG);

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
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    chossen_date = sdf.parse(year+"-"+month+"-"+dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                chossen_date = new Date(view.getDate());
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

    private static Calendar toCalendar(Long ldate){
        Date date = new Date(ldate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

}
