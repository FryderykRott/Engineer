package com.fryderykrott.receiptcarerfinal.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Receipt implements Serializable {

    public String name;
    public float sumTotal;
    public long dateOfCreation;
    public long dateOfEndOfWarrant;
    public int groupID;
    public ArrayList<Integer> tagsID;

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

    public long getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(long dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public long getDateOfEndOfWarrant() {
        return dateOfEndOfWarrant;
    }

    public void setDateOfEndOfWarrant(long dateOfEndOfWarrant) {
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
}
