package com.fryderykrott.receiptcarerfinal;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        bottomNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_groups, R.id.navigation_receipts, R.id.navigation_search)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if(destination.getId() == R.id.navigation_login || destination.getId() == R.id.navigation_register){
            setNavigationVisibility(false);
        }
        else
            setNavigationVisibility(true);
    }

    private void setNavigationVisibility(boolean isShown) {
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
//        Snackbar snackbar = Snackbar.make(bottomNavigationView, id, Snackbar.LENGTH_SHORT);
//        ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(String tekst){
//        Snackbar snackbar = Snackbar.make(bottomNavigationView, id, Snackbar.LENGTH_SHORT);
//        ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
        Toast.makeText(this, tekst, Toast.LENGTH_LONG).show();
    }

    public void setProgressView(boolean isVisible) {
        if(isVisible){
            findViewById(R.id.progressView).setVisibility(View.VISIBLE);
        }
        else
            findViewById(R.id.progressView).setVisibility(View.GONE);
    }
}
