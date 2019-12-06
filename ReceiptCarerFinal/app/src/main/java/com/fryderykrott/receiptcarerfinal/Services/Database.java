package com.fryderykrott.receiptcarerfinal.Services;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.fryderykrott.receiptcarerfinal.Model.Tag;
import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.fryderykrott.receiptcarerfinal.Model.Group;
import com.fryderykrott.receiptcarerfinal.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Database{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage store = FirebaseStorage.getInstance();

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
        updateAllArrays(listener);
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
        updateAllArrays(voidOnCompleteListener);
    }

    public void uploadAndUpgradeReceipts(final ArrayList<Receipt> receipts, final OnCompleteListener<Void> listener){
        final String userDocRef = Utils.user.getDocRefUser();

        ArrayList<Bitmap> bitmaps;
        ByteArrayOutputStream baos;
        byte[] data;
        String path;
        StorageReference receiptImageRef;

        for (int i = 0; i < receipts.size(); i++) {
            final Receipt receipt = receipts.get(i);
            bitmaps = receipt.somethingDifferentImagesAsBitmap();
            for (Bitmap bitmap : bitmaps) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                data = baos.toByteArray();

                path = "receiptimages/" + UUID.randomUUID() + ".png";
                receiptImageRef = store.getReference(path);

                UploadTask uploadTask = receiptImageRef.putBytes(data);
                final String finalPath = path;

                final int finalI = i;
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url = uri.getResult();
                        receipt.getImages_as_base64().add(url.toString());

                        if (finalI == receipts.size() - 1) {
                            updateAllArrays(listener);
                        }

                    }});



            }
        }
    }


    public void updateAllArrays(final OnCompleteListener<Void> listener){

        final String userDocRef = Utils.user.getDocRefUser();
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

    }

}
