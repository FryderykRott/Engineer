package com.fryderykrott.receiptcarerfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.fryderykrott.receiptcarerfinal.alertdialogs.AlertDialogFullScreenImageDisplayer;
import com.google.firebase.auth.FirebaseAuth;

public class ReceiptAddingActivity extends AppCompatActivity implements AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener {
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
    public void imagePreviewCallback(int info) {

    }
}
