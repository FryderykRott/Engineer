package com.example.admin.appbarbottom.alertdialog.info;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.ReceiptCarerUtils.Utils;
import com.example.admin.appbarbottom.model.GroupReceipt;

import adapter.ItemAdapter;
import adapter.OnNewGroupClickListener;

public class GroupChossingAlertDialog extends MyDialog implements View.OnClickListener, OnNewGroupClickListener{

    public Activity parent_activity;
    public Dialog d;

    GroupReceipt choosenGroup;

    public Button ok;
    public Button cancel;

    RecyclerView groups;
    ItemAdapter itemAdapter;

    private boolean isCheckedNeverShowAgainInfo = true;

    private int layoutID = R.layout.alert_dialog_group_choosing;

//    TODO napisać
    private OnAlertDialogOnGroupChossing listener;

    //    public static final int
    GroupChossingAlertDialog(Activity a, OnAlertDialogOnGroupChossing listener) {
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
        cancel = findViewById(R.id.button_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        itemAdapter = new ItemAdapter(parent_activity, Utils.global_Group_Set, ItemAdapter.SMALL_GROUPS);

        itemAdapter.setOnNewGroupClickListener(this);

        groups = findViewById(R.id.group_recycle_view);
        groups.setLayoutManager(new LinearLayoutManager(parent_activity, LinearLayoutManager.VERTICAL, false));

        groups.setAdapter(itemAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                listener.onGroupChossingCallback(choosenGroup);
                break;
            default:
                break;
        }
        dismiss();
    }

//    @Override
//    public void onClick(int position, GroupReceipt group, ImageView view) {
//
//    }

    @Override
    public void onNewGroupClick(GroupReceipt group) {
        choosenGroup = group;
    }
}

