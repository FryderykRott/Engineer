package com.fryderykrott.receiptcarerfinal.Model;

import com.fryderykrott.receiptcarerfinal.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String docRefUser;
    public String uid;

    public ArrayList<Tag> tags;
    public ArrayList<Receipt> receipts;
    public ArrayList<Group> groups;

    public User(){
        groups = new ArrayList<>();
        tags = new ArrayList<>();
        receipts = new ArrayList<>();

    }

    public User(String uid) {
        this.uid = uid;

        groups = new ArrayList<>();
        tags = new ArrayList<>();
        receipts = new ArrayList<>();

    }

    public String getDocRefUser() {
        return docRefUser;
    }

    public void setDocRefUser(String docRefUser) {
        this.docRefUser = docRefUser;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(ArrayList<Receipt> receipts) {
        this.receipts = receipts;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

}
