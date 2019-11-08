package com.example.admin.appbarbottom.ReceipCarerChips;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogOnGroupChossing;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.google.android.material.chip.Chip;

public class GroupChossingContainer implements View.OnClickListener, OnAlertDialogOnGroupChossing {

    private boolean isDateSet = false;

    private GroupReceipt group;

    private Chip group_chossing_chip;
    private Activity parent_activity;
    String basic_text;

    public GroupChossingContainer(final Activity parent_activity) {
        this.parent_activity = parent_activity;

        LayoutInflater inflater = parent_activity.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        group_chossing_chip = myLayout.findViewById(R.id.group_chip);

        if(group_chossing_chip.getParent() != null) {
            ((ViewGroup) group_chossing_chip.getParent()).removeView(group_chossing_chip); // <- fix
        }
        basic_text = group_chossing_chip.getText().toString();

        group_chossing_chip.setOnClickListener(this);
        group_chossing_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDateSet = false;
                group_chossing_chip.setCloseIconVisible(false);
                resetText();
            }
        });
        resetChip();
    }


    @Override
    public void onClick(View v) {
        Dialog da = AlertDialogInfoBuilder.buildGroupChossingAlertDialog(parent_activity, this);
        da.show();
    }

    private void resetText(){
        group_chossing_chip.setText(basic_text);
    }

    public GroupReceipt getGroup() {
        return group;
    }

    public Chip getGroupChip(){
        return group_chossing_chip;
    }

    public boolean isDateSet() {
        return isDateSet;
    }

    public void resetChip(){
        isDateSet = false;
        group = null;
        resetText();
    }


    @Override
    public void onGroupChossingCallback(GroupReceipt group) {
        if(group == null)
            return;
        this.group = group;

        group_chossing_chip.setText(group.getName());
//        TODO ustawianie ikony takieej jaka jest w grupie
        group_chossing_chip.setCloseIconVisible(true);
        isDateSet = true;
    }
}

