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

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.adapters.ImageAdapter;
import com.fryderykrott.receiptcarerfinal.adapters.ImageURLAdapter;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class AlertDialogFullScreenImageDisplayer extends Dialog implements android.view.View.OnClickListener {

    public Activity parent_activity;
    Receipt receipt;
    public ImageButton ok;
    public ImageButton delete;
    private int position;
    ArrayList<Bitmap> bitmaps;
    PagerAdapter adapter;

    ViewPager viewpager;
    ViewGroup viewGroup;
    CircleIndicator indicator;

    private int layoutID = R.layout.alert_dialog_full_screen_pictures;
    OnImagePreviewCallbackListener listener;

    boolean isDeletable = true;
    //    public static final int
    public AlertDialogFullScreenImageDisplayer(Activity a, OnImagePreviewCallbackListener listener, Receipt receipt, int position) {
        super(a);
        parent_activity = a;
        this.listener = listener;
        this.receipt = receipt;
        this.bitmaps = receipt.somethingDifferentImagesAsBitmap();
        this.position = position;
    }

    public AlertDialogFullScreenImageDisplayer(Activity a, OnImagePreviewCallbackListener listener,Receipt receipt, int position, boolean isDeletable) {
        super(a);
        parent_activity = a;
        this.listener = listener;
        this.receipt = receipt;
        this.bitmaps = receipt.somethingDifferentImagesAsBitmap();
        this.position = position;
        this.isDeletable = isDeletable;
    }

    public AlertDialogFullScreenImageDisplayer(Activity a, OnImagePreviewCallbackListener listener,ArrayList<Bitmap> bitmaps, int position, boolean isDeletable) {
        super(a);
        parent_activity = a;
        this.listener = listener;
        this.bitmaps = bitmaps;
        this.position = position;
        this.isDeletable = isDeletable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewGroup = findViewById(android.R.id.content);
        View layout = LayoutInflater.from(parent_activity).inflate(layoutID, viewGroup, false);

        setContentView(layout);

        getWindow().setLayout(1000, 1700);

        ok = findViewById(R.id.buttonOK);
        ok.setOnClickListener(this);


        if(receipt != null && !receipt.getImages_as_base64().isEmpty())
            adapter = new ImageURLAdapter(parent_activity, receipt);
        else
            adapter = new ImageAdapter(parent_activity, bitmaps);


        viewpager = findViewById(R.id.view_pager_receipts);
        viewpager.setOffscreenPageLimit(100);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(position);

        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        delete = findViewById(R.id.buttonDetele);

        if(!isDeletable){
            delete.setVisibility(View.GONE);
        }
        else if(bitmaps.size() == 1){
            delete.setVisibility(View.GONE);
        }
        delete.setOnClickListener(this);
    }

    public static final int NO_RESPONSE = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.buttonOK):
                listener.imagePreviewCallback(125534);
                dismiss();
                break;
            case (R.id.buttonDetele):
                int current_position = viewpager.getCurrentItem();
                int new_position = 0;

                if(current_position == 0 && bitmaps.size() > 1)
                    new_position = 1;
                else if(current_position > 0)
                    new_position = current_position - 1;

                viewpager.setCurrentItem(new_position);

                if(!receipt.getImages_as_base64().isEmpty())
                    receipt.getImages_as_base64().remove(current_position);
                else
                    bitmaps.remove(current_position);
//                adapter.destroyItem(viewGroup, current_position, null);
                adapter.notifyDataSetChanged();

                if(!receipt.getImages_as_base64().isEmpty())
                    adapter = new ImageURLAdapter(parent_activity, receipt);
                else
                    adapter = new ImageAdapter(parent_activity, bitmaps);

                viewpager.setAdapter(adapter);

                adapter.registerDataSetObserver(indicator.getDataSetObserver());
                adapter.notifyDataSetChanged();

                if(!isDeletable){
                    delete.setVisibility(View.GONE);
                }
                else if(bitmaps.size() == 1){
                    delete.setVisibility(View.GONE);
                }

                listener.imagePreviewCallback(NO_RESPONSE);
                break;
        }

    }

    public interface OnImagePreviewCallbackListener {
        public void imagePreviewCallback(int info);
    }

}

