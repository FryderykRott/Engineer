package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.google.android.material.chip.Chip;

public class MiniBasicChipContainer {

    private Chip miniChip;

    String basic_text;

    public MiniBasicChipContainer(Context activity, String basic_text) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        View myLayout = inflater.inflate(R.layout.chips, null);

        miniChip = myLayout.findViewById(R.id.basic_chip_mini);

        if(miniChip.getParent() != null) {
            ((ViewGroup) miniChip.getParent()).removeView(miniChip); // <- fix
        }

        this.basic_text = basic_text;

        miniChip.setText(basic_text);
        miniChip.setClickable(false);
        miniChip.setCheckable(false);
    }

    public Chip getMiniChip() {
        return miniChip;
    }

    public void setMiniChip(Chip miniChip) {
        this.miniChip = miniChip;
    }

    public String getBasic_text() {
        return basic_text;
    }

    public void setBasic_text(String basic_text) {
        this.basic_text = basic_text;
    }
}
