package com.example.admin.appbarbottom.ReceipCarerChips;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.MyDialog;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogRangePriceListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

public class RangePriceChipContainer implements View.OnClickListener, OnAlertDialogRangePriceListener {

    private boolean isDateSet = false;

    private float from;
    private float to;

    private Chip price_range_chip;
    private Activity parent_activity;

    public RangePriceChipContainer(final Activity parent_activity) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        price_range_chip = myLayout.findViewById(R.id.range_price_chip);

        if(price_range_chip.getParent() != null) {
            ((ViewGroup) price_range_chip.getParent()).removeView(price_range_chip); // <- fix
        }

        price_range_chip.setOnClickListener(this);
        price_range_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateSet = false;
                price_range_chip.setCloseIconVisible(false);
                resetText();
            }
        });
    }


    @Override
    public void onClick(View v) {
        Dialog da = AlertDialogInfoBuilder.buildRangePriceChooser(parent_activity, this);
        da.show();
    }

    @Override
    public void OnAlertDialogRangeDateListener(MyDialog dialog, float from, float to) {
        this.from = from;
        this.to = to;

        if(from > to)
        {
            Snackbar snackbar = Snackbar
                    .make(parent_activity.findViewById(R.id.coordinatorLayout), "Wrong format: from > to", Snackbar.LENGTH_LONG);
            snackbar.show();

            return;
        }

        price_range_chip.setText(String.format("%s - %s", from, to) + " PLN");
        price_range_chip.setCloseIconVisible(true);
        isDateSet = true;
    }

    private void resetText(){
        price_range_chip.setText("_ - _ PLN");
    }

    public float getFrom() {
        return from;
    }

    public float  getTo() {
        return to;
    }

    public Chip getRangePriceChip(){
        return price_range_chip;
    }


    public boolean isDateSet() {
        return isDateSet;
    }

    public void resetChip(){
        isDateSet = false;
        from = 0;
        to = 0;
        resetText();
    }
}
