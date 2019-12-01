package com.fryderykrott.receiptcarerfinal.alertdialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.utils.Validator;
import com.fryderykrott.receiptcarerfinal.model.Group;

public class GroupAddingAlertDialog extends Dialog implements android.view.View.OnClickListener {

    public Context context;
    public Dialog d;

    public Button ok;
    public Button cancel;

    ImageView fab_color_for_folder_1;
    ImageView fab_color_for_folder_2;
    ImageView fab_color_for_folder_3;
    ImageView fab_color_for_folder_4;
    ImageView fab_color_for_folder_5;
    ImageView fab_color_for_folder_6;

    ImageView check_1;
    ImageView check_2;
    ImageView check_3;
    ImageView check_4;
    ImageView check_5;
    ImageView check_6;

    EditText editTextGroupname;

    Group toEditGroup;
    TextView titleTextView;

    private int layoutID = R.layout.alertdialog_add_new_group;
    private OnGroupCreationCallBackListener l_creation;
    private OnGroupEditionCallBackListener l_edition;

    int chosenColor;
    ImageView currenCheck;

    //    public static final int
    public GroupAddingAlertDialog(Context a, OnGroupCreationCallBackListener l_creation, OnGroupEditionCallBackListener l_edition) {
        super(a);

        this.l_creation = l_creation;
        this.l_edition = l_edition;

        context = a;
    }

    public void editGroupMode( Group group){
        toEditGroup = group;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(layoutID);
        ok = findViewById(R.id.button_ok);
        cancel = findViewById(R.id.button_cancel);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        fab_color_for_folder_1 = findViewById(R.id.fab_color_for_folder_1);
        fab_color_for_folder_1.setOnClickListener(this);

        fab_color_for_folder_2 = findViewById(R.id.fab_color_for_folder_2);
        fab_color_for_folder_2.setOnClickListener(this);

        fab_color_for_folder_3 = findViewById(R.id.fab_color_for_folder_3);
        fab_color_for_folder_3.setOnClickListener(this);

        fab_color_for_folder_4 = findViewById(R.id.fab_color_for_folder_4);
        fab_color_for_folder_4.setOnClickListener(this);

        fab_color_for_folder_5 = findViewById(R.id.fab_color_for_folder_5);
        fab_color_for_folder_5.setOnClickListener(this);

        fab_color_for_folder_6 = findViewById(R.id.fab_color_for_folder_6);
        fab_color_for_folder_6.setOnClickListener(this);

        check_1 = findViewById(R.id.check_1);
        check_2 = findViewById(R.id.check_2);
        check_3 = findViewById(R.id.check_3);
        check_4 = findViewById(R.id.check_4);
        check_5 = findViewById(R.id.check_5);
        check_6 = findViewById(R.id.check_6);

        editTextGroupname = findViewById(R.id.edit_text_group_name);

        titleTextView = findViewById(R.id.title);

        if(toEditGroup == null){
            chosenColor = 1;
            setCurrentColor(check_1);
        }
        else{
            titleTextView.setText("Edytuj grupę");
            chosenColor = toEditGroup.getColor();
            editTextGroupname.setText(toEditGroup.getName());

            switch (chosenColor){
                case (1):
                    setCurrentColor(check_1);
                    break;
                case (2):
                    setCurrentColor(check_2);
                    break;
                case (3):
                    setCurrentColor(check_3);
                    break;
                case (4):
                    setCurrentColor(check_4);
                    break;
                case (5):
                    setCurrentColor(check_5);
                    break;
                case (6):
                    setCurrentColor(check_6);
                    break;
            }
//            zrobic tutaj takie dane jakie ma stuff
        }
//        setParams(GroupReceipt.COLOR_CHOSEN_1, "New group");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                okGroup();
                break;
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.fab_color_for_folder_1:
                setCurrentColor(check_1);
                chosenColor = 1;
                break;
            case R.id.fab_color_for_folder_2:
                setCurrentColor(check_2);
                chosenColor = 2;
                break;
            case R.id.fab_color_for_folder_3:
                setCurrentColor(check_3);
                chosenColor = 3;
                break;
            case R.id.fab_color_for_folder_4:
                setCurrentColor(check_4);
                chosenColor = 4;
                break;
            case R.id.fab_color_for_folder_5:
                setCurrentColor(check_5);
                chosenColor = 5;
                break;
            case R.id.fab_color_for_folder_6:
                setCurrentColor(check_6);
                chosenColor = 6;
                break;
            default:
                break;
        }

    }


    private void okGroup() {
        String name = editTextGroupname.getText().toString();

        if(toEditGroup == null){
            if(Validator.validateGroupName(name)){
                Group group = new Group(name, chosenColor);
                l_creation.onGroupCreationCallBackListener(this, group);
                dismiss();
            }
            else {
                String massage = Validator.getMassage();
                editTextGroupname.setError(massage);
            }

        }
        else {
            if(Validator.validateEditGroupName(name, toEditGroup.getName())){
                toEditGroup.setName(name);
                toEditGroup.setColor(chosenColor);

                l_edition.onGroupEditionCallBackListener(this, toEditGroup);
                dismiss();
            }
            else {
                String massage = Validator.getMassage();
                editTextGroupname.setError(massage);
            }

        }





    }

    private void setCurrentColor(ImageView check) {
        if(currenCheck == check)
            return;

        if(currenCheck != null)
        {
            currenCheck.setVisibility(View.GONE);
        }

        currenCheck = check;
        check.setVisibility(View.VISIBLE);
    }
//
//    private void changeColorOfFolder(int color_chosen) {
//
//        if(color_chosen == GroupReceipt.COLOR_CHOSEN_1)
//            folder_preview.setImageResource(R.drawable.ic_folder_big);
//        else if(color_chosen == GroupReceipt.COLOR_CHOSEN_2)
//            folder_preview.setImageResource(R.drawable.ic_folder_big_2);
//        else if(color_chosen == GroupReceipt.COLOR_CHOSEN_3)
//            folder_preview.setImageResource(R.drawable.ic_folder_big_3);
//        else if(color_chosen == GroupReceipt.COLOR_CHOSEN_4)
//            folder_preview.setImageResource(R.drawable.ic_folder_big_4);
//
//        chosenColor = color_chosen;
//    }


    private void changeText(String group_name) {

        if(group_name.isEmpty())
            return;
        else
            editTextGroupname.setText(group_name);
    }

    public interface OnGroupCreationCallBackListener {
        public void onGroupCreationCallBackListener(Dialog dialog, Group ew_group);
    }

    public interface OnGroupEditionCallBackListener {
        public void onGroupEditionCallBackListener(Dialog dialog,Group group);
    }

    public Group getToEditGroup() {
        return toEditGroup;
    }

    public void setToEditGroup(Group toEditGroup) {
        this.toEditGroup = toEditGroup;
    }


}

