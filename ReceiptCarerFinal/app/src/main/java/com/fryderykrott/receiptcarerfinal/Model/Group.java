package com.fryderykrott.receiptcarerfinal.Model;

import com.fryderykrott.receiptcarerfinal.utils.Utils;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Group implements Serializable {

//    czy mozna usunąć
    public boolean is_deletable;
//    Static
    public String groupID;
    public String name;
    public int color;
//    public ArrayList<Receipt> receipts;

//    Calculated
    @Exclude
    public int receiptsNumber;

    @Exclude
    public float sumOfAllReceipts;

    public Group(){
        this.groupID = UUID.randomUUID().toString();
//        receipts = new ArrayList<>();
        is_deletable = true;
    }

    public Group(String name, int color) {
        this.groupID = UUID.randomUUID().toString();
        this.name = name;
        this.color = color;

        is_deletable = true;
//        receipts = new ArrayList<>();
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

    public String  getGroupID() {
        return groupID;
    }

    public void setGroupID(String  groupID) {
        this.groupID = groupID;
    }
//
//    public ArrayList<Receipt> getReceipts() {
//        return receipts;
//    }
//
//    public void setReceipts(ArrayList<Receipt> receipts) {
//        this.receipts = receipts;
//    }

    public int getNumberOfReceipts(){
        int size = 0;
        if(Utils.user == null)
            return 0;

        for(Receipt receipt:Utils.user.getReceipts()){
            if(receipt.getGroupID().equals(groupID))
                size++;
        }

        return size;
    }

    public float getSumOfAllReceipts(){
        float receiptSum = 0f;

        if(Utils.user == null)
            return 0f;

        Receipt receipt;
        ArrayList<Receipt> receipts = Utils.user.getReceipts();
        for(int i = 0; i < receipts.size(); i++){
            receipt = receipts.get(i);

            if(receipt.getGroupID().equals(groupID))
                receiptSum += receipts.get(i).getSumTotal();

        }

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

    public boolean isIs_deletable() {
        return is_deletable;
    }

    public void setIs_deletable(boolean is_deletable) {
        this.is_deletable = is_deletable;
    }
}
