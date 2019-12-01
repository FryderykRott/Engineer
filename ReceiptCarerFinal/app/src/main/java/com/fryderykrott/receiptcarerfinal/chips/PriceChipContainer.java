package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.PriceSelectionAlertDialog;
import com.google.android.material.chip.Chip;

public class PriceChipContainer implements View.OnClickListener, PriceSelectionAlertDialog.OnAlertDialogPriceListener {

    private boolean isDateSet = false;

    private double price;

    private Chip price_chip;
    private Activity parent_activity;

    public PriceChipContainer(final Activity parent_activity) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        price_chip = myLayout.findViewById(R.id.range_price_chip);

        if(price_chip.getParent() != null) {
            ((ViewGroup) price_chip.getParent()).removeView(price_chip); // <- fix
        }

        price_chip.setOnClickListener(this);
        price_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetText();
            }
        });
        resetChip();
    }


    @Override
    public void onClick(View v) {
        PriceSelectionAlertDialog da = new PriceSelectionAlertDialog(parent_activity, this, price);
        da.show();
    }

    private void resetText(){
        price = 0;
        price_chip.setText("0 PLN");
    }

    public double getPrice() {
        return price;
    }

    public Chip getPriceChip(){
        return price_chip;
    }


    public boolean isDateSet() {
        return isDateSet;
    }

    public void resetChip(){
        resetText();
    }

    @Override
    public void onAlertDialogPriceListener(Dialog dialog, float price) {
        this.price = price;

        price_chip.setText(String.format("%.2f", price) + " PLN");
    }

    public void setPriceOnChip(double sumTotal) {
        price = sumTotal;
        price_chip.setText(String.format("%.2f", sumTotal) + " PLN");
    }
}

