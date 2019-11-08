package com.example.admin.appbarbottom.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.admin.appbarbottom.fragments.receipt.adding.AddingReceiptFragment;
import com.example.admin.appbarbottom.model.GroupReceipt;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"1", "2","3","4", "5","6"};
    private ArrayList<GroupReceipt> receipts;
    private ArrayList<Fragment> fragments;

    private final AppCompatActivity mContext;

    public SectionsPagerAdapter(AppCompatActivity context, FragmentManager fm, ArrayList<GroupReceipt> receipts) {
        super(fm);
        this.receipts =receipts;
        fragments = new ArrayList<>();
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = AddingReceiptFragment.newInstance(receipts.get(position), mContext, position);
        fragments.add(fragment);

        return fragment;
    }
    public Fragment getFragmentAt(int position){
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Receipt " + (position + 1) ;
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

}