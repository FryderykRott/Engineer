package com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview;

import android.app.Activity;
import android.content.Context;
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

import com.fryderykrott.receiptcarerfinal.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.ReceiptAddingActivity;
import com.fryderykrott.receiptcarerfinal.camera.*;
import com.fryderykrott.receiptcarerfinal.alertdialogs.AlertDialogFullScreenImageDisplayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CameraPreviewFragment extends Fragment implements View.OnClickListener, AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener {

    public boolean only_single_photo_flag = false;
    public static final int VISIBLE = 10;
    public static final int INVISIBLE = 11;
    public static final int GONE = 12;

    public static final int ENABLED = 0;
    public static final int DISABLE= 1;


    public static final int FLASH_ON = R.drawable.ic_flash_on_white_24dp;
    public static final int FLASH_OFF = R.drawable.ic_flash_off_white_24dp;
    public static final int FLASH_AUTO = R.drawable.ic_flash_auto_white_24dp;

    public static int flash_state = FLASH_OFF;

    public static final int CAPTURE = R.drawable.ic_camera_capture;


    private int fragment_state = BASIC_CAPTURING;

    private static final int BASIC_CAPTURING = 100;
    private static final int SINGLE_PHOTO_PREVIEW = 101;
    private static final int MULTI_PHOTO_CAPTURING_STATE = 102;

    public static final String PICTURE_TAKEN = "PICTURE_TAKEN";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    private BottomNavigationView navigationView;

    private boolean cameraFront = false;
    public static Bitmap bitmap;
    public static ArrayList<Bitmap> bitmaps = new ArrayList<>();

    private OnFragmentPhotoTakingCallbackListener mListener;


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
//        czyli zrobić ze moze byc swaping miedzy trybami single i multy
//        ok zrobie to za pomoca navigation view

        ((ReceiptAddingActivity) getActivity()).getSupportActionBar().hide();
//        navigationView.setOnNavigationItemSelectedListener(this);
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

        menuOrGoBackButton = view.findViewById(R.id.button_menu_or_go_back_left_upper_corner);
        menuOrGoBackButton.setOnClickListener(this);

        closeImagePreview = view.findViewById(R.id.fab_close_image_preview);
        closeImagePreview.setOnClickListener(this);

        cardViewPreviewOfImages = view.findViewById(R.id.card_view_image);
//        cardViewPreviewOfImages.setOnClickListener(this);

        currentImageView = view.findViewById(R.id.image_preview);
        currentImageView.setOnClickListener(this);

        text_view_number_of_images = view.findViewById(R.id.text_view_number_of_images);
        text_view_number_of_images.setVisibility(View.INVISIBLE);

//        text_view_number_of_images.setTextColor(ContextCompat.getColor(a, R.color.colorWhite));

//        zacyznamy z BASIC
//        1. fashem wylaczonym
//        2. schowaną preview oraz schowanym buttonem acteptacji
//        3. guzik wskazuje powrot
//        4. Fab capture jest jako capture

//        Single photo case
//        1. mamy tylko kamere i guzk fab ustawiony na aktywny
//        2. kiedy klikniesz to poakzuje wi epreview i fab zmienais ie na akceptuj
//        3. a) akceptacja przenosi dalej
//        3. b) cofniecie powoduje powrot do podstawowych ustawien

//        Multi photo case
//        1. mamy kamere, pojawia sie preview okienko bez guzika wyrzucania aktualnie zrobionego zdjecia
//        2. po zrobineiu jednego zdjecia mozemy zaakceptować, jelsi to tylko jedno zdjecie ot wykonaj wariant jednego paraognu
//        3.
        fragment_state = BASIC_CAPTURING;
        commitStateChanges();
    }

    boolean takingPhoto = false;
    private void commitStateChanges() {
        switch (fragment_state){
            case BASIC_CAPTURING:
                setBasicState();
                break;
            case SINGLE_PHOTO_PREVIEW:
            case MULTI_PHOTO_CAPTURING_STATE:
                menuOrGoBackButton.setImageResource(R.drawable.ic_close);
                if(!takingPhoto){
                    mCamera.takePicture(null, null, mPicture);
                    takingPhoto = true;
                }

                break;
        }
    }

    private void setSinglePhotoPreviewState() {
        Log.i("state:", "CAPUTRE STATE SINGLE");
//        mCamera.takePicture(null, null, mPicture);
        captureButton.hide();
        acceptButton.show();
        setStateTo(flashButton, GONE);
        navigationView.setVisibility(View.GONE);
    }

    private int number_of_images = 0;

    private void setMultiPhotoState() {
        //        Multi photo case

        number_of_images++;
        text_view_number_of_images.setText(number_of_images + "");
        text_view_number_of_images.setVisibility(View.VISIBLE);
        captureButton.setImageAlpha(0);
//        mCamera.startPreview();
//        1. mamy kamere, pojawia sie preview okienko bez guzika wyrzucania aktualnie zrobionego zdjecia
        setStateTo(cardViewPreviewOfImages, VISIBLE);
//        2. po zrobineiu jednego zdjecia mozemy zaakceptować, jelsi to tylko jedno zdjecie ot wykonaj wariant jednego paraognu
        acceptButton.show();
        navigationView.setVisibility(View.GONE);

        currentImageView.setImageBitmap(bitmap);

        if(number_of_images >= 2){
            closeImagePreview.show();
        }
        else
            closeImagePreview.hide();


    }

    private void setBasicState() {
        //        zacyznamy z BASIC
        menuOrGoBackButton.setImageResource(R.drawable.ic_arrow_back);
        text_view_number_of_images.setVisibility(View.INVISIBLE);
        text_view_number_of_images.setText("1");
        captureButton.setImageAlpha(255);
        captureButton.show();
//        1. fashem wylaczonym
        setStateTo(flashButton, VISIBLE);

        setImageButtonIconTo(flashButton, FLASH_OFF);
//        2. schowaną preview oraz schowanym buttonem acteptacji
        hidePreview();
//        4. Fab capture jest jako capture
        setFABIconTo(captureButton, CAPTURE);

        acceptButton.hide();

        mCamera.startPreview();
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
                bitmaps.add(bitmap);


                if(fragment_state == MULTI_PHOTO_CAPTURING_STATE){
                    setMultiPhotoState();
                    mCamera.startPreview();
                }
                else
                    setSinglePhotoPreviewState();

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
        Log.i("state:", "CLICK");
        switch (v.getId()){
            case (R.id.fab_capture):
                fabCaptureClick();
                break;
            case (R.id.fab_send_next):
                if (mListener != null) {
                    mListener.onFragmentCallback(bitmaps);
                    bitmaps = new ArrayList<>();
                }
                break;
            case (R.id.fab_close_image_preview):
                Log.i("number", String.valueOf(number_of_images));
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
                if(flash_state == FLASH_OFF){
                    setImageButtonIconTo(flashButton, FLASH_ON);
                    flash_state = FLASH_ON;
                }
                else if(flash_state == FLASH_ON){
                    setImageButtonIconTo(flashButton, FLASH_AUTO);
                    flash_state = FLASH_AUTO;
                }
                else if(flash_state == FLASH_AUTO){
                    setImageButtonIconTo(flashButton, FLASH_OFF);
                    flash_state = FLASH_OFF;
                }
                break;
            case (R.id.image_preview):
                mCamera.stopPreview();
                AlertDialogFullScreenImageDisplayer ad = new AlertDialogFullScreenImageDisplayer(getActivity(), this, bitmaps, 0);
                ad.show();

                break;
        }
    }

    private void exitButtonClicked() {
        switch (fragment_state){
            case BASIC_CAPTURING:
                if (mListener != null) {
                    mListener.onFragmentCallback(null);
                }
                break;
            case SINGLE_PHOTO_PREVIEW:
            case MULTI_PHOTO_CAPTURING_STATE:
                bitmaps.clear();
                number_of_images = 0;

                fragment_state = BASIC_CAPTURING;
                break;
        }
        commitStateChanges();
    }

    private void fabCaptureClick() {
        switch (fragment_state){
            case BASIC_CAPTURING:
                switch (navigationView.getSelectedItemId()){

                }
                commitStateChanges();
                break;
            case SINGLE_PHOTO_PREVIEW:
//              TODO  nigdy w tym stanie nie zostnie klikniety poniewaz znika wtedy na zawsze elo papa
                break;
            case MULTI_PHOTO_CAPTURING_STATE:
                commitStateChanges();
                break;
        }
    }

    @Override
    public void imagePreviewCallback(int info) {
        mCamera.startPreview();
    }

    public void setOnlySiglePhotoFlag(boolean b) {
        if(b){
            fragment_state = SINGLE_PHOTO_PREVIEW;
        }
        only_single_photo_flag = b;
    }

    public interface OnFragmentPhotoTakingCallbackListener {
        void onFragmentCallback(ArrayList<Bitmap> bitmaps);
    }
}
