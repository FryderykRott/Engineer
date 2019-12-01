package com.fryderykrott.receiptcarerfinal.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Receipt implements Serializable {

    public String name;
    public float sumTotal;
    public String dateOfCreation;
    public String dateOfEndOfWarrant;
    public String groupID;
    public ArrayList<String> tagsID;
    public ArrayList<String> images_as_base64;

    public ArrayList<Bitmap> images_as_bitmap;

    public Receipt(){
        name = "";
        sumTotal = 0f;
        dateOfCreation = "";
        dateOfEndOfWarrant = "";
        groupID = "";
        tagsID = new ArrayList<>();
        images_as_base64 = new ArrayList<>();
        images_as_bitmap = new ArrayList<>();
    }

    public ArrayList<String> getImages_as_base64() {
        return images_as_base64;
    }

    public void setImages_as_base64(ArrayList<String> images_as_base64) {
        this.images_as_base64 = images_as_base64;
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

    public void addTag(String shopName) {
//        TODO sprawdzanie czy to nowy tag czy stary
    }

    public ArrayList<Bitmap> getImages_as_bitmap() {
        return images_as_bitmap;
    }

    public void addReceiptBitmap(Bitmap bitmap) {
        if(images_as_bitmap == null)
            images_as_bitmap = new ArrayList<Bitmap>();

        images_as_bitmap.add(bitmap);
    }
}
