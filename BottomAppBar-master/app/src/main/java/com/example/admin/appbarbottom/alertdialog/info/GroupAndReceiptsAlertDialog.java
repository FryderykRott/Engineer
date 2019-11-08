package com.example.admin.appbarbottom.alertdialog.info;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.admin.appbarbottom.R;

public class GroupAndReceiptsAlertDialog extends MyDialog implements android.view.View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public Activity parent_activity;
    public Dialog d;
    public Button ok;
    public CheckBox checkBox;

    private boolean isCheckedNeverShowAgainInfo = true;

    private int layoutID = R.layout.alert_dialog_info_groups_and_receipt_adding;
    private OnAlertCkeckBoxCallBackListener listener;

    //    public static final int
    GroupAndReceiptsAlertDialog(Activity a, OnAlertCkeckBoxCallBackListener listener) {
        super(a, MyDialog.GROUP_AND_RECEITS_INFO_ALERT_DIALOG);

        this.listener = listener;
        parent_activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        setContentView(layoutID);
        ok = findViewById(R.id.button_ok);
        checkBox = findViewById(R.id.checkbox_info);

        ok.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                listener.onAlertCheckBoxCallBackListener(this, isCheckedNeverShowAgainInfo);
                break;
            default:
                break;
        }
        dismiss();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isCheckedNeverShowAgainInfo = isChecked;
    }
}

