package com.fryderykrott.receiptcarerfinal.mainacitivityUI.receipts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.fryderykrott.receiptcarerfinal.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.Utils;
import com.fryderykrott.receiptcarerfinal.model.User;
import com.fryderykrott.receiptcarerfinal.services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReceiptsFragment extends Fragment {

    private FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_receipts, container, false);


        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            Navigation.findNavController(getView()).navigate(R.id.navigation_login);
        }
        else{
            loadUser();
        }

//        updateUI(currentUser);
    }

    private void loadUser() {
        ((MainActivity) getActivity()).setProgressView(true);

        final FirebaseUser user = mAuth.getCurrentUser();
        Database.getInstance().checkIfUserExists(user.getUid(),new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    ArrayList<User> usersButOneUser = (ArrayList<User>) task.getResult().toObjects(User.class);

                    if (usersButOneUser.isEmpty()) {
                        Database.getInstance().saveUser(user.getUid(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ((MainActivity) getActivity()).setProgressView(false);
                            }
                        });
                    }
                    else {
                        Utils.user = usersButOneUser.get(0);
                        ((MainActivity) getActivity()).setProgressView(false);
                    }


                }
            }
        });

    }

    private void updateUI(FirebaseUser currentUser) {

    }
}