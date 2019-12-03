package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.CalendarSingleSelectionAlertDialog;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarChipContainer implements View.OnClickListener, CalendarSingleSelectionAlertDialog.OnAlertDialogDateListener {
    private boolean isDateSet = false;
    private Date date;

    private Chip date_chip;
    private Activity parent_activity;

    public CalendarChipContainer(final Activity parent_activity) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        date_chip = myLayout.findViewById(R.id.range_calendar_chip);
        resetText();

        if(date_chip.getParent() != null) {
            ((ViewGroup) date_chip.getParent()).removeView(date_chip); // <- fix
        }

        date_chip.setOnClickListener(this);

        resetText();
    }


    @Override
    public void onClick(View v) {
        Dialog da = new CalendarSingleSelectionAlertDialog(parent_activity, this, date);
        da.show();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Chip getCalendarChip(){
        return date_chip;
    }

    private void resetText(){
        date = Calendar.getInstance().getTime();
        date_chip.setText(Utils.formatDateToString(date));
    }

    @Override
    public void onAlertDialogDateListener(Dialog dialog, Date chosenDate) {
        if(chosenDate == null){
            return;
        }

        this.date = chosenDate;
        date_chip.setText(Utils.formatDateToString(chosenDate));
    }
}
