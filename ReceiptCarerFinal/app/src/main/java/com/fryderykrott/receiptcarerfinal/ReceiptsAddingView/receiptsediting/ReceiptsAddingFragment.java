package com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.receiptsediting;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.ReceiptAddingActivity;
import com.fryderykrott.receiptcarerfinal.adapters.SectionsPagerAdapter;
import com.fryderykrott.receiptcarerfinal.Model.Group;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.receiptdetail.ReceiptDetailFragment;
import com.fryderykrott.receiptcarerfinal.Services.Database;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ReceiptsAddingFragment extends Fragment {

    public ReceiptsAddingFragment() {
        // Required empty public constructor
    }

    public ArrayList<Receipt> receipts;

    AppCompatActivity context;

    public static ReceiptsAddingFragment newInstance(ArrayList<Receipt> receipts, AppCompatActivity context) {
        ReceiptsAddingFragment fragment = new ReceiptsAddingFragment();
        fragment.context = context;
        fragment.receipts = receipts;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipts_adding, container, false);
    }

    SectionsPagerAdapter sectionsPagerAdapter;
    boolean wasAlreadyCreated = false;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        receipts = ((ReceiptAddingActivity) getActivity()).getReceiptsToEdit();
//        if(wasAlreadyCreated){
//            return;
//        }

        sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getChildFragmentManager(), receipts);

        ViewPager viewPager =  view.findViewById(R.id.receipt_view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = view.findViewById(R.id.receipts_tabs);
        if(receipts.size() > 1){
            tabs.setupWithViewPager(viewPager);
        }
        else
            tabs.setVisibility(View.GONE);

        Toolbar toolbar = view.findViewById(R.id.toolbar_adding_receipts);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (getActivity()).finish();
            }
        });


        ActionMenuItemView accept_button = toolbar.findViewById(R.id.accept);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Validate
                boolean pass = true;
                ((ReceiptAddingActivity)getActivity()).setProgressView(true);
                for(ReceiptDetailFragment fragment :sectionsPagerAdapter.getFragments())
                    if(!fragment.validateFragment())
                        pass = false;

               if(pass){
                   for(ReceiptDetailFragment fragment :sectionsPagerAdapter.getFragments())
                      fragment.prepareReceipt();

                   Group group;

                   for(Receipt receipt :receipts){
//                       group = Utils.findGroupById(receipt.getGroupID());
                       Utils.user.getReceipts().add(receipt);
                   }


                   Database.getInstance().uploadAndUpgradeReceipts(receipts, new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           getActivity().finish();
                       }
                   });

                   Log.i("das", "Das");
               }
               else
                   ((ReceiptAddingActivity) getActivity()).showSnackBar("Sprawdż wszystkie paragony!");
            }
        });


        ((ReceiptAddingActivity) getActivity()).setProgressView(false);
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
//        Navigation.findNavController(getView()).navigate(R.id.navigation_camera_preview);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

//    public void updateReceipt(int positionOfReceipt, Bitmap bitmap) {
//        receipts.get(positionOfReceipt).addPhoto(bitmap);
//        ((AddingReceiptFragment)sectionsPagerAdapter.getFragmentAt(positionOfReceipt)).notifyDataSetChanges();
//    }

}
