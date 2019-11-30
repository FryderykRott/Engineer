package com.fryderykrott.receiptcarerfinal.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

//    Static
    public int groupID;
    public String name;
    public int color;
    public ArrayList<Receipt> receipts;

//    Calculated
    public int receiptsNumber;
    public float sumOfAllReceipts;

    public Group(){}

    public Group(String name, int color) {
        this.name = name;
        this.color = color;

        receipts = new ArrayList<>();
    }

    //    Static
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(ArrayList<Receipt> receipts) {
        this.receipts = receipts;
    }

    public int getNumberOfReceipts(){
        return receipts.size();
    }

    public float getSumOfAllReceipts(){
        float receiptSum = 0f;

        for(int i = 0; i < receipts.size(); i++)
            receiptSum += receipts.get(i).getSumTotal();

        return receiptSum;
    }


    public int getReceiptsNumber() {
        return receiptsNumber;
    }

    public void setReceiptsNumber(int receiptsNumber) {
        this.receiptsNumber = receiptsNumber;
    }

    public void setSumOfAllReceipts(float sumOfAllReceipts) {
        this.sumOfAllReceipts = sumOfAllReceipts;
    }
}
