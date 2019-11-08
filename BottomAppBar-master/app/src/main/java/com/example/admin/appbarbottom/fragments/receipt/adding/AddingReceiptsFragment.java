package com.example.admin.appbarbottom.fragments.receipt.adding;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.example.admin.appbarbottom.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AddingReceiptsFragment extends Fragment{


    public ArrayList<GroupReceipt> receipts;

    AppCompatActivity context;

    private OnReceiptsAddingCallbackListener mListener;

    public static AddingReceiptsFragment newInstance(ArrayList<GroupReceipt> receipts, AppCompatActivity context) {
        AddingReceiptsFragment fragment = new AddingReceiptsFragment();
        fragment.context = context;
        fragment.receipts = receipts;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mListener = (OnReceiptsAddingCallbackListener) context;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipts_adding_view_pager, container, false);
    }

    SectionsPagerAdapter sectionsPagerAdapter;
    boolean wasAlreadyCreated = false;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(wasAlreadyCreated){
            return;
        }

        sectionsPagerAdapter = new SectionsPagerAdapter(context, getChildFragmentManager(), receipts);

        ViewPager viewPager =  context.findViewById(R.id.receipt_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = view.findViewById(R.id.receipts_tabs);
        if(receipts.size() > 1){
            tabs.setupWithViewPager(viewPager);
        }
        else
            tabs.setVisibility(View.GONE);

        ActionMenuItemView accept_button = view.findViewById(R.id.accept_button);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData())
                    mListener.onReceiptsAddingCallback(null);
            }
        });

        Toolbar toolbar = view.findViewById(R.id.toolbar_adding_receipts);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO co za burdel, zrób coś z tym
//                wysiwetlanie informaacji czy aby na pewno?
//                ((ReceiptAddingActivity)getActivity()).onlySinglePhotoFlag = false;
//                ((ReceiptAddingActivity)getActivity()).onFragmentCallback(null);
                ((ReceiptAddingActivity)getActivity()).finish();
            }
        });
    }

    private boolean validateData() {
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof CameraPictureTakingFragment.OnFragmentPhotoTakingCallbackListener) {
//            mListener = (CameraPictureTakingFragment.OnFragmentPhotoTakingCallbackListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentCallbackListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public ArrayList<GroupReceipt> getReceipts() {
        return receipts;
    }


    public void updateReceipt(int positionOfReceipt, Bitmap bitmap) {
        receipts.get(positionOfReceipt).addPhoto(bitmap);
        ((AddingReceiptFragment)sectionsPagerAdapter.getFragmentAt(positionOfReceipt)).notifyDataSetChanges();
    }

    public interface OnReceiptsAddingCallbackListener {
        public void onReceiptsAddingCallback(ArrayList<GroupReceipt> receipts);
    }
}
