package com.fryderykrott.receiptcarerfinal.MainView.receipts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fryderykrott.receiptcarerfinal.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.ReceiptAddingActivity;
import com.fryderykrott.receiptcarerfinal.adapters.ReceiptsAdapter;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.Model.User;
import com.fryderykrott.receiptcarerfinal.Services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReceiptsFragment extends Fragment {

    private FirebaseAuth mAuth;
    CardView add_new_receipt;
    ReceiptsAdapter receiptAdapter;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        add_new_receipt = view.findViewById(R.id.receipt_fragment_add_receipt_card);
        add_new_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ReceiptAddingActivity.class);
                startActivityForResult(intent, -1);

            }
        });
        setRecycleViewAndAdapter();
        checkCameraPermissions();
    }

    private void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            add_new_receipt.setVisibility(View.INVISIBLE);
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        else
            add_new_receipt.setVisibility(View.VISIBLE);
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
        if(Utils.user != null)
            return;

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
//                                                             Group group = new Group();
//                                group.setName("Ogólne");
//                                group.setColor(-1);

//                                Database.getInstance().saveGroupOfCurrentUser(group, new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//
//                                    }
//                                });
                                setRecycleViewAndAdapter();
                                ((MainActivity) getActivity()).setProgressView(false);
                            }
                        });
                    }
                    else {
                        Utils.user = usersButOneUser.get(0);
                        ((MainActivity) getActivity()).setProgressView(false);
                        setRecycleViewAndAdapter();
                    }


                }
            }
        });

    }

    private void setRecycleViewAndAdapter(){
        receiptAdapter = new ReceiptsAdapter((MainActivity)getActivity(), ((MainActivity)getActivity()).getNavController());
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(receiptAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(receiptAdapter != null)
            setRecycleViewAndAdapter();
    }
}