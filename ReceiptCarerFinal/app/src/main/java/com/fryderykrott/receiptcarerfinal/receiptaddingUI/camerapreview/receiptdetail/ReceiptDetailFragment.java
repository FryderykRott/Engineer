package com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview.receiptdetail;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.adapters.ImageAdapter;
import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview.camerapreview.CameraPreviewFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import me.relex.circleindicator.CircleIndicator;

public class ReceiptDetailFragment extends Fragment {

    public ReceiptDetailFragment() {
        // Required empty public constructor
    }

    Receipt receipt;

//    public ArrayList<Bitmap> photos;

    AutoCompleteTextView autoCompleteTextView;

    TextInputEditText receiptNameTextInput;
    TextInputLayout receiptNameTextInputLayout;

    ImageAdapter adapter;
    ChipGroup tags;
    ImageButton acceptIcon;

    Activity context;
    int position;
    private CameraPreviewFragment.OnPhotoTakingListener mListener;

    public static ReceiptDetailFragment newInstance(Receipt receipt, Activity context, int position) {
        ReceiptDetailFragment fragment = new ReceiptDetailFragment();
        fragment.context = context;

        fragment.receipt = receipt;
        fragment.position = position;

        fragment.adapter = new ImageAdapter(context, receipt.getImages_as_bitmap(), position, new ImageAdapter.OnNewPhotoCallbackListener() {
            @Override
            public void onNewPhotoCallback(int position_off_receipt) {

            }
        });
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt_detail, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewpager = view.findViewById(R.id.view_pager_receipts);
        viewpager.setAdapter(adapter);

        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        acceptIcon = view.findViewById(R.id.button);
        acceptIcon.setVisibility(View.INVISIBLE);
        acceptIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChipToGroup(autoCompleteTextView.getText().toString(), tags);
                autoCompleteTextView.setText("");
                acceptIcon.setVisibility(View.INVISIBLE);
            }
        });

        autoCompleteTextView = view.findViewById(R.id.edit_text_tag_true);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("ds: ", String.valueOf(count));

                if(s.length() == 0){
                    acceptIcon.setVisibility(View.INVISIBLE);
                }
                else
                    acceptIcon.setVisibility(View.VISIBLE);
            }



            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("ds: ", String.valueOf(count));
                if(s.length() == 0){
                    acceptIcon.setVisibility(View.INVISIBLE);
                }
                else
                    acceptIcon.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        receiptNameTextInput = view.findViewById(R.id.edit_text_receipt_name);
        receiptNameTextInputLayout = view.findViewById(R.id.edit_text_receipt_name_lauout);

        if(!receipt.getName().isEmpty())
            receiptNameTextInput.setText(receipt.getName());


        tags = view.findViewById(R.id.chip_group_tags);

        createBasicChips();
        setAutoCompleteTextOfTags();
//        preview = view.findViewById(R.id.image_view_photo_preview);
//        preview.setImageBitmap(RotateBitmap(picture, 90));
    }

    String[] tags_string = new String[]{"ksiazki","ksiazki",
            "ksiazki","ksiazki","ksiazki","ksiazki","ksiazki","ksiazki","ksiazki","ksiazki","ksiazki"};


    private void setAutoCompleteTextOfTags() {
        ArrayAdapter adapterr = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, tags_string);

//        TODO Napisać włąsny adapter do tagów
        autoCompleteTextView.setAdapter(adapterr);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextView.setText("");
                String selected = (String) parent.getItemAtPosition(position);

                addChipToGroup(selected, tags);
            }
        });
//        mView.autoCompleteTextView.setAdapter<ArrayAdapter<String>>(adapter)
//                mView.autoCompleteTextView.setOnItemClickListener { parent, arg1, position, arg3 ->
//                mView.autoCompleteTextView.text = null
//            val selected = parent.getItemAtPosition(position) as String
//            addChipToGroup(selected, mView.chipGroup2)
//        }
    }

    private void addChipToGroup(String selected, final ChipGroup tags) {
        final Chip chip = new Chip(context);
        chip.setText("#" + selected);
//        chip.chipIcon = ContextCompat.getDrawable(requireContext(), baseline_person_black_18)
        chip.setCloseIconVisible(true);
//        chip.setChipIconTintResource(R.color.chipIconTint)

        // necessary to get single selection working
//        chip.isClickable = true
        chip.setCheckable(false);
        tags.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags.removeView(chip);
            }
        });
    }

    private void createBasicChips() {
//        CalendarChipContainer calendarChip = new CalendarChipContainer();
//        CalendarChipContainer rccc = new CalendarChipContainer(context);
//        PriceChipContainer pcc = new PriceChipContainer(context);
//        WarrantyChipContainer wcc = new WarrantyChipContainer(context);
//        GroupChossingContainer gcc = new GroupChossingContainer(context);
//
//        tags.addView(rccc.getCalendarChip());
//        tags.addView(pcc.getPriceChip());
//        tags.addView(wcc.getWarrantyChip());
//        tags.addView(gcc.getGroupChip());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CameraPreviewFragment.OnPhotoTakingListener) {
            mListener = (CameraPreviewFragment.OnPhotoTakingListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCallbackListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void notifyDataSetChanges() {
        adapter.notifyDataSetChanged();
    }

}
