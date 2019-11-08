package com.example.admin.appbarbottom.ReceipCarerChips;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;

import com.example.admin.appbarbottom.MainActivity;
import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.ReceiptCarerUtils.Utils;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.example.admin.appbarbottom.model.RCTag;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.LinkedList;

import adapter.ItemAdapter;

public class ChipGroupFiltrer {

    ArrayList<RCTag> all_tags;
    LinkedList<Chip> all_chips;
    LinkedList<Chip> selected_tags;
    LinkedList<Chip> current_tags;

    ArrayList<GroupReceipt> filtratedList;

    private final Activity a;
    private final ChipGroup group;
    private final SearchView searchView;

    private ItemAdapter itemAdapter;

    private RangeCalendarChipContainer rangeCalendarChipContainer;
    private RangePriceChipContainer rangePriceChipContainer;

    MaterialCardView cardViewChips;
//    TODO dodać warranty listenera

    public ChipGroupFiltrer(Activity a, final ChipGroup group, SearchView searchView, ItemAdapter items){
        this.a = a;
        this.group = group;
        this.searchView = searchView;
        this.itemAdapter = items;

        all_tags = Utils.global_Tags_set;
        createAllChips();
        selected_tags = new LinkedList<>();
        current_tags = new LinkedList<>();

        filtratedList = new ArrayList<>(100);

        rangeCalendarChipContainer = new RangeCalendarChipContainer(a);
        rangePriceChipContainer = new RangePriceChipContainer(a);


        cardViewChips = a.findViewById(R.id.card_view_chips);
        cardViewChips.setVisibility(View.GONE);

        searchViewListeners();

        group.removeAllViews();
        addBasicChipSet();
        resetBasicChipSet();
    }



    private void createAllChips() {
        all_chips = new LinkedList<>();
        String tagName;

//        moge bezpiecznie tak zrobic fora poneiwa ztagow zawsze jest wiecej niz grup i paragonow
        for(int i = 0; i < all_tags.size(); i++){

            if( i < Utils.global_Group_Set.size() ){
                tagName = Utils.global_Group_Set.get(i).getName();
                all_chips.add(createFilterChip(tagName));
            }

            if( i < Utils.global_Receipts_Set.size() ){
                tagName = Utils.global_Receipts_Set.get(i).getName();
                all_chips.add(createFilterChip(tagName));
            }

            tagName = all_tags.get(i).getName();
            all_chips.add(createFilterChip(tagName));
        }
    }

    private void updateBasicStateGroup() {
        group.removeAllViews();

        addBasicChipSet();
        addTagsToGroup(selected_tags);

        itemFiltr();
    }

    private void itemFiltr() {
        filtratedList.clear();
//        miec wszystkie elemnty w jednej duzej liscie do filtrowania, zaladowac je na samym poczatku
        addGroupsAndReceiptsToShow(Utils.global_Group_Set);
        addGroupsAndReceiptsToShow(Utils.global_Receipts_Set);
        itemAdapter.setArrayList(filtratedList);
        itemAdapter.notifyDataSetChanged();

    }

    private void addGroupsAndReceiptsToShow(ArrayList<GroupReceipt> groupReceipts) {
        for(int i = 0; i < groupReceipts.size(); i++){
            if(checkIfGroupReceiptsSetContainsCurrentFilters(groupReceipts.get(i)))
                filtratedList.add(groupReceipts.get(i));
        }
    }

    private boolean checkIfGroupReceiptsSetContainsCurrentFilters(GroupReceipt groupReceipt) {
        for(int i = 0; i < selected_tags.size(); i++){
            if(groupReceipt.toString().contains(selected_tags.get(i).getText()))
                return true;
        }
        return false;
    }

    public void addBasicChipSet(){
        group.addView(rangeCalendarChipContainer.getRangeCalendarChip());
        group.addView(rangePriceChipContainer.getRangePriceChip());
//        TODO dodać wiecej podstawowych klikniec jak gwarancja i czy grupa czu paragon

    }
    private void resetBasicChipSet() {
        rangeCalendarChipContainer.resetChip();
        rangePriceChipContainer.resetChip();
        //   TODO     dodać wiecej podstawowych klikniec jak gwarancja i czy grupa czu paragon
    }

    private void filtrTags(String tekst) {
        group.removeAllViews();
        current_tags.clear();
//        Podczas filtrowania beda sprawdzane rzeczy ktore sa zaznaczone plus dodatkowo te ktore sa zaznaczone
//        najpierw zrobmy zeby si epojawialy tylko te tagi ktore zawieraja w sobie nazwe chociaz torhce
//        1. Dodaj do current_tags wszystkie ktore przypominają tekst

        String tagName;

        for(int i = 0; i < all_tags.size(); i++){
            tagName = all_chips.get(i).getText().toString();
            if(tagName.contains(tekst))
                current_tags.add(createFilterChip(tagName));
        }

//        2. wyswietl je
        addTagsToGroup(current_tags);
    }

    private void addTagsToGroup(LinkedList<Chip> tags) {
        for(int i = 0; i < tags.size(); i++){
            group.addView(tags.get(i));
        }
    }

    private Chip createFilterChip(String tekst) {
        LayoutInflater inflater = a.getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.chips, null);

        Chip filterChip = myLayout.findViewById(R.id.basic_chip);

        if(filterChip.getParent() != null) {
            ((ViewGroup)filterChip.getParent()).removeView(filterChip); // <- fix
        }

        filterChip.setText(tekst);

        filterChip.setOnClickListener(new View.OnClickListener() {
            boolean isChecked = false;

            @Override
            public void onClick(View v) {
                Chip chip = (Chip) v;
                if(isChecked == false){
                    selected_tags.add(chip);
                    all_chips.remove(chip);
                    isChecked = true;
                }
                else {
                    selected_tags.remove(chip);
                    all_chips.add(chip);
                    updateBasicStateGroup();
                    isChecked = false;
                }
            }
        });

        return filterChip;
    }

    private void searchViewListeners() {
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) a).checkIfFabIsExpandedAndInCaseItIsCloseIt();
                ((MainActivity) a).floatingActionButton.hide();
                cardViewChips.setVisibility(View.VISIBLE);

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                cardViewChips.setVisibility(View.GONE);
                ((MainActivity) a).floatingActionButton.show();

                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateBasicStateGroup();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() >= 1)
                    filtrTags(newText);
                else
                    updateBasicStateGroup();

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updateBasicStateGroup();
//                addBasicChipSet();
//                resetBasicChipSet();
                return false;
            }
        });

    }


}
