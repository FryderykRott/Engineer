package com.fryderykrott.receiptcarerfinal.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.AlertDialogFullScreenImageDisplayer;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.receiptdetail.ReceiptDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageURLAdapter extends PagerAdapter implements AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener {
    private Activity context;
    ArrayList<String> images;
    Receipt receipt;
    private boolean isClickable;

    ImageAdapter.OnNewPhotoCallbackListener listener;
    Fragment fragment;

    public ImageURLAdapter(Activity context, Receipt receipt) {
        this.receipt = receipt;
        this.context = context;
        this.images = receipt.getImages_as_base64();
        this.isClickable = false;
    }

    public ImageURLAdapter(Activity context, Receipt receipt, ImageAdapter.OnNewPhotoCallbackListener listener, Fragment fragment) {
        this.receipt = receipt;
        this.context = context;
        this.images = receipt.getImages_as_base64();
        this.isClickable = true;
        this.listener = listener;
        this.fragment = fragment;
    }

    TextView information;
    ImageView add_photo_image;

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_pager_item, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        final ImageURLAdapter adapter = this;
        if(isClickable){
            if(position == images.size() ) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onNewPhotoCallback(position);
                    }
                });
            }
            else {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        FragmentActivity a, OnImagePreviewCallbackListener listener,Receipt receipt, int position, boolean isDeletable) {
                        AlertDialogFullScreenImageDisplayer ad = new AlertDialogFullScreenImageDisplayer(context, adapter, receipt, position, false);
                        ad.show();

                    }
                });
            }

        }

        information = view.findViewById(R.id.Information);
        add_photo_image = view.findViewById(R.id.add_photo_image);

        if(position == images.size() ){
            setVisibilityOfInformationPanel(View.VISIBLE);
        }
        else{
            setVisibilityOfInformationPanel(View.INVISIBLE);

            Picasso.get()
                    .load(receipt.getImages_as_base64().get(position))
                    .into(imageView);

        }

//        imageView.setImageDrawable(context.getDrawable(getImageAt(position)));
        container.addView(view);
        return view;
    }

    private void setVisibilityOfInformationPanel(int visibility){
        information.setVisibility(visibility);
        add_photo_image.setVisibility(visibility);
    }

    @Override
    public int getCount() {
        if(isClickable)
            return images.size() + 1;

        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container.getChildAt(position));
    }

    @Override
    public void imagePreviewCallback(int info) {
        notifyDataSetChanged();
        if(fragment != null)
            ((ReceiptDetailFragment)fragment).resetImageAdapter();
    }

    public interface OnNewPhotoCallbackListener {
        public void onNewPhotoCallback(int position_off_receipt);
    }
}
