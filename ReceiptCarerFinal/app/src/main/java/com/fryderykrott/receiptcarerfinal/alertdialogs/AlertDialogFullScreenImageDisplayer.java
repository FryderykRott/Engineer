package com.fryderykrott.receiptcarerfinal.alertdialogs;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import androidx.viewpager.widget.ViewPager;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.adapters.ImageAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class AlertDialogFullScreenImageDisplayer extends Dialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    public ImageButton ok;
    private int position;
    ArrayList<Bitmap> bitmaps;

    private int layoutID = R.layout.alert_dialog_full_screen_pictures;
    OnImagePreviewCallbackListener listener;

    //    public static final int
    public AlertDialogFullScreenImageDisplayer(Activity a, OnImagePreviewCallbackListener listener, ArrayList<Bitmap> bitmaps, int position) {
        super(a);
        parent_activity = a;
        this.listener = listener;

        this.bitmaps = bitmaps;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View layout = LayoutInflater.from(parent_activity).inflate(layoutID, viewGroup, false);

        setContentView(layout);

        getWindow().setLayout(1000, 1700);

        ok = findViewById(R.id.buttonOK);
        ok.setOnClickListener(this);

        ImageAdapter adapter = new ImageAdapter(parent_activity, bitmaps);

        ViewPager viewpager = findViewById(R.id.view_pager_receipts);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(position);

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    public static final int NO_RESPONSE = 0;

    @Override
    public void onClick(View v) {
        listener.imagePreviewCallback(NO_RESPONSE);
        dismiss();
    }

    public interface OnImagePreviewCallbackListener {
        public void imagePreviewCallback(int info);
    }
}

