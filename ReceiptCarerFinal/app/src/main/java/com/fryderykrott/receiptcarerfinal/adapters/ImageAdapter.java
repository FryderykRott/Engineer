package com.fryderykrott.receiptcarerfinal.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.fryderykrott.receiptcarerfinal.MainView.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.AlertDialogFullScreenImageDisplayer;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.receiptdetail.ReceiptDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter implements AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener {
    private int receipt_position;
    private Activity context;
    ArrayList<Bitmap> images;
    Receipt receipt;
    private boolean isClickable;

    OnNewPhotoCallbackListener listener;
    ReceiptDetailFragment fragment;

//    public ImageAdapter(Activity context, Receipt receipt) {
//        this.context = context;
//        this.images = receipt.somethingDifferentImagesAsBitmap();
//        this.isClickable = false;
//    }

    public ImageAdapter(Activity context, Receipt receipt, int receipt_position, OnNewPhotoCallbackListener listener, ReceiptDetailFragment fragment) {
        this.receipt = receipt;
        this.context = context;
        this.images = receipt.somethingDifferentImagesAsBitmap();
        this.isClickable = true;
        this.receipt_position = receipt_position;
        this.listener = listener;
        this.fragment = fragment;
//        resizeImages();
    }

//    public ImageAdapter(Activity context, Receipt receipt, int receipt_position, OnNewPhotoCallbackListener listener, Fragment fragment) {
//        this.receipt = receipt;
//        this.context = context;
//        this.images = receipt.somethingDifferentImagesAsBitmap();
//        this.isClickable = true;
//        this.receipt_position = receipt_position;
//        this.listener = listener;
//        this.fragment = fragment;
////        resizeImages();
//    }
//
//    public ImageAdapter(MainActivity context, Receipt receipt, OnNewPhotoCallbackListener listener, Fragment fragment) {
//        this.receipt = receipt;
//        this.context = context;
//        this.images = receipt.somethingDifferentImagesAsBitmap();
//        this.receipt_position = 0;
//        this.isClickable = true;
//        this.listener = listener;
//        this.fragment = fragment;
//    }

    public ImageAdapter(Activity context, ArrayList<Bitmap> bitmaps) {
        this.context = context;
        this.images = bitmaps;
        this.receipt_position = 0;
    }



    ImageView add_photo_image;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_pager_item, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        final ImageAdapter adapter = this;
        if(isClickable){
            if(position == images.size() ) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                     Możliwość dodania kolejnego zdjęcia lub paru
                        listener.onNewPhotoCallback(receipt_position);
                    }
                });
            }
            else {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialogFullScreenImageDisplayer ad = new AlertDialogFullScreenImageDisplayer(context, adapter, receipt, position);
                        ad.show();

                    }
                });
            }

        }


        add_photo_image = view.findViewById(R.id.add_photo_image);

        if(position == images.size() ){
            setVisibilityOfInformationPanel(View.VISIBLE);
        }
        else{
            setVisibilityOfInformationPanel(View.INVISIBLE);
            if(receipt != null && !receipt.getImages_as_base64().isEmpty()){
                Picasso.get()
                        .load(receipt.getImages_as_base64().get(position))
                        .centerCrop()
                        .into(imageView);
            }
            else
                imageView.setImageBitmap(getImageAt(position));
        }

//        imageView.setImageDrawable(context.getDrawable(getImageAt(position)));
        container.addView(view);
        return view;
    }

    private void setVisibilityOfInformationPanel(int visibility){
        add_photo_image.setVisibility(visibility);
    }

    @Override
    public int getCount() {

        if (isClickable) {
            if (receipt != null && !receipt.getImages_as_base64().isEmpty())
                return receipt.getImages_as_base64().size() + 1;
            return images.size() + 1;
        } else {
            if (receipt != null && !receipt.getImages_as_base64().isEmpty())
                return receipt.getImages_as_base64().size();
            return images.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    private Bitmap getImageAt(int position) {
        return images.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container.getChildAt(position));
    }

    @Override
    public void imagePreviewCallback(int info) {
        notifyDataSetChanged();
        if(fragment != null)
            ((ReceiptDetailFragment)fragment).resetImageAdapter(0);
    }

    public interface OnNewPhotoCallbackListener {
        public void onNewPhotoCallback(int position_off_receipt);
    }
}
