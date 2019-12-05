package com.fryderykrott.receiptcarerfinal.chips;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.google.android.material.chip.Chip;

public class MiniBasicChipContainer {

    private Chip miniChip;

    String basic_text;
    Context context;

    public MiniBasicChipContainer(Context activity, String basic_text) {
        context = activity;
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

    public MiniBasicChipContainer(Context activity, Group group) {
        context = activity;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View myLayout = inflater.inflate(R.layout.chips, null);

        miniChip = myLayout.findViewById(R.id.mini_group_chip);

        if(miniChip.getParent() != null) {
            ((ViewGroup) miniChip.getParent()).removeView(miniChip); // <- fix
        }

        this.basic_text = group.getName();
        miniChip.setChipIcon(activity.getDrawable(R.drawable.ic_receipt));
        setMiniChipIconColor(group);
        miniChip.setText(basic_text);
        miniChip.setClickable(false);
        miniChip.setCheckable(false);
    }


    private void setMiniChipIconColor(Group group) {
        Drawable icon = miniChip.getChipIcon();

        switch (group.getColor()){
            case(1):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_1));
                break;
            case (2):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_2));
                break;
            case (3):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_3));
                break;
            case (4):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_4));
                break;
            case (5):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_5));
                break;
            case (6):
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_6));
                break;
            default:
                icon.setTint(context.getResources().getColor(R.color.folder_icon_color_basic));
                break;
        }

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
