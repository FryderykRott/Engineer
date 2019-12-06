package com.fryderykrott.receiptcarerfinal.MainView.groups;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fryderykrott.receiptcarerfinal.MainActivity;
import com.fryderykrott.receiptcarerfinal.R;
import com.fryderykrott.receiptcarerfinal.adapters.GroupsAdapter;
import com.fryderykrott.receiptcarerfinal.alertdialogs.GroupAddingAlertDialog;
import com.fryderykrott.receiptcarerfinal.Model.Group;
import com.fryderykrott.receiptcarerfinal.Services.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GroupsFragment extends Fragment implements GroupAddingAlertDialog.OnGroupCreationCallBackListener, View.OnClickListener{
    GroupsAdapter groupsAdapter;

   CardView add_new_group;

    public GroupsFragment (){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.groupsAdapter = new GroupsAdapter(view.getContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(groupsAdapter);

        add_new_group = view.findViewById(R.id.receipt_fragment_add_receipt_card);

        add_new_group.setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Dialog groupAddingAlertDialog = new GroupAddingAlertDialog(getActivity(), this, null);
        groupAddingAlertDialog.show();
    }

    @Override
    public void onGroupCreationCallBackListener(Dialog dialog, Group group) {
        Database.getInstance().saveGroupOfCurrentUser(group, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((MainActivity) getActivity()).showSnackBar("Udało się utworzyć grupę");

                groupsAdapter.notifyDataSetChanged();
            }
        });
    }


}