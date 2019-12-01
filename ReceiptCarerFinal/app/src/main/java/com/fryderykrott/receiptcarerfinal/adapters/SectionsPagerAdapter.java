package com.fryderykrott.receiptcarerfinal.adapters;


import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview.receiptdetail.ReceiptDetailFragment;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Receipt> receipts;

    private ArrayList<ReceiptDetailFragment> fragments;

    private final Activity mContext;

    public SectionsPagerAdapter(Activity context, FragmentManager fm, ArrayList<Receipt> receipts) {
        super(fm);
        this.receipts = receipts;
        fragments = new ArrayList<>();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        ReceiptDetailFragment fragment = ReceiptDetailFragment.newInstance(receipts.get(position), mContext, position);
        fragments.add(fragment);

        return fragment;
    }

    public Fragment getFragmentAt(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Paragon " + (position + 1);
    }

    @Override
    public int getCount() {
        return receipts.size();
    }


    public ArrayList<ReceiptDetailFragment> getFragments() {
        return fragments;
    }

}


