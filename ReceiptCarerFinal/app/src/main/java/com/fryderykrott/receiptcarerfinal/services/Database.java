package com.fryderykrott.receiptcarerfinal.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.model.ReceiptImage;
import com.fryderykrott.receiptcarerfinal.model.Tag;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

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

        Group basic_group = new Group(Utils.BASIC_GROUP_NAME, -1);
        basic_group.setIs_deletable(false);

        user.getGroups().add(basic_group);
        user.setDocRefUser(doc.getPath());

        doc.set(user).addOnCompleteListener(listener);
        Utils.user = user;
    }

    public void checkIfUserExists(String uid, OnCompleteListener<QuerySnapshot> voidOnCompleteListener) {
        db.collection("User").whereEqualTo("uid", uid).get().addOnCompleteListener(voidOnCompleteListener);
    }

    public void updateUser(final OnCompleteListener<Void>  listener) {
        final String userDocRef = Utils.user.getDocRefUser();
        updateAllArrays(userDocRef, listener);
//        db.document(Utils.user.getDocRefUser()).set(Utils.user).addOnCompleteListener(listener);
    }

    public void saveGroupOfCurrentUser(Group group, OnCompleteListener<Void> voidOnCompleteListener) {
        String userDocRef = Utils.user.getDocRefUser();
        ArrayList<Group> groups = Utils.user.getGroups();
        groups.add(group);

        db.document(userDocRef).update("groups", groups).addOnCompleteListener(voidOnCompleteListener);
    }

    public void reloadGroupsOfCurrentUser(OnCompleteListener<Void> voidOnCompleteListener) {
        String userDocRef = Utils.user.getDocRefUser();
        updateAllArrays(userDocRef, voidOnCompleteListener);
    }

    private void updateAllArrays(final String userDocRef, final OnCompleteListener<Void> listener){
        final ArrayList<Group> groups = Utils.user.getGroups();
        final ArrayList<Tag> tags = Utils.user.getTags();
        final ArrayList<Receipt> receipts = Utils.user.getReceipts();
        final ArrayList<ReceiptImage> receiptImages = Utils.user.getReceiptImages();


        // Get a new write batch
        WriteBatch batch = db.batch();

// Set the value of 'NYC'

        DocumentReference nycRef = db.document(userDocRef);
        batch.update(nycRef, "groups", groups);
        batch.update(nycRef, "tags", tags);
        batch.update(nycRef, "receipts", receipts);
//        batch.update(nycRef, "receiptImages", receiptImages);
// Commit the batch
        batch.commit().addOnCompleteListener(listener).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("ds","dafagdagsdg");
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.w("ds","dafagdagsdg");
            }
        });

        
//        db.document(userDocRef).update("groups", groups).addOnCompleteListener(listener);
//        db.document(userDocRef).set(receipts);
//        db.document(userDocRef).update("tags", tags);
//        db.document(userDocRef).set(receiptImages).addOnCompleteListener(listener);
//        Utils.user.loadBitmapImages();
    }

//
//    private void updateAllArrays(final String userDocRef, final OnCompleteListener<Void> listener){
//        final ArrayList<Group> groups = Utils.user.getGroups();
//        final ArrayList<Tag> tags = Utils.user.getTags();
//        final ArrayList<Receipt> receipts = Utils.user.getReceipts();
//        final ArrayList<ReceiptImage> receiptImages = Utils.user.getReceiptImages();
//
//        db.document(userDocRef).update("groups", groups).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                db.document(userDocRef).set(tags).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        db.document(userDocRef).set(receipts).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                db.document(userDocRef).set(receiptImages).addOnCompleteListener(listener);
//                            }
//                        });
//                    }
//                });
//
//            }
//        });
////        Utils.user.loadBitmapImages();
//    }
}
