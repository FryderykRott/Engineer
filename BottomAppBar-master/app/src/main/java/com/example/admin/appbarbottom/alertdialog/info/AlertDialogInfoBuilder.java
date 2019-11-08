package com.example.admin.appbarbottom.alertdialog.info;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class AlertDialogInfoBuilder {
    private static final AlertDialogInfoBuilder ourInstance = new AlertDialogInfoBuilder();

    public static AlertDialogInfoBuilder getInstance() {
        return ourInstance;
    }

    private AlertDialogInfoBuilder() {
    }

    public static Dialog buildGroupAndReceiptAlertDialog(Activity a, OnAlertCkeckBoxCallBackListener l){
        return new GroupAndReceiptsAlertDialog(a, l);
    }

    public static Dialog buildGroupAddingAlertDialog(Activity a, OnAlertDialogGroupCreationCallBackListener l) {
        GroupAddingAlertDialog gad = new GroupAddingAlertDialog(a, l);
        return gad;
    }

    public static Dialog buildGroupAddingAlertDialog(Activity a, OnAlertDialogGroupCreationCallBackListener l, int folder_color, String group_name) {
        GroupAddingAlertDialog gad = new GroupAddingAlertDialog(a, l);
        gad.setParams(folder_color, group_name);
        return gad;
    }

    public static Dialog buildRangeCalendarChooser(Activity a, OnAlertDialogRangeDateListener l) {
        CalendarRangeSelectionAlertDialog gad = new CalendarRangeSelectionAlertDialog(a, l);
        return gad;
    }

    public static Dialog buildRangePriceChooser(Activity a, OnAlertDialogRangePriceListener l) {
        RangePriceSelectionAlertDialog gad = new RangePriceSelectionAlertDialog(a, l);
        return gad;
    }

    public static Dialog buildCalendarChooser(Activity a, OnAlertDialogDateListener l) {
        CalendarSingleSelectionAlertDialog gad = new CalendarSingleSelectionAlertDialog(a, l);
        return gad;
    }

    public static Dialog builImagesViewFullScreenAlertDialog(Activity a, AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener listener, ArrayList<Bitmap> bitmaps, int position) {
        AlertDialogFullScreenImageDisplayer alertDialog = new AlertDialogFullScreenImageDisplayer(a, listener, bitmaps, position);
        return alertDialog;
    }


    public static Dialog buildPriceChooser(Activity parent_activity, OnAlertDialogPriceListener listener) {
        PriceSelectionAlertDialog alertDialog = new PriceSelectionAlertDialog(parent_activity, listener);
        return alertDialog;
    }

    public static Dialog buildWarrantyAlertDialog(Activity parent_activity, OnAlertDialogWarrantyListener listener) {
        WarrantySelectionAlertDialog alertDialog = new WarrantySelectionAlertDialog(parent_activity, listener);
        return alertDialog;
    }

    public static Dialog buildGroupChossingAlertDialog(Activity parent_activity, OnAlertDialogOnGroupChossing listener) {
        GroupChossingAlertDialog alertDialog = new GroupChossingAlertDialog(parent_activity, listener);
        return alertDialog;
    }

}
