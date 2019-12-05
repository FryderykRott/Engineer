package com.fryderykrott.receiptcarerfinal.mainacitivityUI.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fryderykrott.receiptcarerfinal.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.adapters.ReceiptsAdapter;
import com.fryderykrott.receiptcarerfinal.chips.OnChipAnwserListener;
import com.fryderykrott.receiptcarerfinal.chips.RangeCalendarChipContainer;
import com.fryderykrott.receiptcarerfinal.chips.RangePriceChipContainer;
import com.fryderykrott.receiptcarerfinal.chips.WarrantyChipContainer;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.model.Tag;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Date;

public class SearchFragment extends Fragment implements OnChipAnwserListener {

    SearchView searchView;
    ChipGroup chipGroupSpecials;
    ChipGroup chipGroupTags;

    ReceiptsAdapter receiptAdapter;

    ArrayList<Receipt> allReceipts;
    ArrayList<Receipt> filtredReceipts;

    ArrayList<String> tagsToAutoComplite;
    ArrayList<String> tagsToSearch;

    RecyclerView recyclerView;

    RangeCalendarChipContainer rangeCalendarChipContainer;
    RangePriceChipContainer rangePriceChipContainer;
    WarrantyChipContainer warrantyChipConteiner;
    View progresView;
    TextView notFoundText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allReceipts = Utils.user.receipts;
        tagsToSearch = new ArrayList<>();
        allReceipts = Utils.user.getReceipts();
        filtredReceipts = new ArrayList<>();

        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        setAdapter();

        declareSearchView();
        declareChipGroupSpecials();
        declareChipGroupTags();

        progresView = view.findViewById(R.id.progressView);
        notFoundText = view.findViewById(R.id.notFoundTextView);

    }

    private void declareSearchView() {
        searchView = getView().findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                validateTagOf(query.toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        tagsToAutoComplite = new ArrayList<>();
        ArrayList<Tag> tags = Utils.user.getTags();
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            tagsToAutoComplite.add(tag.getName().toLowerCase());
        }

        ArrayList<Group> groups = Utils.user.getGroups();

        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            tagsToAutoComplite.add(group.getName().toLowerCase());
        }

        for(Receipt receipt: allReceipts){
            tagsToAutoComplite.add(receipt.getName().toLowerCase());
        }

        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, tagsToAutoComplite);
        searchAutoComplete.setAdapter(dataAdapter);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                validateTagOf(selected);
            }
        });
    }

    private void validateTagOf(String tagName) {
        if(isReapeted(tagName.toLowerCase())){
            return;
        }
        addChipToGroup(tagName.toLowerCase());
        findReceipts();
    }

    private boolean isReapeted(String tagName) {
        for (String tag: tagsToSearch){
            if(tag.equals(tagName))
                return true;
        }

        return false;
    }

    private void addChipToGroup(final String selected) {

//        adapterAutoCompliteTagsList.notifyDataSetInvalidated();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

//        final Chip chip = new Chip(context);
        final Chip chip = myLayout.findViewById(R.id.basic_chip);

        if(chip.getParent() != null) {
            ((ViewGroup) chip.getParent()).removeView(chip); // <- fix
        }

        tagsToSearch.add(selected);
        chip.setText("#" + selected);

        chip.setCloseIconVisible(true);

        chip.setCheckable(false);
        chipGroupTags.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroupTags.removeView(chip);
                tagsToSearch.remove(selected);
                findReceipts();
//                resetAdapter();
            }
        });
    }

    private void declareChipGroupSpecials() {
        chipGroupSpecials = getView().findViewById(R.id.chipGroupSpecials);

//        range calendar
        rangeCalendarChipContainer = new RangeCalendarChipContainer(getActivity(), this);
        chipGroupSpecials.addView(rangeCalendarChipContainer.getRangeCalendarChip());

//        range proce
        rangePriceChipContainer = new RangePriceChipContainer(getActivity(), this);
        chipGroupSpecials.addView(rangePriceChipContainer.getRangePriceChip());

//
////        Grupa i wybieranie grupy
//        GroupChossingContainer groupChossingContainerChip = new GroupChossingContainer(getActivity());
//        chipGroupSpecials.addView(groupChossingContainerChip.getGroupChip());

        //        Gwarancja
        warrantyChipConteiner = new WarrantyChipContainer(getActivity(), this);
        chipGroupSpecials.addView(warrantyChipConteiner.getWarrantyChip());

    }

    private void declareChipGroupTags() {
        chipGroupTags = getView().findViewById(R.id.chipGroupTags);
    }

    private void setAdapter(){
        receiptAdapter = new ReceiptsAdapter((MainActivity)getActivity(), ((MainActivity)getActivity()).getNavController(), filtredReceipts);
        recyclerView.setAdapter(receiptAdapter);
    }

    private void findReceipts() {
        setProgressView(true);

        filtredReceipts = new ArrayList<>();


        boolean calendarChipIsSet = rangeCalendarChipContainer.isDateSet();
        boolean priceChipIsSet = rangePriceChipContainer.isPriceSet();
        boolean warrantyChipIsSet = warrantyChipConteiner.isWarrantySet();
//        sprawidzć warunki ze specjalnych chipów

        if(!calendarChipIsSet && !priceChipIsSet && !warrantyChipIsSet && tagsToSearch.isEmpty()){
            notFoundText.setVisibility(View.VISIBLE);
            setProgressView(false);
            setAdapter();
            return;
        }

        for(Receipt receipt: allReceipts){

            if(calendarChipIsSet)
            {
                if(!receiptInCalendarRange(receipt)){
                    continue;
                }
            }

            if (priceChipIsSet){
                if(!receiptInPriceRange(receipt)){
                    continue;
                }
            }
            if (warrantyChipIsSet){
                if(!receiptInWarrantyRange(receipt)){
                    continue;
                }
            }

//        sprawdz tagi

            if(!tagsToSearch.isEmpty()){
                if (!hasReceiptOneOfTagsOrGroup(receipt)){
                    continue;
                }
            }

//        sprawdz czy grupa jest o takiej nazwie

            filtredReceipts.add(receipt);
        }

        setAdapter();
        if(filtredReceipts.isEmpty())
            notFoundText.setVisibility(View.VISIBLE);
        else
            notFoundText.setVisibility(View.GONE);

        setProgressView(false);
    }

    private boolean hasReceiptOneOfTagsOrGroup(Receipt receipt) {
        ArrayList<String> receiptTags = new ArrayList<>();
        for(String tagsID: receipt.getTagsID()){
            receiptTags.add(Utils.findTagById(tagsID).getName().toLowerCase());
        }

        receiptTags.add(Utils.findGroupById(receipt.getGroupID()).getName().toLowerCase());
        receiptTags.add(receipt.getName().toLowerCase());

//        teraz jak mam wsyztkie tagi to mogę porownanie zrobić
        for(String filterTag: tagsToSearch){
            for(String receiptTag: receiptTags){
                if(filterTag.equals(receiptTag))
                    return true;
            }
        }

        return false;
    }

    private boolean receiptInCalendarRange(Receipt receipt) {

        Date startDate = rangeCalendarChipContainer.getFrom();
        Date endDate = rangeCalendarChipContainer.getTo();

        Date testDate = Utils.formatStringToDate(receipt.getDateOfCreation());
        if(endDate == null)
            return Utils.formatDateToString(testDate).equals(Utils.formatDateToString(startDate));

        return !(testDate.before(startDate) || testDate.after(endDate));
    }

    private boolean receiptInPriceRange(Receipt receipt) {

        float price = receipt.getSumTotal();

        float startPrice = rangePriceChipContainer.getFrom();
        float toPrice = rangePriceChipContainer.getTo();

        return !(price < startPrice || price > toPrice);
    }

    private boolean receiptInWarrantyRange(Receipt receipt) {
        Date warranty = Utils.formatStringToDate(receipt.getDateOfEndOfWarrant());
        Date warrantyLimit = warrantyChipConteiner.getDate_of_end();

        return warranty.before(warrantyLimit);
    }

    @Override
    public void onChipAnwser() {
        findReceipts();
    }


    public void setProgressView(boolean isVisible) {
        if(isVisible){
            progresView.setVisibility(View.VISIBLE);
        }
        else{
            progresView.setVisibility(View.GONE);
        }
    }
}