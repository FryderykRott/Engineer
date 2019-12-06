package com.fryderykrott.receiptcarerfinal.Model;

import android.graphics.Bitmap;

import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

@IgnoreExtraProperties
public class Receipt implements Serializable {

    public String receiptUID;
    public String name;
    public float sumTotal;
    public String dateOfCreation;
    public String dateOfEndOfWarrant;
    public String groupID;
    public ArrayList<String> tagsID;
    public ArrayList<String> images_as_base64;

    private ArrayList<Bitmap> images_as_bitmap;

    public boolean isInfiniteWarranty;

    public Receipt(){
        receiptUID = UUID.randomUUID().toString();
        name = "";
        sumTotal = 0f;
        dateOfCreation = "";
        dateOfEndOfWarrant = "";
        groupID = "";
        tagsID = new ArrayList<>();
        images_as_bitmap = new ArrayList<>();
        images_as_base64 = new ArrayList<>();
        isInfiniteWarranty = false;
    }

    public Receipt(Receipt receipt) {
        name = receipt.name;
        sumTotal = receipt.sumTotal;
        dateOfCreation = receipt.dateOfCreation;
        dateOfEndOfWarrant = receipt.dateOfEndOfWarrant;
        groupID = receipt.groupID;
        tagsID = new ArrayList<>(receipt.tagsID);
        images_as_bitmap = new ArrayList<>(receipt.images_as_bitmap);
        images_as_base64 = new ArrayList<>(receipt.images_as_base64);
        isInfiniteWarranty = receipt.isInfiniteWarranty;
    }

    public String getReceiptUID() {
        return receiptUID;
    }

    public void setReceiptUID(String receiptUID) {
        this.receiptUID = receiptUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSumTotal() {
        return sumTotal;
    }

    public void setSumTotal(float sumTotal) {
        this.sumTotal = sumTotal;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getDateOfEndOfWarrant() {
        return dateOfEndOfWarrant;
    }

    public void setDateOfEndOfWarrant(String dateOfEndOfWarrant) {
        this.dateOfEndOfWarrant = dateOfEndOfWarrant;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public ArrayList<String> getTagsID() {
        return tagsID;
    }

    public void setTagsID(ArrayList<String> tagsID) {
        this.tagsID = tagsID;
    }

    public void addTag(String tagName) {
//        TODO sprawdzanie czy to nowy tag czy stary

        Tag currentTag;
        for(String tagID: tagsID){
            currentTag = Utils.findTagById(tagID);
            if(currentTag.getName().equals(tagName)){
                return;
            }
        }

        ArrayList<Tag> allTags = Utils.user.getTags();

        for(Tag tag: allTags){
            if(tag.getName().equals(tagName)){
                tagsID.add(tag.getUid());
                return;
            }
        }


        Tag newTag = new Tag(tagName);
        Utils.user.getTags().add(newTag);
        tagsID.add(newTag.getUid());
    }

    @Exclude
    public ArrayList<Bitmap> somethingDifferentImagesAsBitmap() {
        return images_as_bitmap;
    }

    public void addReceiptBitmap(Bitmap bitmap) {
        if(images_as_bitmap == null)
            images_as_bitmap = new ArrayList<>();

//        if(images_as_base64 == null)
//            images_as_base64 = new ArrayList<>();
//
//        images_as_base64.add(Utils.bitmapToBase64(bitmap));
//
//        String imageAsBase64 = Utils.bitmapToBase64(bitmap);
//        Utils.user.getReceiptImages().add(new ReceiptImage(receiptUID, imageAsBase64));
        images_as_bitmap.add(bitmap);
    }


    public boolean isInfiniteWarranty() {
        return isInfiniteWarranty;
    }


    public void setInfiniteWarranty(boolean infiniteWarranty) {
        isInfiniteWarranty = infiniteWarranty;
    }



    public ArrayList<String> getImages_as_base64() {
        return images_as_base64;
    }

    public void setImages_as_base64(ArrayList<String> images_as_base64) {
        this.images_as_base64 = images_as_base64;
    }
}
