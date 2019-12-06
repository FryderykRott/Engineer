package com.fryderykrott.receiptcarerfinal.alertdialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.utils.Validator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ShareAlertDialog extends Dialog implements android.view.View.OnClickListener{
    public Receipt receipt;
    public Activity parent_activity;
    public Dialog d;

    public Button share;
    public Button cancel;

    TextInputLayout emailInputLayout;
    TextInputEditText emailInputEditText;

    private OnShareListener ls;

    public ShareAlertDialog(Activity a, OnShareListener ls, Receipt receipt){
        super(a);
        this.receipt = receipt;
        this.ls=ls;
        parent_activity=a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.alert_dialog_share);
        emailInputLayout = findViewById(R.id.emailTextInputLayout);
        emailInputEditText = findViewById(R.id.emailTextInputEditText);

        share = findViewById(R.id.button_ok);
        cancel=findViewById(R.id.button_cancel);

        share.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button_ok:
                String email = emailInputEditText.getText().toString();

                if(!Validator.validateEmail(email))
                {
                    String massage = Validator.getMassage();
                    emailInputLayout.setError(massage);
                    return;
                }

                String[] adresses = new String[1];
                adresses[0] = email;

                String bodyOfemail = "Oto paragon: ";

                ArrayList<String> imagesAsLinks = receipt.getImages_as_base64();

                for(String adress: imagesAsLinks){
                    bodyOfemail += "\n" + adress;
                }

                composeEmail(adresses, bodyOfemail);
                ls.onshareCallback();
                break;
            case R.id.button_cancel:

                break;
            default:
                break;

        }
        dismiss();
    }

    public void composeEmail(String[] addresses, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, addresses); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Paragon");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("message/rfc822");
        if (intent.resolveActivity(parent_activity.getPackageManager()) != null) {
            parent_activity.startActivity(intent);
        }
    }

    public interface OnShareListener {
        void onshareCallback();
    }
}
