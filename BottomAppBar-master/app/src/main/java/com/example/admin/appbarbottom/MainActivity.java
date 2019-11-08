package com.example.admin.appbarbottom;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.admin.appbarbottom.ReceipCarerChips.ChipGroupFiltrer;
import com.example.admin.appbarbottom.ReceipCarerChips.RangeCalendarChipContainer;
import com.example.admin.appbarbottom.ReceipCarerChips.RangePriceChipContainer;
import com.example.admin.appbarbottom.ReceiptCarerUtils.AppSettings;
import com.example.admin.appbarbottom.ReceiptCarerUtils.MyExtentableFABs;
import com.example.admin.appbarbottom.ReceiptCarerUtils.Utils;
import com.example.admin.appbarbottom.alertdialog.info.AlertDialogInfoBuilder;
import com.example.admin.appbarbottom.alertdialog.info.MyDialog;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertCkeckBoxCallBackListener;
import com.example.admin.appbarbottom.alertdialog.info.OnAlertDialogGroupCreationCallBackListener;
import com.example.admin.appbarbottom.fragments.receipt.adding.ReceiptAddingActivity;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Date;

import adapter.ItemAdapter;
import adapter.OnNewGroupClickListener;
import interfaces.CustomItemClick;

//import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnAlertCkeckBoxCallBackListener,
        OnAlertDialogGroupCreationCallBackListener,
        CustomItemClick, OnNewGroupClickListener {

    BottomAppBar bottomAppBar;
    public FloatingActionButton floatingActionButton;
    Toolbar toolbar;

    SearchView searchView;

    FloatingActionButton fab_mini_menu_SINGLE_RECEIPT;
    FloatingActionButton fab_mini_menu_ADD_GROUP;
    FloatingActionButton fab_mini_menu_MULTY_RECEIPT;
    FloatingActionButton fab_inf;

    MyExtentableFABs extentableFABs;

    RangeCalendarChipContainer data_range_choose_chip;
    RangePriceChipContainer price_range_choose__chip;

    ChipGroup chipGroup;

    GroupFragment groupFragment;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemAdapter = new ItemAdapter(this, Utils.generateGroupsAndReceipts(this), ItemAdapter.GROUPS);
        itemAdapter.setOnNewGroupClickListener(this);

        groupFragment = new GroupFragment(itemAdapter);


        toolbar = findViewById(R.id.toolbar);
        addFragment(groupFragment, false, GroupFragment.class.getName(), false, false, null, false, false, null);
        bottomAppBar = findViewById(R.id.bottom_appbar);
        bottomAppBar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet(BottomSheetFragment.class.getName());
            }
        });

        createFAB();
        createChips();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().hide();

        initSingleScanningFAB();

//        if(AppSettings.getInstance().isFirstAttemptToRun())
//        {
//            Intent intent = new Intent(MainActivity.this, ReceiptAdding.class);
//            Bundle activity_type = new Bundle();
//
//            activity_type.putInt(ReceiptAdding.ADDING_TYPE_KEY, ReceiptAdding.SINGLE_RECEIPT);
//
//            intent.putExtras(activity_type);
//
//            startActivityForResult(intent, EnumReceipCarerRequsts.SINGLE_RECEIPT_ADDING);
//        }
    }

    public void checkIfFabIsExpandedAndInCaseItIsCloseIt(){
        if(extentableFABs.isExpanded())
            extentableFABs.tickleFloatingFABMenu();
    }
    private void createFAB() {
        extentableFABs = new MyExtentableFABs(this, floatingActionButton);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                extentableFABs.tickleFloatingFABMenu();
                Intent intent = new Intent(MainActivity.this, ReceiptAddingActivity.class);
                startActivity(intent);
            }
        });
//
//        //Floating Action Buttons
//        fab_mini_menu_SINGLE_RECEIPT = findViewById(R.id.fab_single_receipt);
//        fab_mini_menu_SINGLE_RECEIPT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ReceiptAdding.class);
//                Bundle activity_type = new Bundle();
//
//                activity_type.putInt(ReceiptAdding.ADDING_TYPE_KEY, ReceiptAdding.SINGLE_RECEIPT);
//
//                intent.putExtras(activity_type);
//
//                startActivityForResult(intent, EnumReceipCarerRequsts.SINGLE_RECEIPT_ADDING);
//
//            }
//        });
//
//        fab_mini_menu_ADD_GROUP = findViewById(R.id.fab_add_group);
//        fab_mini_menu_ADD_GROUP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog ad = AlertDialogInfoBuilder.buildGroupAddingAlertDialog(MainActivity.this, MainActivity.this);
//                ad.show();
//            }
//        });
//
//        fab_mini_menu_MULTY_RECEIPT = findViewById(R.id.fab_multy_receipts);
//        fab_mini_menu_MULTY_RECEIPT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ReceiptAdding.class);
//                startActivityForResult(intent, EnumReceipCarerRequsts.MULTY_RECEIPT_ADDING);
//            }
//        });

        fab_inf = findViewById(R.id.fab_inf);
        fab_inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog ad = AlertDialogInfoBuilder.buildGroupAndReceiptAlertDialog(MainActivity.this, MainActivity.this);
                ad.show();
            }
        });

    }

    private void createChips() {
//        data_range_choose_chip = new RangeCalendarChipContainer(this);
//        price_range_choose__chip = new RangePriceChipContainer(this);

        chipGroup = findViewById(R.id.chipGroup);
//        chipGroup.addView(data_range_choose_chip.getCalendarChip());
//        chipGroup.addView(price_range_choose__chip.getWarrantyChip());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.my_menu, menu);

        MenuItem searchItem = bottomAppBar.getMenu().findItem(R.id.app_bar_search);
        searchItem.getIcon().setTint(getColor(R.color.colorWhite));

        final SearchView searchView = (SearchView) searchItem.getActionView();

        ChipGroupFiltrer chipGroupFiltrer = new ChipGroupFiltrer(this, chipGroup, searchView, itemAdapter);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Log.i("fsfsasdgsagasdgS:", "dsadas");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void addFragment(Fragment fragment, boolean addToBackStack, String tag, boolean isAnimate, boolean isArguments, Bundle bundle, boolean isAdd, boolean isShared, View view) {

//        fragment.setEnterTransition(new Fade());
//        fragment.setExitTransition(new Fade());

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (isAnimate) {
//            ft.setCustomAnimations(R.anim.slide_in_right,
//                    R.anim.slide_out_left, R.anim.slide_in_left,
//                    R.anim.slide_out_right);
            ft.setCustomAnimations(R.animator.slide_up, R.animator.slide_down);
        }
        if (isShared) {
            ft.addSharedElement(view, ViewCompat.getTransitionName(view));
        }
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }

        if (isArguments) {
            fragment.setArguments(bundle);
        }

        if (!isAdd) {
            ft.replace(R.id.frame_container, fragment, tag);

        } else {
            ft.add(R.id.frame_container, fragment, tag);
        }
        ft.commitAllowingStateLoss();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() >= 0) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
            if (fragment instanceof GroupFragment) {
                setToolbar("Animals", false);
                toggleFabButton();
            }

        }


    }

    public void toggleFabButton() {
        if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            floatingActionButton.setImageResource(R.drawable.ic_reply_black_24dp);

//            animowanie FAB
            final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_right);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            getSupportActionBar().show();
            myAnim.setInterpolator(interpolator);

            floatingActionButton.startAnimation(myAnim);
        } else {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
            getSupportActionBar().hide();
            floatingActionButton.setImageResource(R.drawable.ic_add_black_24dp);
        }

        checkIfFabIsExpandedAndInCaseItIsCloseIt();
    }

    public void setToolbar(String title, boolean isBack) {
        if (isBack) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        toolbar.setTitle(title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
                if (fragment instanceof GroupFragment) {

                } else {
                    onBackPressed();
                }
            }
        });


    }

    public void openBottomSheet(String tag) {
        BottomSheetDialogFragment bottomSheet = null;
        if (tag.equals(BottomSheetFragment.class.getName())) {
            bottomSheet = new BottomSheetFragment();
            bottomSheet.setAllowEnterTransitionOverlap(true);
        }
        if (bottomSheet != null) {
            bottomSheet.show(getSupportFragmentManager(), tag);
        }
    }

    public void calendarResponse(Date date) {
        Log.i("Ad test:","SUCCES");
    }

    @Override
    public void onClick(int position, GroupReceipt group, ImageView view) {
//        TODO Klikniecie w grupe tutaj sie dzieje
    }

    @Override
    public void onNewGroupClick(GroupReceipt group) {
//        addFragment(groupFragment, false, ReceiptFragment.class.getName(), false, false, null, false, false, null);
    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

    private void showDiag() {

        final View dialogView = View.inflate(this, R.layout.dialog, null);
//
        final Dialog dialog = new Dialog(this, R.style.MyAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.closeDialogImg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealShow(dialogView, false, dialog);
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShow(dialogView, true, null);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {

                    revealShow(dialogView, false, dialog);
                    return true;
                }

                return false;
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
    }

    private void revealShow(View dialogView, boolean b, final Dialog dialog) {

        final View view = dialogView.findViewById(R.id.dialog);

        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (floatingActionButton.getX() + (floatingActionButton.getWidth() / 2));
        int cy = (int) (floatingActionButton.getY()) + floatingActionButton.getHeight() / 2;


        if (b) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(700);
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });
            anim.setDuration(700);
            anim.start();
        }

    }

    private void initSingleScanningFAB(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            fab_mini_menu_SINGLE_RECEIPT.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                fab_mini_menu_SINGLE_RECEIPT.setEnabled(true);
            }
        }
    }

    File file;
    Uri imageUri;
    public void takePicture(View view) throws Exception {
//        Image Uri Handling

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        file = createTemporaryFile("heh", ".jpg");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, IMAGE_CAPTURE_REQUEST);
    }


    private static final int IMAGE_CAPTURE_REQUEST = 1000;
    Bitmap bitmap;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_REQUEST) {
            if (resultCode == RESULT_OK) {
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                Bitmap eh = decodeBase64(encodeTobase64(bitmap));
//                String lol = convertJPGToBase64(bitmap);

//                ImageView imageView = findViewById(R.id.image_view);
//                imageView.setImageBitmap(bitmap);
                    file = new File(getPath(imageUri));

//                  ReceiptScanner rc = new ReceiptScanner(file);
//                  rc.execute();


//                OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(Main2Activity.this, mAPiKey, isOverlayRequired, getPath(imageUri), mLanguage, mIOCRCallBack);
//                oCRAsyncTask.execute();
            }
        }
//        else if (searchView.onActivityResult(requestCode, resultCode, data)) {
//            return; //TODO wyszukiwanie rzeczy
//        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public FloatingActionButton getFloatingInfoActionButton(){
        return fab_inf;
    }


    @Override
    public void onAlertCheckBoxCallBackListener(MyDialog dialog, boolean isChecked) {
        if(isChecked){
            AppSettings.getInstance().setShowInfoGraphics(false);
            getFloatingInfoActionButton().hide();
        }

    }


    @Override
    public void onAlertDialogGroupCreationCallBackListener(MyDialog dialog, GroupReceipt new_group) {
        Utils.global_Group_Set.add(new_group);
        groupFragment.notifyDataChangeSet();
        checkIfFabIsExpandedAndInCaseItIsCloseIt();
    }


}
