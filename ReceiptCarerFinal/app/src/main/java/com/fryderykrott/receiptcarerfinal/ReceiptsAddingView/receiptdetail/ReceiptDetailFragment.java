package com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.receiptdetail;


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
import com.fryderykrott.receiptcarerfinal.ReceiptAddingActivity;
import com.fryderykrott.receiptcarerfinal.adapters.ImageAdapter;
import com.fryderykrott.receiptcarerfinal.chips.CalendarChipContainer;
import com.fryderykrott.receiptcarerfinal.chips.GroupChossingContainer;
import com.fryderykrott.receiptcarerfinal.chips.PriceChipContainer;
import com.fryderykrott.receiptcarerfinal.chips.WarrantyChipContainer;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.Model.Tag;
import com.fryderykrott.receiptcarerfinal.ReceiptsAddingView.camerapreview.CameraPreviewFragment;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.utils.Validator;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ReceiptDetailFragment extends Fragment implements ImageAdapter.OnNewPhotoCallbackListener {

    public ReceiptDetailFragment() {
        // Required empty public constructor
    }

    Receipt receipt;

//    public ArrayList<Bitmap> photos;

    AutoCompleteTextView autoCompleteTextView;

    TextInputEditText receiptNameTextInput;
    TextInputLayout receiptNameTextInputLayout;

    ImageAdapter imagesAdapter;
    ChipGroup tagsChipGroup;

    ImageButton acceptIcon;

    Activity context;
    int position;

    ArrayAdapter adapterAutoCompliteTagsList;
    ArrayList<String> tagsToAutoComplite;
    ArrayList<String> receiptTags;

    ViewPager viewpager;
    CircleIndicator indicator;

    boolean isAlreadyCreted = false;
    private CameraPreviewFragment.OnPhotoTakingListener mListener;

    private GroupChossingContainer groupChossingContainerChip;
    private CalendarChipContainer calendarChipConteiner;
    private WarrantyChipContainer warrantyChipConteiner;
    private PriceChipContainer priceChipConteiner;

    public static ReceiptDetailFragment newInstance(Receipt receipt, Activity context, int position) {
        ReceiptDetailFragment fragment = new ReceiptDetailFragment();
        fragment.context = context;

        fragment.receipt = receipt;
        fragment.position = position;

        fragment.imagesAdapter = new ImageAdapter(context, receipt, position, fragment);
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

        if(isAlreadyCreted)
            return;

        viewpager = view.findViewById(R.id.view_pager_receipts);
        viewpager.setAdapter(imagesAdapter);

        indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
        imagesAdapter.registerDataSetObserver(indicator.getDataSetObserver());

        acceptIcon = view.findViewById(R.id.button);
        acceptIcon.setVisibility(View.INVISIBLE);
        acceptIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = autoCompleteTextView.getText().toString();
                if(!isTagReapeted(selected)){
                    receiptTags.add(selected);
                    tagsToAutoComplite.add(selected);
                    resetAdapter();

                    addChipToGroup(selected, tagsChipGroup);
                }
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


        tagsChipGroup = view.findViewById(R.id.chip_group_tags);

        receiptTags = new ArrayList<>();

//        share dobra, mamy paragon ktory ma tagi

        createBasicChips();
        setAutoCompleteTextOfTags();

        addReceiptsTagsToGroup();
        isAlreadyCreted = true;
//        preview = view.findViewById(R.id.image_view_photo_preview);
//        preview.setImageBitmap(RotateBitmap(picture, 90));
    }

    private void addReceiptsTagsToGroup() {
        ArrayList<String> tagsID = receipt.getTagsID();
        ArrayList<Tag> allTags = Utils.user.getTags();

        for(Tag tag: allTags){
            for(String tagUID: tagsID){
                if(tag.getUid().equals(tagUID)){
                    receiptTags.add(tag.getName());
                    addChipToGroup(tag.getName(), tagsChipGroup);
                }
            }
        }
    }

    private void setAutoCompleteTextOfTags() {
        tagsToAutoComplite = new ArrayList<>();
        for(Tag tag: Utils.user.getTags()){
            tagsToAutoComplite.add(tag.getName());
        }

        resetAdapter();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextView.setText("");
                String selected = (String) parent.getItemAtPosition(position);

                if(!isTagReapeted(selected)){
                    receiptTags.add(selected);
                    addChipToGroup(selected, tagsChipGroup);
                }
            }
        });

//        mView.autoCompleteTextView.setAdapter<ArrayAdapter<String>>(imagesAdapter)
//                mView.autoCompleteTextView.setOnItemClickListener { parent, arg1, position, arg3 ->
//                mView.autoCompleteTextView.text = null
//            val selected = parent.getItemAtPosition(position) as String
//            addChipToGroup(selected, mView.chipGroup2)
//        }
    }

    private boolean isTagReapeted(String selected) {
        for(String tag: receiptTags)
            if(selected.equals(tag))
                return true;

        return false;
    }

    private void addChipToGroup(final String selected, final ChipGroup tags) {

//        adapterAutoCompliteTagsList.notifyDataSetInvalidated();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

//        final Chip chip = new Chip(context);
        final Chip chip = myLayout.findViewById(R.id.basic_chip);

        if(chip.getParent() != null) {
            ((ViewGroup) chip.getParent()).removeView(chip); // <- fix
        }

        chip.setText("#" + selected);

        chip.setCloseIconVisible(true);

        chip.setCheckable(false);
        tags.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags.removeView(chip);

                tagsToAutoComplite.remove(selected);
                receiptTags.remove(selected);
                resetAdapter();

                adapterAutoCompliteTagsList.notifyDataSetInvalidated();
            }
        });
    }

    private void resetAdapter() {
        adapterAutoCompliteTagsList = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, tagsToAutoComplite);
        autoCompleteTextView.setAdapter(adapterAutoCompliteTagsList);
    }

    public void resetImageAdapter(){
        imagesAdapter.notifyDataSetChanged();

        imagesAdapter = new ImageAdapter(getActivity(), receipt);
        viewpager.setAdapter(imagesAdapter);

        imagesAdapter.registerDataSetObserver(indicator.getDataSetObserver());
        imagesAdapter.notifyDataSetChanged();
    }


    private void createBasicChips() {
//      Data na paragonie
        calendarChipConteiner = new CalendarChipContainer(context);
        if(!receipt.getDateOfCreation().isEmpty()){
            calendarChipConteiner.setDate(Utils.formatStringToDate(receipt.getDateOfCreation()));
            calendarChipConteiner.getCalendarChip().setText(receipt.getDateOfCreation());
        }
        tagsChipGroup.addView(calendarChipConteiner.getCalendarChip());

//        Gwarancja
      warrantyChipConteiner = new WarrantyChipContainer(context);
        tagsChipGroup.addView(warrantyChipConteiner.getWarrantyChip());

//        Cena na apragonie
        priceChipConteiner = new PriceChipContainer(context);
        tagsChipGroup.addView(priceChipConteiner.getPriceChip());

        if(receipt.getSumTotal() != 0f) {
            priceChipConteiner.setPriceOnChip(receipt.getSumTotal());
        }

//        Grupa i wybieranie grupy
        groupChossingContainerChip = new GroupChossingContainer(context);
        tagsChipGroup.addView(groupChossingContainerChip.getGroupChip());


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
        imagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewPhotoCallback(int position_off_receipt) {
//        zapisac stan wszysktich paragonów
//        nakladamy kolejny fragment, zapisac pozycje paragon z listy
//        mamy aktywnosc i jestes we fragmencie i mozemy wrzucić na wierzch fragment ktory cofając się wrzuci nas do aktywnosci, a w aktywnosic sa paragony, wiec trzeba powiedziae
//        ktory paragon jest akutalnie robione
        ReceiptAddingActivity a = ((ReceiptAddingActivity) getActivity());
        a.setCurrentReceiptPhotoTakingPosition(position);
        a.getNavController().navigate(R.id.navigation_camera_preview);

    }

    public boolean validateFragment() {
        String receiptName = receiptNameTextInput.getText().toString();
        boolean pass = true;

        if( !Validator.validateReceiptName(receiptName))
        {
            String massege = Validator.getMassage();
            receiptNameTextInputLayout.setError(massege);
            pass = false;
        }

        if(!Validator.validateGroupChoosingChip(groupChossingContainerChip))
        {
            groupChossingContainerChip.setError();
            pass = false;
        }

        return pass;
    }

//    groupChossingContainerChip;
//    private CalendarChipContainer calendarChipConteiner;
//    private WarrantyChipContainer warrantyChipConteiner;
//    private PriceChipContainer priceChipConteiner;

    public void prepareReceipt() {
//        1. dodaj datę do apragonu
//        2. updatów nazwę paragonu
//        3. ustal datę gwarancji paragonu
//        4. ustal grupę
//        5. włóż wszystkie znaczniki
        String receiptName = receiptNameTextInput.getText().toString();
        String dateOfCreation = Utils.formatDateToString(calendarChipConteiner.getDate());

        String dateWarantyDateEnd = "";
        if(warrantyChipConteiner.getDate_of_end() != null)
            if(warrantyChipConteiner.isInfiniteWarranty())
                receipt.setInfiniteWarranty(true);
            else
                dateWarantyDateEnd = Utils.formatDateToString(warrantyChipConteiner.getDate_of_end());

        String groupUID = groupChossingContainerChip.getGroup().getGroupID();
        float total = (float) priceChipConteiner.getPrice();

        receipt.setDateOfCreation(dateOfCreation);
        receipt.setName(receiptName);
        receipt.setDateOfEndOfWarrant(dateWarantyDateEnd);
        receipt.setGroupID(groupUID);
        receipt.setSumTotal(total);

        for(String tag: receiptTags){
            receipt.addTag(tag);
        }

        Log.i("dsa", "dsa");
    }

}
