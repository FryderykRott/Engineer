package com.fryderykrott.receiptcarerfinal.services;

import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Database{
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final  Database ourInstance = new  Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database(){
    }

    public void saveUser(String UID, OnCompleteListener<Void>  listener) {
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
        DocumentReference doc = db.collection("User").document();
        User user = new User(UID);
        user.setDocRefUser(doc.getPath());

        doc.set(user).addOnCompleteListener(listener);

        Utils.user = user;
    }

    public void checkIfUserExists(String uid, OnCompleteListener<QuerySnapshot> voidOnCompleteListener) {
        db.collection("User").whereEqualTo("uid", uid).get().addOnCompleteListener(voidOnCompleteListener);
    }

    public void saveGroupOfCurrentUser(Group group, OnCompleteListener<Void> voidOnCompleteListener) {
        String userDocRef = Utils.user.getDocRefUser();
        ArrayList<Group> groups = Utils.user.getGroups();
        groups.add(group);

        db.document(userDocRef).update("groups", groups).addOnCompleteListener(voidOnCompleteListener);
    }

    public void reloadGroupsOfCurrentUser(OnCompleteListener<Void> voidOnCompleteListener) {
        String userDocRef = Utils.user.getDocRefUser();
        ArrayList<Group> groups = Utils.user.getGroups();

        db.document(userDocRef).update("groups", groups).addOnCompleteListener(voidOnCompleteListener);
    }
}
