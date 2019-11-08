package com.example.admin.appbarbottom.alertdialog.info;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.admin.appbarbottom.model.GroupReceipt;
import com.example.admin.appbarbottom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

class GroupAddingAlertDialog extends MyDialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;

    public Button ok;
    public Button cancel;

    ImageButton folder_preview;

    FloatingActionButton fab_color_for_folder_1;
    FloatingActionButton fab_color_for_folder_2;
    FloatingActionButton fab_color_for_folder_3;
    FloatingActionButton fab_color_for_folder_4;

   EditText editTextGroupname;


    private int layoutID = R.layout.alert_dialog_group_adding;
    private OnAlertDialogGroupCreationCallBackListener l;

    int chosenColor;

    //    public static final int
    public GroupAddingAlertDialog(Activity a, OnAlertDialogGroupCreationCallBackListener l) {
        super(a, MyDialog.GROUP_ADDING_ALERT_DIALOG);
        this.l = l;
        parent_activity = a;
    }

    public void setParams(int color_chosen, String group_name){
        changeColorOfFolder(color_chosen);
        changeText(group_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(layoutID);
        ok = findViewById(R.id.button_ok);
        cancel = findViewById(R.id.button_cancel);

        folder_preview = findViewById(R.id.folder_icon_preview);
        fab_color_for_folder_1 = findViewById(R.id.fab_color_for_folder_1);
        fab_color_for_folder_2 = findViewById(R.id.fab_color_for_folder_2);
        fab_color_for_folder_3 = findViewById(R.id.fab_color_for_folder_3);
        fab_color_for_folder_4 = findViewById(R.id.fab_color_for_folder_4);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        fab_color_for_folder_1.setOnClickListener(this);
        fab_color_for_folder_2.setOnClickListener(this);
        fab_color_for_folder_3.setOnClickListener(this);
        fab_color_for_folder_4.setOnClickListener(this);

        editTextGroupname = findViewById(R.id.edit_text_folder_name);

        setParams(GroupReceipt.COLOR_CHOSEN_1, "New group");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                String name = editTextGroupname.getText().toString();
                if(name.isEmpty())
                    name = "GroupReceipt";

                GroupReceipt group = GroupReceipt.buildGroup(name, 0.0f, chosenColor);
                l.onAlertDialogGroupCreationCallBackListener(this, group);
                dismiss();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.fab_color_for_folder_1:
                changeColorOfFolder(GroupReceipt.COLOR_CHOSEN_1);
                break;
            case R.id.fab_color_for_folder_2:
                changeColorOfFolder(GroupReceipt.COLOR_CHOSEN_2);
                break;
            case R.id.fab_color_for_folder_3:
                changeColorOfFolder(GroupReceipt.COLOR_CHOSEN_3);
                break;
            case R.id.fab_color_for_folder_4:
                changeColorOfFolder(GroupReceipt.COLOR_CHOSEN_4);
                break;
            default:
                break;
        }

    }

    private void changeColorOfFolder(int color_chosen) {

        if(color_chosen == GroupReceipt.COLOR_CHOSEN_1)
            folder_preview.setImageResource(R.drawable.ic_folder_big);
        else if(color_chosen == GroupReceipt.COLOR_CHOSEN_2)
            folder_preview.setImageResource(R.drawable.ic_folder_big_2);
        else if(color_chosen == GroupReceipt.COLOR_CHOSEN_3)
            folder_preview.setImageResource(R.drawable.ic_folder_big_3);
        else if(color_chosen == GroupReceipt.COLOR_CHOSEN_4)
            folder_preview.setImageResource(R.drawable.ic_folder_big_4);

        chosenColor = color_chosen;
    }


    private void changeText(String group_name) {

        if(group_name.isEmpty())
            return;
        else
            editTextGroupname.setText(group_name);
    }

}
