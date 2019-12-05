package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.RangePriceSelectionAlertDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

public class RangePriceChipContainer implements View.OnClickListener, RangePriceSelectionAlertDialog.OnAlertDialogRangePriceListener {

    private boolean isDateSet = false;

    private float from;
    private float to;

    private Chip price_range_chip;
    private Activity parent_activity;
    OnChipAnwserListener onChipAnwserListener;

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

    public RangePriceChipContainer(final Activity parent_activity, final OnChipAnwserListener onChipAnwserListener) {
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
                if(onChipAnwserListener != null)
                    onChipAnwserListener.onChipAnwser();
            }
        });

        this.onChipAnwserListener = onChipAnwserListener;
    }

    @Override
    public void onClick(View v) {
        RangePriceSelectionAlertDialog da = new RangePriceSelectionAlertDialog(parent_activity, this);
        da.show();
    }

    @Override
    public void OnAlertDialogRangeDateListener(Dialog dialog, float from, float to) {
        this.from = from;
        this.to = to;

        price_range_chip.setText(String.format("%s - %s", from, to) + " PLN");
        price_range_chip.setCloseIconVisible(true);
        isDateSet = true;

        if(onChipAnwserListener != null)
            onChipAnwserListener.onChipAnwser();
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


    public boolean isPriceSet() {
        return isDateSet;
    }

    public void resetChip(){
        isDateSet = false;
        from = 0;
        to = 0;
        resetText();
    }
}
