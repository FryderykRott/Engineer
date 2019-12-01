package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.adapters.MiniGroupAdapter;
import com.fryderykrott.receiptcarerfinal.alertdialogs.GroupChossingAlertDialog;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.google.android.material.chip.Chip;

public class GroupChossingContainer implements View.OnClickListener, GroupChossingAlertDialog.OnAlertDialogOnGroupChossing {

    private boolean isGroupSet = false;

    private Group group;

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
                isGroupSet = false;
                group_chossing_chip.setCloseIconVisible(false);
                resetText();
            }
        });
        resetChip();
    }


    @Override
    public void onClick(View v) {
        GroupChossingAlertDialog da = new GroupChossingAlertDialog(parent_activity, this);
        da.show();
    }

    private void resetText(){
        group_chossing_chip.setText(basic_text);
        Drawable icon = group_chossing_chip.getChipIcon();
        icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_basic));
    }

    public Group getGroup() {
        return group;
    }

    public Chip getGroupChip(){
        return group_chossing_chip;
    }

    public boolean isGroupSet() {
        return isGroupSet;
    }

    public void resetChip(){
        isGroupSet = false;
        group = null;
        resetText();
    }


    @Override
    public void onGroupChossingCallback(Group group) {
        if(group == null)
            return;
        this.group = group;

        group_chossing_chip.setText(group.getName());
//        TODO ustawianie ikony takieej jaka jest w grupie
        setColorOfGroup(group);
        group_chossing_chip.setCloseIconVisible(true);
        isGroupSet = true;
    }

    private void setColorOfGroup(Group group) {
        Drawable icon = group_chossing_chip.getChipIcon();

        switch (group.getColor()){
            case(1):
                icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_1));
                break;
            case (2):
                icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_2));
                break;
            case (3):
                icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_3));
                break;
            case (4):
                icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_4));
                break;
            case (5):
                icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_5));
                break;
            case (6):
                icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_6));
                break;
            default:
                icon.setTint(parent_activity.getResources().getColor(R.color.folder_icon_color_basic));
                break;
        }

    }
}


