package com.example.admin.appbarbottom.ReceiptCarerUtils;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.appbarbottom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MyExtentableFABs {

    public static final int EXTRA_REVEAL_CENTER_PADDING = 40;
//    private SimpleSearchView searchView;

    //Animations
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;
    Animation show_fab_inf;
    Animation hide_fab_inf;

    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    FloatingActionButton fab_inf;

    //Save the FAB's active status
    //false -> fab = close
    //true -> fab = open
    private boolean FAB_Status = false;

    private static final float LEFT_AND_RIGHT_ANIMATION_FAB_HIGHT_MARGIN = 1f;
    private static final float VERTICAL_ANIMATION_FAB_MARGIN = 1.25f;
    private static final float FAB_INF_MARGIN = 4f;

    AppCompatActivity a;
    public MyExtentableFABs(AppCompatActivity activity, FloatingActionButton fab){
        a = activity;
        this.fab = fab;
        createFAB();
    }


    private void createFAB() {

        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab3_hide);

        show_fab_inf = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab_inf_show);
        hide_fab_inf = AnimationUtils.loadAnimation(a.getApplication(), R.anim.fab_inf_hide);

        fab1 = a.findViewById(R.id.fab_single_receipt);
        fab2 = a.findViewById(R.id.fab_multy_receipts);
        fab3 = a.findViewById(R.id.fab_add_group);
        fab_inf = a.findViewById(R.id.fab_inf);

        show_fab_1.setDuration(200);
        show_fab_2.setDuration(200);
        show_fab_3.setDuration(200);
        show_fab_inf.setDuration(200);
    }

    public void tickleFloatingFABMenu() {

        if (!FAB_Status) {
            //Display FAB menu

            //FAB 1
            expandFAB(fab1, show_fab_1, VERTICAL_ANIMATION_FAB_MARGIN, LEFT_AND_RIGHT_ANIMATION_FAB_HIGHT_MARGIN );

            //FAB 2
            expandFAB(fab2, show_fab_2, -VERTICAL_ANIMATION_FAB_MARGIN, LEFT_AND_RIGHT_ANIMATION_FAB_HIGHT_MARGIN );

            //FAB 3
            expandFAB(fab3, show_fab_3, 0f,  2f);

            //FAB INF
            if(AppSettings.getInstance().isShowInfoGraphics())
                fab_inf.show();


            FAB_Status = true;
        } else {
            //FAB 1
            hideFAB(fab1, hide_fab_1, VERTICAL_ANIMATION_FAB_MARGIN, LEFT_AND_RIGHT_ANIMATION_FAB_HIGHT_MARGIN );

            //FAB 2
            hideFAB(fab2, hide_fab_2, -VERTICAL_ANIMATION_FAB_MARGIN, LEFT_AND_RIGHT_ANIMATION_FAB_HIGHT_MARGIN );

            //FAB 3
            hideFAB(fab3, hide_fab_3, 0f, 2f);

            //FAB INF
            if(AppSettings.getInstance().isShowInfoGraphics())
                fab_inf.hide();



            FAB_Status = false;
        }
    }

    public boolean isExpanded(){
        return FAB_Status;
    }

    private void expandFAB(FloatingActionButton fab, Animation anim_fab, float right_margin_scale, float bottom_margin_scale) {
        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.rightMargin += (int) (fab.getWidth() * right_margin_scale);
//        layoutParams.leftMargin += (int) (fab.getWidth() * right_margin_scale);
        layoutParams.bottomMargin += (int) (fab.getHeight() * bottom_margin_scale);
        fab.setLayoutParams(layoutParams);
        fab.startAnimation(anim_fab);
        fab.setClickable(true);
    }

    private void hideFAB(FloatingActionButton fab, Animation anim_fab, float right_margin_scale, float bottom_margin_scale){

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab.getWidth() * right_margin_scale);
        layoutParams.bottomMargin -= (int) (fab.getHeight() * bottom_margin_scale);
        fab.setLayoutParams(layoutParams);
        fab.startAnimation(anim_fab);
        fab.setClickable(false);

    }
}