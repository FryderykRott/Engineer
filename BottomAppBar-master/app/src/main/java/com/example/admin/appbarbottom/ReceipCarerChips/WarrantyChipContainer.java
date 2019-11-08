package com.example.admin.appbarbottom.ReceipCarerChips;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.ReceiptCarerUtils.Utils;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogWarrantyListener;
import com.google.android.material.chip.Chip;

import java.util.Date;

//warranty_chip
public class WarrantyChipContainer implements View.OnClickListener, OnAlertDialogWarrantyListener {

    private boolean isDateSet = false;

    private Date date_of_end;

    private Chip warranty_chip;
    private Activity parent_activity;

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
                isDateSet = false;
                warranty_chip.setCloseIconVisible(false);
                resetText();
            }
        });
        resetChip();
    }


    @Override
    public void onClick(View v) {
        Dialog da = AlertDialogInfoBuilder.buildWarrantyAlertDialog(parent_activity, this);
        da.show();
    }

    private void resetText(){
        warranty_chip.setText("No warranty");
    }


    public Chip getWarrantyChip(){
        return warranty_chip;
    }


    public boolean isDateSet() {
        return isDateSet;
    }

    public void resetChip(){
        isDateSet = false;

        resetText();
    }

//    @Override
//    public void onAlertDialogPriceListener(MyDialog dialog, float price) {
//
//    }

    @Override
    public void onAlertDialogWarrantyCallback(Date date_of_end) {
        this.date_of_end = date_of_end;
        int days = Utils.convertDateToDays(date_of_end);
        if(days == Utils.INFINITE) {
            warranty_chip.setText("Infinite warranty");
        }
        else if(days == 1){
            warranty_chip.setText("1 day left");
        }
        else if(days == Utils.NONE) {
            resetText();
        }
        else{
            warranty_chip.setText(days + " days left");
        }

        warranty_chip.setCloseIconVisible(true);
        isDateSet = true;
    }
}
