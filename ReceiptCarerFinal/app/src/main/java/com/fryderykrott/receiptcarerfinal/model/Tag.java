package com.fryderykrott.receiptcarerfinal.model;

import java.io.Serializable;

public class Tag implements Serializable {
    public String uid;
    public String name;

    public Tag(){}

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
