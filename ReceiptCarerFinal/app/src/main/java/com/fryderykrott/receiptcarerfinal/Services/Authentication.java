package com.fryderykrott.receiptcarerfinal.Services;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Authentication {

    private static final Authentication ourInstance = new Authentication();

    public static Authentication getInstance() {
        return ourInstance;
    }

    private Authentication(){
    }

    public void signUserWith(String email, String password, OnCompleteListener<AuthResult> listener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void signUpUserWith(String email, String password, OnCompleteListener<AuthResult> listener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

}
