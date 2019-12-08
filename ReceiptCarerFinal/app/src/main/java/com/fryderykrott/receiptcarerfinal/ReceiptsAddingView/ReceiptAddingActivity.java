package com.fryderykrott.receiptcarerfinal.ReceiptsAddingView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.alertdialogs.AlertDialogFullScreenImageDisplayer;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.camerapreview.CameraPreviewFragment;
import com.fryderykrott.receiptcarerfinal.Services.ReceiptScaner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.ArrayList;

public class ReceiptAddingActivity extends AppCompatActivity implements CameraPreviewFragment.OnPhotoTakingListener, OnFailureListener, OnSuccessListener<FirebaseVisionText>, AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener {

    private static final int NO_RECEIPT_EDITING = -1;
    private NavController navController;
    private ArrayList<Receipt> receiptsToEdit;

    private ConstraintLayout container;
    private int currentReceiptPhotoTakingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_adding);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.receipt_adding_nav_host_fragment);

        container = findViewById(R.id.container);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_groups, R.id.navigation_receipts, R.id.navigation_search)
//                .build();
//        navController = navHostFragment.getNavController();

        navController = Navigation.findNavController(this, R.id.receipt_adding_nav_host_fragment);
//
//        navController.navigate(R.id.navigation_camera_preview);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        getSupportActionBar().hide();
//        setupToolBar();
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
    public void onPhotoTakeingCallback(final ArrayList<Bitmap> bitmaps) {
        if(bitmaps == null)
        {

            if(currentReceiptPhotoTakingPosition != NO_RECEIPT_EDITING){
                navController.popBackStack();
                return;
            }
            finish();
            return;
        }

        if(currentReceiptPhotoTakingPosition != NO_RECEIPT_EDITING){
            ArrayList<Bitmap> currentBitmaplist =(ArrayList< Bitmap>) receiptsToEdit.get(currentReceiptPhotoTakingPosition).somethingDifferentImagesAsBitmap();
            currentBitmaplist.addAll(bitmaps);

            showSnackBar("Udało się dodać zdjęcia na pozycje: " + currentBitmaplist);
            navController.popBackStack();
            currentReceiptPhotoTakingPosition = NO_RECEIPT_EDITING;
            return;
        }

        setProgressView(true);

//        teraz następuje skanowanie
        ReceiptScaner scan = new ReceiptScaner(this, bitmaps, this, this);
        scan.proccesImages(new ReceiptScaner.OnCompleteScanningListener() {
            @Override
            public void onCompleteScanningListener(ArrayList<Receipt> results) {
                for(int i = 0; i<bitmaps.size(); i++){
                    results.get(i).addReceiptBitmap(bitmaps.get(i));
                }

                receiptsToEdit = results;
//                getSupportActionBar().show();
                navController.navigate(R.id.navigation_adding_receipts);
            }
        });
    }


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

    public ArrayList<Receipt> getReceiptsToEdit() {
        return receiptsToEdit;
    }

    public NavController getNavController() {
        return navController;
    }

    public void showSnackBar(String tekst){
        Snackbar snackbar = Snackbar.make(container, tekst, Snackbar.LENGTH_LONG);

//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
//                snackbar.getView().getLayoutParams();
//        params.setMargins(1, 1, 1, 180);
//        snackbar.getView().setLayoutParams(params);

        snackbar.show();
//        Toast.makeText(this, tekst, Toast.LENGTH_LONG).show();
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    public int getCurrentReceiptPhotoTakingPosition() {
        return currentReceiptPhotoTakingPosition;
    }

    public void setCurrentReceiptPhotoTakingPosition(int currentReceiptPhotoTakingPosition) {
        this.currentReceiptPhotoTakingPosition = currentReceiptPhotoTakingPosition;
    }

}
