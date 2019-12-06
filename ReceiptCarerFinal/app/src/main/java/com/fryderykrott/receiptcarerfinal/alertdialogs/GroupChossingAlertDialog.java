package com.fryderykrott.receiptcarerfinal.alertdialogs;

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

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.adapters.MiniGroupAdapter;
import com.fryderykrott.receiptcarerfinal.Model.Group;

public class GroupChossingAlertDialog extends Dialog implements View.OnClickListener, MiniGroupAdapter.OnGroupChoosen {

    public Activity parent_activity;

    Group choosenGroup;

    public Button ok;
    public Button cancel;

    RecyclerView groups;
    MiniGroupAdapter itemAdapter;

    View add_new_group;

    private boolean isCheckedNeverShowAgainInfo = true;

    private int layoutID = R.layout.alert_dialog_group_choosing;

    //    TODO napisać
    private OnAlertDialogOnGroupChossing listener;

    //    public static final int
    public GroupChossingAlertDialog(Activity a, OnAlertDialogOnGroupChossing listener) {
        super(a);

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
        itemAdapter = new MiniGroupAdapter(parent_activity);

        itemAdapter.setOnNewGroupClickListener(this);

        groups = findViewById(R.id.group_recycle_view);
        groups.setLayoutManager(new LinearLayoutManager(parent_activity, LinearLayoutManager.VERTICAL, false));

        groups.setAdapter(itemAdapter);

//        add_new_group = findViewById(R.id.choosing_group_add_new_group);
//        add_new_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                TODO jak kliknie sie to go widać
//            }
//        });
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
    public void onGroupChoosen(Group group) {
        choosenGroup = group;
    }

    public interface OnAlertDialogOnGroupChossing{
        public void onGroupChossingCallback(Group group);
    }
}


