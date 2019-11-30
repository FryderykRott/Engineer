package com.fryderykrott.receiptcarerfinal.mainacitivityUI.login;

import android.app.Activity;

import com.fryderykrott.receiptcarerfinal.services.Authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public class LoginFragmentPresenter {
    Activity activity;

    public void signUserWith(String email, String password, OnCompleteListener<AuthResult> listener) {

        Authentication.getInstance().signUserWith(email, password, listener);

    }

}
