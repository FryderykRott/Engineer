package com.example.admin.appbarbottom.fragments.receipt.adding;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.ReceiptCarerUtils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SinglePicturePreviewFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton accept_button;
    ImageButton reject_button;
    ImageView preview;

    public static Bitmap picture;


    private OnFragmentImagePreviewListener mListener;

    public SinglePicturePreviewFragment() {
    }

    public static SinglePicturePreviewFragment newInstance(Bitmap bitmap) {
        SinglePicturePreviewFragment fragment = new SinglePicturePreviewFragment();

        picture = bitmap;

        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_preview_single, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preview = view.findViewById(R.id.image_view_photo_preview);
        preview.setImageBitmap(Utils.RotateBitmap(picture, 90));

        accept_button =  view.findViewById(R.id.fab_accept_photo);
        reject_button =  view.findViewById(R.id.image_button_reject_photo);

        accept_button.setOnClickListener(this);
        reject_button.setOnClickListener(this);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentImagePreviewListener) {
            mListener = (OnFragmentImagePreviewListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentImagePreviewListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.fab_accept_photo):
                mListener.onFragmentImagePreviewListener(true);
                break;
            case(R.id.image_button_reject_photo):
                mListener.onFragmentImagePreviewListener(false);
                break;
        }
    }

    public interface OnFragmentImagePreviewListener {
        // TODO: Update argument type and name
        void onFragmentImagePreviewListener(Boolean answer);
    }

}