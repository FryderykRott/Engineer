package com.fryderykrott.receiptcarerfinal.MainView.login;

import android.app.Activity;

import com.fryderykrott.receiptcarerfinal.Services.Authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public class LoginFragmentPresenter {
    Activity activity;

    public void signUserWith(String email, String password, OnCompleteListener<AuthResult> listener) {

        Authentication.getInstance().signUserWith(email, password, listener);

    }

}
