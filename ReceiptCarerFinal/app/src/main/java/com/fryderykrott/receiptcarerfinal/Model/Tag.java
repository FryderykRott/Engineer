package com.fryderykrott.receiptcarerfinal.Model;

import java.io.Serializable;
import java.util.UUID;

public class Tag implements Serializable {
    public String uid;
    public String name;

    public Tag(){
        uid = UUID.randomUUID().toString();
    }

    public Tag(String tagName) {
        uid = UUID.randomUUID().toString();
        name = tagName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
