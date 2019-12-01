package com.fryderykrott.receiptcarerfinal.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Receipt implements Serializable {

    public String name;
    public float sumTotal;
    public String dateOfCreation;
    public String dateOfEndOfWarrant;
    public int groupID;
    public ArrayList<Integer> tagsID;
    public ArrayList<String> images_as_base64;

    public ArrayList<Bitmap> images_as_bitmap;

    public Receipt(){}

    

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

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public ArrayList<Integer> getTagsID() {
        return tagsID;
    }

    public void setTagsID(ArrayList<Integer> tagsID) {
        this.tagsID = tagsID;
    }

    public void addTag(String shopName) {
//        TODO sprawdzanie czy to nowy tag czy stary
    }

    public ArrayList<Bitmap> getImages_as_bitmap() {
    }
}
