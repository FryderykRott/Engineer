package com.fryderykrott.receiptcarerfinal.MainView.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fryderykrott.receiptcarerfinal.MainView.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.utils.Validator;
import com.fryderykrott.receiptcarerfinal.Services.Authentication;
import com.fryderykrott.receiptcarerfinal.Services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment implements View.OnClickListener, OnCompleteListener<AuthResult> {

    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText repeatedPassword;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayour;
    TextInputLayout repeatedPasswordLayout;

    MaterialButton createAccountButton;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.registerFragment_emailEditText);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailLayout.setErrorEnabled(false);
            }
        });
        password = view.findViewById(R.id.registerFragment_passwordEditText);
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordLayour.setErrorEnabled(false);
                repeatedPasswordLayout.setErrorEnabled(false);
            }
        });
        repeatedPassword = view.findViewById(R.id.registerFragment_passwordRepeatEditText);
        repeatedPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                repeatedPasswordLayout.setErrorEnabled(false);
                passwordLayour.setErrorEnabled(false);
            }
        });

        emailLayout = view.findViewById(R.id.registerFragment_email);
        passwordLayour = view.findViewById(R.id.registerFragment_password);
        repeatedPasswordLayout = view.findViewById(R.id.registerFragment_passwordRepeat);

        createAccountButton = view.findViewById(R.id.registerFragment_nextButton);
        createAccountButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean pass = true;

        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String passwordReapedString = repeatedPassword.getText().toString();

        String massage;

//        Walidowanie emaila i hasla
        if(!Validator.validateEmail(emailString)){
            massage = Validator.getMassage();
            emailLayout.setError(massage);
            pass = false;
        }

        if(!Validator.validateReapetedPassword(passwordString, passwordReapedString)){
            massage = Validator.getMassage();

            passwordLayour.setError(massage);
            repeatedPasswordLayout.setError(massage);

//            ((MainActivity) getActivity()).showSnackBar(massage);

            pass = false;
        }

        if(pass){
//            dodaj nowego uzytkwnika o podanych danych
            ((MainActivity) getActivity()).setProgressView(true);
            Authentication.getInstance().signUpUserWith(emailString, passwordString, this);
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
//        zapiszmy nowego uzytkownika do bazy danych
//        1. musi miec UID
//        2. Musi miec na poczaktu jedna grupę

        if(task.isSuccessful()){
            Database.getInstance().saveUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        ((MainActivity) getActivity()).setProgressView(false);

                        NavOptions navOptions = new NavOptions.Builder()
                                .setEnterAnim(R.anim.fab_slide_out_to_right)
                                .setPopUpTo(R.id.mobile_navigation, true).build();

                        Navigation.findNavController(getView()).navigate(R.id.navigation_receipts, null, navOptions, null);
                    }
                }
            });
        }
        else if(task.isCanceled()){
//            TODO zrobic to kiedys
            ((MainActivity) getActivity()).showSnackBar("Błąd");
        }


    }
}
