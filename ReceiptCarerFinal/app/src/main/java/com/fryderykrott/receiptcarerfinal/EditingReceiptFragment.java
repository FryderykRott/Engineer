package com.fryderykrott.receiptcarerfinal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fryderykrott.receiptcarerfinal.adapters.ImageAdapter;
import com.fryderykrott.receiptcarerfinal.adapters.ImageURLAdapter;
import com.fryderykrott.receiptcarerfinal.alertdialogs.ShareAlertDialog;
import com.fryderykrott.receiptcarerfinal.chips.CalendarChipContainer;
import com.fryderykrott.receiptcarerfinal.chips.GroupChossingContainer;
import com.fryderykrott.receiptcarerfinal.chips.PriceChipContainer;
import com.fryderykrott.receiptcarerfinal.chips.WarrantyChipContainer;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.model.Tag;
import com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview.camerapreview.CameraPreviewFragment;
import com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview.receiptdetail.ReceiptDetailFragment;
import com.fryderykrott.receiptcarerfinal.services.Database;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.utils.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class EditingReceiptFragment extends Fragment implements ImageAdapter.OnNewPhotoCallbackListener, ShareAlertDialog.OnShareListener {

    Receipt receiptToEdit;
    Receipt receipt;

//    public ArrayList<Bitmap> photos;

    AutoCompleteTextView autoCompleteTextView;

    TextInputEditText receiptNameTextInput;
    TextInputLayout receiptNameTextInputLayout;

    ImageURLAdapter imagesAdapter;
    ChipGroup tagsChipGroup;

    ImageButton acceptIcon;

    MainActivity context;
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

    public EditingReceiptFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = (MainActivity) getActivity();

        receiptToEdit = context.getReceiptToEdit();
        receipt = new Receipt(receiptToEdit);
        imagesAdapter = new ImageURLAdapter(context, receipt, this, this);

        return inflater.inflate(R.layout.fragment_editing_receipt, container, false);
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

        setToolbar(view);
    }

    private void setToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_adding_receipts);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).getNavController().popBackStack();
            }
        });


        ActionMenuItemView accept_button = toolbar.findViewById(R.id.accept);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Validate
                boolean pass = true;

                if(!validateFragment())
                    pass = false;

                if(pass){
                    ((MainActivity)getActivity()).setProgressView(true);
                    prepareReceipt();
                    Database.getInstance().updateAllArrays( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ((MainActivity)getActivity()).getNavController().popBackStack();
                            ((MainActivity)getActivity()).setProgressView(false);
                            ((MainActivity)getActivity()).showSnackBar("Pomyślnie poprawiono paragon");

                        }
                    });
                }
                else
                    ((ReceiptAddingActivity) getActivity()).showSnackBar("Sprawdż wszystkie dane na paragonach!");
            }
        });

        ActionMenuItemView share_button = toolbar.findViewById(R.id.share);
        final EditingReceiptFragment fragment = this;
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareAlertDialog ad = new ShareAlertDialog(getActivity(), fragment, receipt);
                ad.show();
            }
        });

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

        new ImageURLAdapter(context, receipt, null, this);
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
        if(!receipt.getDateOfEndOfWarrant().isEmpty()){
            warrantyChipConteiner.setWarranty(Utils.formatStringToDate(receipt.getDateOfEndOfWarrant()));
        }

        tagsChipGroup.addView(warrantyChipConteiner.getWarrantyChip());

//        Cena na apragonie
        priceChipConteiner = new PriceChipContainer(context);
        tagsChipGroup.addView(priceChipConteiner.getPriceChip());

        if(receipt.getSumTotal() != 0f) {
            priceChipConteiner.setPriceOnChip(receipt.getSumTotal());
        }

//        Grupa i wybieranie grupy
        groupChossingContainerChip = new GroupChossingContainer(context);
       if(!receipt.getGroupID().isEmpty())
           groupChossingContainerChip.setGroup(Utils.findGroupById(receipt.getGroupID()));

        tagsChipGroup.addView(groupChossingContainerChip.getGroupChip());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CameraPreviewFragment.OnPhotoTakingListener) {
            mListener = (CameraPreviewFragment.OnPhotoTakingListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentCallbackListener");
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
//        ((MainActivity)getActivity()).getNavController().navigate(R.id.navigation_camera_preview);
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

    public void prepareReceipt() {
        String receiptName = receiptNameTextInput.getText().toString();
        String dateOfCreation = Utils.formatDateToString(calendarChipConteiner.getDate());

        String dateWarantyDateEnd = "";
        if(warrantyChipConteiner.getDate_of_end() != null)
            if(warrantyChipConteiner.isInfiniteWarranty())
                receiptToEdit.setInfiniteWarranty(true);
            else
                dateWarantyDateEnd = Utils.formatDateToString(warrantyChipConteiner.getDate_of_end());

        String groupUID = groupChossingContainerChip.getGroup().getGroupID();
        float total = (float) priceChipConteiner.getPrice();

        receiptToEdit.setDateOfCreation(dateOfCreation);
        receiptToEdit.setName(receiptName);
        receiptToEdit.setDateOfEndOfWarrant(dateWarantyDateEnd);
        receiptToEdit.setGroupID(groupUID);
        receiptToEdit.setSumTotal(total);

        receiptToEdit.getTagsID().clear();
        for(String tag: receiptTags){
            receiptToEdit.addTag(tag);
        }

        Log.i("dsa", "dsa");
    }

    @Override
    public void onshareCallback() {
        ((MainActivity)getActivity()).showSnackBar("Wysłano maila z paragonem");
    }
}
