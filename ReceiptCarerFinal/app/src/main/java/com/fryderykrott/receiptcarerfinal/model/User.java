package com.fryderykrott.receiptcarerfinal.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String docRefUser;

    String uid;
    ArrayList<Group> groups;
    ArrayList<Tag> tags;

    public User(){ }

    public User(String uid) {
        this.uid = uid;
        groups = new ArrayList<>();

        tags = new ArrayList<>();
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDocRefUser() {
        return docRefUser;
    }

    public void setDocRefUser(String docRefUser) {
        this.docRefUser = docRefUser;
    }
}
