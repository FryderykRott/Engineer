package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.CalendarRangeSelectionAlertDialog;
import com.fryderykrott.receiptcarerfinal.alertdialogs.RangePriceSelectionAlertDialog;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RangeCalendarChipContainer implements View.OnClickListener, CalendarRangeSelectionAlertDialog.OnAlertDialogRangeDateListener {
    private boolean isDateSet = false;
    private Date from;
    private Date to;

    private Chip date_range_chip;
    private Activity parent_activity;

    public RangeCalendarChipContainer(final Activity parent_activity) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        date_range_chip = myLayout.findViewById(R.id.range_calendar_chip);

        if(date_range_chip.getParent() != null) {
            ((ViewGroup)date_range_chip.getParent()).removeView(date_range_chip); // <- fix
        }

        date_range_chip.setOnClickListener(this);
        date_range_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateSet = false;
                date_range_chip.setCloseIconVisible(false);
                resetText();
            }
        });
    }

    OnChipAnwserListener onChipAnwserListener;
    public RangeCalendarChipContainer(final Activity parent_activity, final OnChipAnwserListener onChipAnwserListener) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        date_range_chip = myLayout.findViewById(R.id.range_calendar_chip);

        if(date_range_chip.getParent() != null) {
            ((ViewGroup)date_range_chip.getParent()).removeView(date_range_chip); // <- fix
        }

        date_range_chip.setOnClickListener(this);
        date_range_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateSet = false;
                date_range_chip.setCloseIconVisible(false);
                resetText();
                if(onChipAnwserListener != null)
                    onChipAnwserListener.onChipAnwser();
            }
        });

        this.onChipAnwserListener = onChipAnwserListener;
    }

    @Override
    public void onClick(View v) {
        CalendarRangeSelectionAlertDialog da = new CalendarRangeSelectionAlertDialog(parent_activity, this);
        da.show();
    }

    @Override
    public void OnAlertDialogRangeDateListener(Dialog dialog, Calendar from, Calendar to) {
        if(from == null && to == null){
            return;
        }
        else if (to == null){
            this.from = from.getTime();
            date_range_chip.setText(formatCalendarToString(from));
        }
        else {
            this.from = from.getTime();
            this.to = to.getTime();
            date_range_chip.setText(String.format("%s - %s", formatCalendarToString(from), formatCalendarToString(to)));
        }

        date_range_chip.setCloseIconVisible(true);
        isDateSet = true;

        if(onChipAnwserListener != null)
            onChipAnwserListener.onChipAnwser();
    }

    private String formatCalendarToString(Calendar c){
        SimpleDateFormat format = new SimpleDateFormat(Utils.date_format, Locale.US);
        String dateString = format.format( c.getTime());
        return dateString;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Chip getRangeCalendarChip(){
        return date_range_chip;
    }

    private void resetText(){
        date_range_chip.setText("Wybierz datę");
    }

    public boolean isDateSet() {
        return isDateSet;
    }

    public void resetChip(){
        isDateSet = false;
        from = null;
        to = null;
        resetText();
    }


}

