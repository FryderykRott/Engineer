package com.example.admin.appbarbottom.ReceipCarerChips;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.MyDialog;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogPriceListener;
import com.google.android.material.chip.Chip;

public class PriceChipContainer implements View.OnClickListener, OnAlertDialogPriceListener {

    private boolean isDateSet = false;

    private float price;

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
                isDateSet = false;
                price_chip.setCloseIconVisible(false);
                resetText();
            }
        });
        resetChip();
    }


    @Override
    public void onClick(View v) {
        Dialog da = AlertDialogInfoBuilder.buildPriceChooser(parent_activity, this);
        da.show();
    }

    private void resetText(){
        price_chip.setText("_ PLN");
    }

    public float getPrice() {
        return price;
    }

    public Chip getPriceChip(){
        return price_chip;
    }


    public boolean isDateSet() {
        return isDateSet;
    }

    public void resetChip(){
        isDateSet = false;
        price = 0;
        resetText();
    }

    @Override
    public void onAlertDialogPriceListener(MyDialog dialog, float price) {
        this.price = price;

        price_chip.setText(price + " PLN");
        price_chip.setCloseIconVisible(true);
        isDateSet = true;
    }
}
