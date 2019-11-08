package com.example.admin.appbarbottom.fragments.receipt.adding;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.ReceipCarerChips.CalendarChipContainer;
import com.example.admin.appbarbottom.ReceipCarerChips.GroupChossingContainer;
import com.example.admin.appbarbottom.ReceipCarerChips.PriceChipContainer;
import com.example.admin.appbarbottom.ReceipCarerChips.WarrantyChipContainer;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import adapter.ImageAdapter;
import adapter.OnNewPhotoCallbackListener;
import me.relex.circleindicator.CircleIndicator;

public class AddingReceiptFragment extends Fragment implements View.OnClickListener {

    GroupReceipt receipt;

//    public ArrayList<Bitmap> photos;

    AutoCompleteTextView autoCompleteTextView;

    ImageAdapter adapter;
    ChipGroup tags;
    ImageButton acceptIcon;

    Activity context;
    int position;
    private CameraPictureTakingFragment.OnFragmentPhotoTakingCallbackListener mListener;

    public static AddingReceiptFragment newInstance(GroupReceipt receipt, Activity context, int position) {
        AddingReceiptFragment fragment = new AddingReceiptFragment();
        fragment.context = context;

        fragment.receipt = receipt;
        fragment.position = position;

        fragment.adapter = new ImageAdapter(context, receipt.getImages_as_bitmap(), position, (OnNewPhotoCallbackListener) context);
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
        return inflater.inflate(R.layout.fragment_receipt_details, container, false);
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
        CalendarChipContainer rccc = new CalendarChipContainer(context);
        PriceChipContainer pcc = new PriceChipContainer(context);
        WarrantyChipContainer wcc = new WarrantyChipContainer(context);
        GroupChossingContainer gcc = new GroupChossingContainer(context);

        tags.addView(rccc.getCalendarChip());
        tags.addView(pcc.getPriceChip());
        tags.addView(wcc.getWarrantyChip());
        tags.addView(gcc.getGroupChip());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CameraPictureTakingFragment.OnFragmentPhotoTakingCallbackListener) {
            mListener = (CameraPictureTakingFragment.OnFragmentPhotoTakingCallbackListener) context;
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

    @Override
    public void onClick(View v) {

    }

    public void notifyDataSetChanges() {
        adapter.notifyDataSetChanged();
    }

    public interface OnFragmentReceiptCallbackListener {
        // TODO: Update argument type and name
        void onFragmentReceiptCallback(GroupReceipt receipt, GroupReceipt group);
    }

    private void validateData(){
//         trzeba sprawdzać czy są wypełnione obowiazakowe pola
//         1. czy jest data, jak nie to spytaj czy dac dzisiejszą
//        2. jesli nie ma gwarancji to jej nie ma i tyle
//        3. je
    }

}