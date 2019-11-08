package com.example.admin.appbarbottom.alertdialog.info;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class MyDialog extends Dialog {

    public static final int GROUP_AND_RECEITS_INFO_ALERT_DIALOG = 100;
    public static final int GROUP_ADDING_ALERT_DIALOG = 101;
    public static final int CALENDAR_RANGE_SELCTION_ALERT_DIALOG = 102;
    public static final int CALENDAR_SINGLE_SELECTION_ALERT_DIALOG = 103;
    public static final int PRICE_RANGE_SELECTION_ALERT_DIALOG = 104;
    public static final int PRICE_SELECTION_ALERT_DIALOG = 105;

    private int id;

    public MyDialog(@NonNull Context context, int ID) {
        super(context);
        id = ID;
    }

    public int getId(){
        return id;
    }
}
