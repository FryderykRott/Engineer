package com.example.admin.appbarbottom.model;

public class RCTag {

    int id;
    String name;

    public RCTag(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


//    statycznie do znajdowania tag w firebasie
}
