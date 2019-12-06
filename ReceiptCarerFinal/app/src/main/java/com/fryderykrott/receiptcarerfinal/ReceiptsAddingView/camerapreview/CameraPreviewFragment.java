package com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.camerapreview;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.ReceiptAddingActivity;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.alertdialogs.AlertDialogFullScreenImageDisplayer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CameraPreviewFragment extends Fragment implements View.OnClickListener, AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener {

    public static final int VISIBLE = 10;
    public static final int INVISIBLE = 11;
    public static final int GONE = 12;

    public static final int ENABLED = 0;
    public static final int DISABLE= 1;

    public static final int FLASH_ON = R.drawable.ic_flash_on_white_24dp;
    public static final int FLASH_OFF = R.drawable.ic_flash_off_white_24dp;
    public static final int FLASH_AUTO = R.drawable.ic_flash_auto_white_24dp;

    public static int flash_state = FLASH_ON;

    public static final int CAPTURE = R.drawable.ic_camera_capture;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPicture;
    private FloatingActionButton captureButton;
    private FloatingActionButton acceptButton;

    private Context myContext;
    private FrameLayout cameraPreview;

    private ImageButton flashButton;
    private ImageButton menuOrGoBackButton;
    private FloatingActionButton closeImagePreview;
    private CardView cardViewPreviewOfImages;
    private ImageView currentImageView;

    TextView text_view_number_of_images;

    public static Bitmap bitmap;
    public static ArrayList<Bitmap> bitmaps = new ArrayList<>();

    private int number_of_images = 0;
    boolean takingPhoto = false;

    private OnPhotoTakingListener mListener;

    public CameraPreviewFragment(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener = (OnPhotoTakingListener) getActivity();


        ((ReceiptAddingActivity) getActivity()).getSupportActionBar().hide();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = getContext();

        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        cameraPreview = view.findViewById(R.id.frame_camera_view);
        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);

        //set camera to continually auto-focus
        Camera.Parameters params = mCamera.getParameters();
//*EDIT*//params.setFocusMode("continuous-picture");
//It is better to use defined constraints as opposed to String, thanks to AbdelHady
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(params);

        captureButton = view.findViewById(R.id.fab_capture);
        captureButton.setOnClickListener(this);

        mCamera.startPreview();
        acceptButton = view.findViewById(R.id.fab_send_next);
        acceptButton.setOnClickListener(this);

        flashButton = view.findViewById(R.id.button_flash);
        flashButton.setOnClickListener(this);

       if(!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
           flashButton.setVisibility(View.INVISIBLE);

        menuOrGoBackButton = view.findViewById(R.id.button_menu_or_go_back_left_upper_corner);
        menuOrGoBackButton.setOnClickListener(this);

        closeImagePreview = view.findViewById(R.id.fab_close_image_preview);
        closeImagePreview.setOnClickListener(this);
        closeImagePreview.hide();

        cardViewPreviewOfImages = view.findViewById(R.id.card_view_image);
//        cardViewPreviewOfImages.setOnClickListener(this);

        currentImageView = view.findViewById(R.id.image_preview);
        currentImageView.setOnClickListener(this);

        text_view_number_of_images = view.findViewById(R.id.text_view_number_of_images);
        text_view_number_of_images.setVisibility(View.INVISIBLE);

        setBasicState();

        setStateTo(cardViewPreviewOfImages, INVISIBLE);
        notifyFlashStateChanges();
    }

    private void commitStateChanges() {
        setBasicState();

        if(!takingPhoto){
            mCamera.takePicture(null, null, mPicture);
            takingPhoto = true;
        }

    }

    private void setBasicState() {
//        menuOrGoBackButton.setImageResource(R.drawable.ic_arrow_back);
//        text_view_number_of_images.setVisibility(View.INVISIBLE);
//        text_view_number_of_images.setText("1");
//        captureButton.setImageAlpha(255);
//        captureButton.show();
//
//        setStateTo(flashButton, VISIBLE);
//        setFABIconTo(captureButton, CAPTURE);

        acceptButton.hide();

        mCamera.startPreview();
    }


    private void setMultiPhotoState() {

        number_of_images++;
        text_view_number_of_images.setText(number_of_images + "");
        text_view_number_of_images.setVisibility(View.VISIBLE);
        captureButton.setImageAlpha(0);

        setStateTo(cardViewPreviewOfImages, VISIBLE);

        acceptButton.show();
        currentImageView.setImageBitmap(bitmap);

        if(number_of_images >= 2){
            closeImagePreview.show();
        }
        else
            closeImagePreview.hide();

    }


    private void showPreview() {
        setStateTo(cardViewPreviewOfImages, VISIBLE);
        setStateTo(closeImagePreview, VISIBLE);
    }

    private void hidePreview() {
        setStateTo(cardViewPreviewOfImages, INVISIBLE);
        setStateTo(closeImagePreview, GONE);
    }

    private void setFABIconTo(FloatingActionButton fabButton, int id) {
        fabButton.setBackgroundResource(id);
    }

    private void setStateTo(View view, int state) {

        switch (state){
            case(VISIBLE):
                view.setVisibility(View.VISIBLE);
                break;
            case(INVISIBLE):
                view.setVisibility(View.INVISIBLE);
                break;
            case(ENABLED):
                view.setEnabled(true);
                break;
            case(DISABLE):
                view.setEnabled(false);
                break;
            case(GONE):
                view.setVisibility(View.GONE);
                break;
        }

    }
    private void setImageButtonIconTo(ImageButton ib, int id) {

        ib.setImageResource(id);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        bitmaps.clear();
        number_of_images = 0;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCamera == null) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            mPicture = getPictureCallback();
            mPreview.refreshCamera(mCamera);
            Log.d("nu", "null");
        } else {
            mPicture = getPictureCallback();
            Log.d("nu", "no null");
        }
    }

    private void releaseCamera () {
        // stop and release camera
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.PictureCallback getPictureCallback () {
        Camera.PictureCallback picture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                bitmap = Bitmap.createScaledBitmap(
                        bitmap, bitmap.getWidth() / 2 , bitmap.getHeight() /2, false);

                bitmap = Utils.RotateBitmap(bitmap, 90);
                bitmaps.add(bitmap);

                setMultiPhotoState();
                mCamera.startPreview();

                takingPhoto = false;
            }
        };
        return picture;
    }


    @Override
    public void onClick(View v) {
        if(!takingPhoto){
            onClickBody(v);
        }
    }

    private void onClickBody(View v) {
        switch (v.getId()){
            case (R.id.fab_capture):
                commitStateChanges();
                break;
            case (R.id.fab_send_next):
                ArrayList<Bitmap> copy_bitmaps = new ArrayList<>(bitmaps);
                mListener.onPhotoTakeingCallback(copy_bitmaps);
                bitmaps.clear();
                number_of_images = 0;
                break;
            case (R.id.fab_close_image_preview):
                number_of_images--;
                text_view_number_of_images.setText(number_of_images + "");
//                gdy sa dwa zdjeica to w liscie jest 0-zdjecie i 1-zdjecie
//                number ma wartosc 2 wiec ma wartosc 1

                bitmaps.remove(number_of_images);

                currentImageView.setImageBitmap(bitmaps.get(number_of_images - 1));

                if(number_of_images >= 2){
                    closeImagePreview.show();
                }
                else
                    closeImagePreview.hide();

                break;
            case (R.id.button_menu_or_go_back_left_upper_corner):
                exitButtonClicked();
                break;
            case (R.id.button_flash):
                notifyFlashStateChanges();
                break;
            case (R.id.image_preview):
                mCamera.stopPreview();
                AlertDialogFullScreenImageDisplayer ad = new AlertDialogFullScreenImageDisplayer(getActivity(), this, bitmaps, 0, false);
                ad.show();

                break;
        }
    }

    private void notifyFlashStateChanges() {
        if(flash_state == FLASH_OFF){
            setImageButtonIconTo(flashButton, FLASH_ON);
            flash_state = FLASH_ON;
            mPreview.setFlashLight(CameraPreview.ON);
        }
        else if(flash_state == FLASH_ON){
            setImageButtonIconTo(flashButton, FLASH_AUTO);
            flash_state = FLASH_AUTO;
            mPreview.setFlashLight(CameraPreview.AUTO);
        }
        else if(flash_state == FLASH_AUTO){
            setImageButtonIconTo(flashButton, FLASH_OFF);
            flash_state = FLASH_OFF;
            mPreview.setFlashLight(CameraPreview.OFF);
        }
    }

    private void exitButtonClicked() {
        if (mListener != null) {
            mListener.onPhotoTakeingCallback(null);
            bitmaps.clear();
            number_of_images = 0;
        }
    }

    @Override
    public void imagePreviewCallback(int info) {
//        mCamera.startPreview();
        commitStateChanges();
    }

    public interface OnPhotoTakingListener {
        void onPhotoTakeingCallback(ArrayList<Bitmap> bitmaps);
    }

}
