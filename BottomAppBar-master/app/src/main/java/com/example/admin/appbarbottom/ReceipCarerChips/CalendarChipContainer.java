package com.example.admin.appbarbottom.ReceipCarerChips;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.MyDialog;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogDateListener;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.admin.appbarbottom.ReceiptCarerUtils.Utils.date_format;

public class CalendarChipContainer implements View.OnClickListener, OnAlertDialogDateListener {
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
        date_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateSet = false;
                date_chip.setCloseIconVisible(false);
                resetText();
            }
        });
    }


    @Override
    public void onClick(View v) {
        Dialog da = AlertDialogInfoBuilder.buildCalendarChooser(parent_activity, this);
        da.show();
    }

    @Override
    public void onAlertDialogDateListener(MyDialog dialog, Date chosenDate) {
        if(chosenDate == null){
            return;
        }

        this.date = chosenDate;
        date_chip.setText(formatCalendarToString(chosenDate));

        date_chip.setCloseIconVisible(true);
        isDateSet = true;
    }


    private String formatCalendarToString(Date date){
        SimpleDateFormat format = new SimpleDateFormat(date_format, Locale.US);
        String dateString = format.format(date);
        return dateString;
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
        date_chip.setText("_._._");
    }

    public boolean isDateSet() {
        return isDateSet;
    }

}
