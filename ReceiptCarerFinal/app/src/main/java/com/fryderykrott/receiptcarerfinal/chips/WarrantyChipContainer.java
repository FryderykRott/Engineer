package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.WarrantySelectionAlertDialog;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.android.material.chip.Chip;

import java.util.Date;

//warranty_chip
public class WarrantyChipContainer implements View.OnClickListener, WarrantySelectionAlertDialog.OnAlertDialogWarrantyListener {

    private boolean isWarrantySet = false;

    private Date date_of_end;

    private Chip warranty_chip;
    private Activity parent_activity;

    boolean isInfiniteWarranty = false;

    public WarrantyChipContainer(final Activity parent_activity) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        warranty_chip = myLayout.findViewById(R.id.warranty_chip);

        if(warranty_chip.getParent() != null) {
            ((ViewGroup) warranty_chip.getParent()).removeView(warranty_chip); // <- fix
        }

        warranty_chip.setOnClickListener(this);
        warranty_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWarrantySet = false;
                warranty_chip.setCloseIconVisible(false);
                resetText();
            }
        });
        resetChip();
    }

    OnChipAnwserListener onChipAnwserListener;
    public WarrantyChipContainer(final Activity parent_activity, final OnChipAnwserListener onChipAnwserListener) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        warranty_chip = myLayout.findViewById(R.id.warranty_chip);

        if(warranty_chip.getParent() != null) {
            ((ViewGroup) warranty_chip.getParent()).removeView(warranty_chip); // <- fix
        }

        warranty_chip.setOnClickListener(this);
        warranty_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWarrantySet = false;
                warranty_chip.setCloseIconVisible(false);
                resetText();
                if(onChipAnwserListener != null)
                    onChipAnwserListener.onChipAnwser();
            }
        });
        resetChip();

        this.onChipAnwserListener = onChipAnwserListener;
    }


    @Override
    public void onClick(View v) {
        Dialog da = new WarrantySelectionAlertDialog(parent_activity, this);
        da.show();
    }

    private void resetText(){
        warranty_chip.setText("Brak gwarancji");
    }


    public Chip getWarrantyChip(){
        return warranty_chip;
    }


    public boolean isWarrantySet() {
        return isWarrantySet;
    }

    public void resetChip(){
        isWarrantySet = false;

        resetText();
    }

//    @Override
//    public void onAlertDialogPriceListener(MyDialog dialog, float price) {
//
//    }

    @Override
    public void onAlertDialogWarrantyCallback(Date date_of_end) {
        this.date_of_end = date_of_end;
        setWarranty(date_of_end);
        if(onChipAnwserListener != null)
            onChipAnwserListener.onChipAnwser();

    }

    public Date getDate_of_end() {
        return date_of_end;
    }

    public boolean isInfiniteWarranty() {
        return isInfiniteWarranty;
    }

    public void setWarranty(Date date_of_end) {
        if(date_of_end == null)
            return;

        int days = Utils.convertDateToDays(date_of_end);
        if(days >= Utils.INFINITE) {
            warranty_chip.setText("Nieograniczona");
            isInfiniteWarranty = true;
        }
        else if(days == 1){
            warranty_chip.setText("1 dzień");
            isInfiniteWarranty = false;
        }
        else if(days == Utils.NONE) {
            resetText();
            isInfiniteWarranty = false;
        }
        else{
            warranty_chip.setText(days + " dni");
            isInfiniteWarranty = false;
        }

        warranty_chip.setCloseIconVisible(true);
        isWarrantySet = true;
    }
}

