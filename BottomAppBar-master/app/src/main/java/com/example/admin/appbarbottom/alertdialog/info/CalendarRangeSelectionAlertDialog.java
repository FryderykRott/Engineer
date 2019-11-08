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

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.example.admin.appbarbottom.R;

public class CalendarRangeSelectionAlertDialog extends MyDialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;

    public static final int NONE = 0;

    public static final int RANGLE_SELECTION_DISABLE = 100;
    public static final int RANGE_SELECTION_ANABLE = 101;

    CalendarView calendarView;
    DateRangeCalendarView rangeCalendarView;

    int calendar_mode;
    private OnAlertDialogRangeDateListener ls;
    //    public static final int
    public CalendarRangeSelectionAlertDialog(Activity a, OnAlertDialogRangeDateListener ls) {
        super(a, MyDialog.CALENDAR_RANGE_SELCTION_ALERT_DIALOG);
        this.ls = ls;
        parent_activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.alert_dialog_calendar_range_selection);
        rangeCalendarView = findViewById(R.id.warranty_bar);


        ok = findViewById(R.id.button_ok);
        cancel = findViewById(R.id.button_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                ls.OnAlertDialogRangeDateListener(this, rangeCalendarView.getStartDate(), rangeCalendarView.getEndDate());
                break;
            case R.id.button_cancel:

                break;
            default:
                break;

        }
        dismiss();
    }


}
