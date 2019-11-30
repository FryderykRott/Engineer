package com.fryderykrott.receiptcarerfinal.model;

import java.io.Serializable;

public class Tag implements Serializable {
    public int ID;
    public String name;

    public Tag(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
