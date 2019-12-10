package com.fryderykrott.receiptcarerfinal.MainView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.camerapreview.CameraPreviewFragment;
import com.fryderykrott.receiptcarerfinal.Services.Database;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener, CameraPreviewFragment.OnPhotoTakingListener {
    Receipt receiptToEdit;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private ConstraintLayout container;
    private View snackbar_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        snackbar_container = findViewById(R.id.snackbar_containter);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        bottomNavigationView = findViewById(R.id.nav_view);
        // Passing each menu uid as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_groups, R.id.navigation_receipts, R.id.navigation_search)
                .build();

        navController = Navigation.findNavController(this, R.id.receipt_adding_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.debug_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                NavOptions navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.fab_slide_out_to_right)
                        .setPopUpTo(R.id.mobile_navigation, true).build();

                navController.navigate(R.id.navigation_login, null, navOptions, null);
                Utils.user = null;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if(destination.getId() == R.id.navigation_login || destination.getId() == R.id.navigation_register ){
            setNavigationVisibility(false);
        }
        else if(destination.getId() == R.id.navigation_editing_receipt || destination.getId() == R.id.fragment_camera_preview_edit ){
            setNavigationVisibility(false);
        }
        else
            setNavigationVisibility(true);

//        if(destination.getId() == R.id.navigation_search)
//            getSupportActionBar().hide();
    }

    public void setNavigationVisibility(boolean isShown) {
        if(isShown){
            bottomNavigationView.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        }
        else{
            bottomNavigationView.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }
    }

    public void showSnackBar(int id){
        Snackbar snackbar = Snackbar.make(snackbar_container, id, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void showSnackBar(String tekst){
        Snackbar snackbar = Snackbar.make(container, tekst, Snackbar.LENGTH_LONG);

       FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                snackbar.getView().getLayoutParams();
        params.setMargins(1, 1, 1, 180);
        snackbar.getView().setLayoutParams(params);

        snackbar.show();
//        Toast.makeText(this, tekst, Toast.LENGTH_LONG).show();
    }

    public void setProgressView(boolean isVisible) {
        if(isVisible){
            findViewById(R.id.progressView).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.progressView).setVisibility(View.GONE);
        }
    }

    public NavController getNavController() {
        return navController;
    }


    public void setReceiptToEdit(Receipt receipt){
        receiptToEdit = receipt;
    }


    public Receipt getReceiptToEdit(){
        return receiptToEdit;
    }

    @Override
    public void onPhotoTakeingCallback(ArrayList<Bitmap> bitmaps) {

        if(bitmaps == null) {
            navController.popBackStack();
            return;
        }

        setProgressView(true);

        Database.getInstance().uploadBitmapsOfReceipt(receiptToEdit, bitmaps, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setProgressView(false);
                navController.popBackStack();
            }
        });

    }
}
