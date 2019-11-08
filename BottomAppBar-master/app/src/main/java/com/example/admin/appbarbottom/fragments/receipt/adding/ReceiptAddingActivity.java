package com.example.admin.appbarbottom.fragments.receipt.adding;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.ReceiptScanner;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogFullScreenImageDisplayer;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.example.admin.appbarbottom.model.RCTag;

import java.util.ArrayList;

import adapter.OnNewPhotoCallbackListener;

public class ReceiptAddingActivity extends AppCompatActivity implements CameraPictureTakingFragment.OnFragmentPhotoTakingCallbackListener,
        AlertDialogFullScreenImageDisplayer.OnImagePreviewCallbackListener,
        OnNewPhotoCallbackListener, AddingReceiptsFragment.OnReceiptsAddingCallbackListener {

    private static final String BACK_STACK_ROOT_TAG = "root";

//    Toolbar toolbar;
//    TabLayout tabs;

    Fragment camera_single_photo_fragment = CameraPictureTakingFragment.newInstance(this);
    Fragment receipts_adding_fragment;

    ArrayList<Bitmap> photos = new ArrayList<>();

    ArrayList<GroupReceipt> savedReceipts;
    int positionOfReceipt;
    boolean onlySinglePhotoFlag = false;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_adding);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.INVISIBLE);

//        toolbar = findViewById(R.id.toolbar_adding_receipts);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().hide();

//        tabs = findViewById(R.id.tabs);
//        tabs.setVisibility(View.GONE);
//
        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frame, camera_single_photo_fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commitAllowingStateLoss();
//

        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Add the new tab fragment
        fragmentManager.beginTransaction()
                .replace(R.id.frame, camera_single_photo_fragment )
                .addToBackStack(null)
                .commit();
//        goToFragment(camera_single_photo_fragment);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.receipt_adding_menu, menu);
//
//        return true;
//    }

    public void addFragmentOnTop(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, fragment)
                .addToBackStack(null)
                .commit();
    }

//    Fragment currentFragment;
//    private void goToFragment(Fragment f){
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//        if(currentFragment != null)
//            fragmentTransaction.remove(currentFragment);
//
//        currentFragment = f;
//
////        fragmentTransaction.add(f, "f");
//        fragmentTransaction.replace(R.id.frame, f);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commitAllowingStateLoss();
//    }

    @Override
    public void onFragmentCallback(ArrayList<Bitmap> bitmaps) {

//        removeAllFragments(getSupportFragmentManager());
//        getSupportFragmentManager().popBackStackImmediate();
        progressBar.setVisibility(View.VISIBLE);
        removeAllFragments(getSupportFragmentManager());


        ReceiptScanner scan = new ReceiptScanner(this, bitmaps);
        scan.execute();

        ArrayList<GroupReceipt> receipts;

        if(onlySinglePhotoFlag){
            if(bitmaps != null){
                savedReceipts.get(positionOfReceipt).addPhoto(bitmaps.get(0));
                receipts = savedReceipts;
                receipts_adding_fragment = AddingReceiptsFragment.newInstance(receipts, this);
            }
//            ((AddingReceiptsFragment)receipts_adding_fragment).updateReceipt(positionOfReceipt, bitmaps.get(0));
        }
        else {
            //        jako ze nie mam skanowania to stworze puste ale z obrazkami

//            W raize gdyby sie okazalo ze nic nie dostali
            if(bitmaps == null){
                finish();
                return;
            }

            receipts = new ArrayList<>();
            photos = bitmaps;
            ArrayList<Bitmap> photos_of_receip;

            photos_of_receip = new ArrayList<>();
            photos_of_receip.add(bitmaps.get(0));

            GroupReceipt receipt_1 = GroupReceipt.buildReceipt("Zakupy nuda",
                    2.2f,
                    12312L,
                    31L,
                    null, photos_of_receip);

            receipt_1.addTag(new RCTag("Media Ekspert"));
            receipt_1.addTag(new RCTag("Pierogi"));
            receipt_1.addTag(new RCTag("Masło"));
            receipt_1.addTag(new RCTag("Ksiażka"));
            receipt_1.addTag(new RCTag("Tanio"));

            receipts.add(receipt_1);

            for(int i = 1; i < bitmaps.size(); i++){
//          TODO w tym miejscu następuje skanowanie i tworzenie paragonów

                photos_of_receip = new ArrayList<>();
                photos_of_receip.add(bitmaps.get(i));

                receipt_1 = GroupReceipt.buildReceipt("",
                        0f,
                        0L,
                        0L,
                        null, photos_of_receip);

                receipts.add(receipt_1);
            }

            receipts_adding_fragment = AddingReceiptsFragment.newInstance(receipts, this);

        }



        addFragmentOnTop(receipts_adding_fragment);
        progressBar.setVisibility(View.INVISIBLE);
//        getSupportActionBar().show();
//        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void imagePreviewCallback(int info) {
//        TODO nothing here poniewaz tak i tyle. Ten listener zostal stowrzony tylko po to by byc uzytym gdzie indziej lol
    }

    @Override
    public void onNewPhotoCallback(int position_off_receipt) {
//        dostaje pozycje paragonu w arrayliscie to musze teraz:
//        1. zapisz stan
        savedReceipts = ((AddingReceiptsFragment)receipts_adding_fragment).getReceipts();
        positionOfReceipt = position_off_receipt;
//        2. zamknij wsio
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.remove(receipts_adding_fragment);
//        3. i otworz robienie zdjec w trybie tylko pojedyncze zdjeice
        onlySinglePhotoFlag = true;
        camera_single_photo_fragment = CameraPictureTakingFragment.newInstance(this);

        ((CameraPictureTakingFragment)camera_single_photo_fragment).setOnlySiglePhotoFlag(onlySinglePhotoFlag);
//        getSupportActionBar().hide();
        addFragmentOnTop(camera_single_photo_fragment);

//        4. dodaj flage mówiąca ze nastepnym razem jak wroci
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        removeAllFragments(getSupportFragmentManager());
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }
    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    public void onReceiptsAddingCallback(ArrayList<GroupReceipt> receipts) {
//        TODO dodąć paragony w odpowiednie miejsce
        finish();
    }
}