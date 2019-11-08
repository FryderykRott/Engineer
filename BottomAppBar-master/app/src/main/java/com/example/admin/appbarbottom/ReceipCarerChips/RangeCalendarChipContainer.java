package com.example.admin.appbarbottom.ReceipCarerChips;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.MyDialog;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogRangeDateListener;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.admin.appbarbottom.ReceiptCarerUtils.Utils.date_format;

public class RangeCalendarChipContainer implements View.OnClickListener, OnAlertDialogRangeDateListener {
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


    @Override
    public void onClick(View v) {
        Dialog da = AlertDialogInfoBuilder.buildRangeCalendarChooser(parent_activity, this);
        da.show();
    }

    @Override
    public void OnAlertDialogRangeDateListener(MyDialog dialog, Calendar from, Calendar to) {
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
    }

    private String formatCalendarToString(Calendar c){
        SimpleDateFormat format = new SimpleDateFormat(date_format, Locale.US);
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
        date_range_chip.setText("_._._ - _._._");
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
