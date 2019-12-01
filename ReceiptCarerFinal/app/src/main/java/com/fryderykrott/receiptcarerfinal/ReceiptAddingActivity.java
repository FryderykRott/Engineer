package com.fryderykrott.receiptcarerfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fryderykrott.receiptcarerfinal.alertdialogs.AlertDialogFullScreenImageDisplayer;
import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview.camerapreview.CameraPreviewFragment;
import com.fryderykrott.receiptcarerfinal.services.ReceiptScaner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.ArrayList;

public class ReceiptAddingActivity extends AppCompatActivity implements CameraPreviewFragment.OnPhotoTakingListener, OnFailureListener, OnSuccessListener<FirebaseVisionText>, AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_adding);

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_groups, R.id.navigation_receipts, R.id.navigation_search)
//                .build();

        navController = Navigation.findNavController(this, R.id.receipt_adding_nav_host_fragment);

//        navController.navigate(R.id.navigation_camera_preview);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupToolBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.receipt_adding_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case(R.id.accept):
//                TODO zrobić stuff akceptowania
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void setupToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up

        return false;
    }


    @Override
    public void onFragmentCallback(ArrayList<Bitmap> bitmaps) {
        if(bitmaps == null)
        {
            finish();
            return;
        }

        setProgressView(true);

//        teraz następuje skanowanie
        ReceiptScaner scan = new ReceiptScaner(this, bitmaps, this, this);
        scan.proccesImages(new ReceiptScaner.OnCompleteScanningListener() {
            @Override
            public void onCompleteScanningListener(ArrayList<Receipt> results) {
                setProgressView(false);

            }
        });
    }

//    private void rezultat skanowanie(){
//
////        gdyby sie powidolo lub niepowiodlo chowam tutaj kod
//
//        ArrayList<GroupReceipt> receipts;
//
//        if(onlySinglePhotoFlag){
//            if(bitmaps != null){
//                savedReceipts.get(positionOfReceipt).addPhoto(bitmaps.get(0));
//                receipts = savedReceipts;
//                receipts_adding_fragment = AddingReceiptsFragment.newInstance(receipts, this);
//            }
////            ((AddingReceiptsFragment)receipts_adding_fragment).updateReceipt(positionOfReceipt, bitmaps.get(0));
//        }
//        else {
//            //        jako ze nie mam skanowania to stworze puste ale z obrazkami
//
////            W raize gdyby sie okazalo ze nic nie dostali
//            if(bitmaps == null){
//                finish();
//                return;
//            }
//
//            receipts = new ArrayList<>();
//            photos = bitmaps;
//            ArrayList<Bitmap> photos_of_receip;
//
//            photos_of_receip = new ArrayList<>();
//            photos_of_receip.add(bitmaps.get(0));
//
//            GroupReceipt receipt_1 = GroupReceipt.buildReceipt("Zakupy nuda",
//                    2.2f,
//                    12312L,
//                    31L,
//                    null, photos_of_receip);
//
//            receipt_1.addTag(new RCTag("Media Ekspert"));
//            receipt_1.addTag(new RCTag("Pierogi"));
//            receipt_1.addTag(new RCTag("Masło"));
//            receipt_1.addTag(new RCTag("Ksiażka"));
//            receipt_1.addTag(new RCTag("Tanio"));
//
//            receipts.add(receipt_1);
//
//            for(int i = 1; i < bitmaps.size(); i++){
////          TODO w tym miejscu następuje skanowanie i tworzenie paragonów
//
//                photos_of_receip = new ArrayList<>();
//                photos_of_receip.add(bitmaps.get(i));
//
//                receipt_1 = GroupReceipt.buildReceipt("",
//                        0f,
//                        0L,
//                        0L,
//                        null, photos_of_receip);
//
//                receipts.add(receipt_1);
//            }
//
//            receipts_adding_fragment = AddingReceiptsFragment.newInstance(receipts, this);
//
//        }
//
//
//
//        addFragmentOnTop(receipts_adding_fragment);
//        progressBar.setVisibility(View.INVISIBLE);
////        getSupportActionBar().show();
////        fragmentTransaction.commitAllowingStateLoss();
//    }

    public void setProgressView(boolean isVisible) {
        if(isVisible){
            findViewById(R.id.progressView).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.progressView).setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }

    @Override
    public void onSuccess(FirebaseVisionText firebaseVisionText) {

    }

    @Override
    public void imagePreviewCallback(int info) {

    }
}
