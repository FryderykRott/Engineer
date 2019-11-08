package com.example.admin.appbarbottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.appbarbottom.model.GroupReceipt;

import adapter.ItemAdapter;
import interfaces.CustomItemClick;

class ReceiptFragment extends Fragment implements CustomItemClick {
    MainActivity mainActivity;
    public static final String TAG = GroupFragment.class.getSimpleName();

    public ReceiptFragment(ItemAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    ItemAdapter itemAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_groups_and_receipts, container, false);
        mainActivity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(itemAdapter);


        recyclerView.addOnScrollListener(new ReceiptFragment.HideShowScrollListener() {
            @Override
            public void onHide() {

                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    public void hideViews() {
        // TODO (-mToolbar)  plus means  2 view above ho jaye or not visible to user
        mainActivity.bottomAppBar.animate().translationY(-mainActivity.bottomAppBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        // TODO uncomment this Hide Footer in android when Scrolling
        // TODO (+mToolbar)  plus means  2 view forward ho jaye or not visible to user
        mainActivity.bottomAppBar.animate().translationY(+mainActivity.bottomAppBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public void showViews() {
        mainActivity.bottomAppBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

        // TODO uncomment this Hide Footer in android when Scrolling
        mainActivity.bottomAppBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));

    }

    @Override
    public void onClick(int position, GroupReceipt group, ImageView view) {
        //TODO tutaj jest fragment ktory sie pojawia po kliknieciu w grupe na ekraniu glownym
//        Fragment fragment = DetailProfileFragment.newInstance(group, ViewCompat.getTransitionName(view));
//        mainActivity.addFragment(fragment, true, fragment.getClass().getName(), false, false, null, false, true, view);
//        mainActivity.setToolbar("Animal Detail", true);
//        mainActivity.toggleFabButton();
        showViews();
    }

    public void notifyDataChangeSet() {
//        Log.i("dsa", "dsadas");
        itemAdapter.notifyDataSetChanged();
    }


    public abstract class HideShowScrollListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }

            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        }

        public abstract void onHide();

        public abstract void onShow();

    }


}
