package com.example.admin.appbarbottom.model;

import android.graphics.Bitmap;

import com.example.admin.appbarbottom.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class GroupReceipt implements Serializable  {

    public static final int NONE = 0;
    public static final int COLOR_CHOSEN_1 = R.color.folder_icon_color_1;
    public static final int COLOR_CHOSEN_2 = R.color.folder_icon_color_2;
    public static final int COLOR_CHOSEN_3 = R.color.folder_icon_color_3;
    public static final int COLOR_CHOSEN_4 = R.color.folder_icon_color_4;

    public static final int TYPE_GROUP = 1000;
    public static final int TYPE_RECEIPT = 2000;

    private int type;

//    Common
    private String id;
    private String name;
    private float money_sum;

//    Grupa
    private int icon_color;
    private ArrayList<GroupReceipt> list_of_receipts;

//    Receipt
    long date_of_creation;
    long date_of_warranty_ending;
    ArrayList<RCTag> list_of_tags;
    ArrayList<String> images_as_base64;

    public ArrayList<Bitmap> getImages_as_bitmap() {
        return images_as_bitmap;
    }

    public void setImages_as_bitmap(ArrayList<Bitmap> images_as_bitmap) {
        this.images_as_bitmap = images_as_bitmap;
    }

    ArrayList<Bitmap> images_as_bitmap;

//    TODO temporary stuff for testing from anather app
//    public String imageUrl;

    private GroupReceipt(){
        this.id = UUID.randomUUID().toString();
    }

    public static GroupReceipt buildGroup(String name, float money_sum, int icon_color){
        GroupReceipt group = new GroupReceipt(name, money_sum, icon_color, 0, 0, null, null);
        group.setType(GroupReceipt.TYPE_GROUP);

        ArrayList<GroupReceipt> list_of_receipts = new ArrayList<>();
        group.setList_of_receipts(list_of_receipts);

        return group;
    }

    public static GroupReceipt buildReceipt(String name, float money_sum, long date_of_creation, long date_of_warranty_ending, ArrayList<String> images_as_base64, ArrayList<Bitmap> images_as_bitmap){
        GroupReceipt receipt = new GroupReceipt(name, money_sum, -1, date_of_creation, date_of_warranty_ending, images_as_base64, images_as_bitmap);
        receipt.setType(GroupReceipt.TYPE_RECEIPT);
        ArrayList<RCTag> list_of_tags = new ArrayList<>();
        receipt.setList_of_tags(list_of_tags);

        return receipt;
    }

    public GroupReceipt(String name, float money_sum, int icon_color,
                        long date_of_creation, long date_of_warranty_ending,
                        ArrayList<String> image_as_base64, ArrayList<Bitmap> images_as_bitmap) {
        super();

//        common
        this.name = name;
        this.money_sum = money_sum;

//        group
        this.icon_color = icon_color;
//        this.imageUrl = imageUrl;

//        receipt
        this.date_of_creation = date_of_creation;
        this.date_of_warranty_ending = date_of_warranty_ending;
        this.images_as_base64 = image_as_base64;
        this.images_as_bitmap = images_as_bitmap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon_color() {
        return icon_color;
    }

    public void setIcon_color(int icon_color) {
        this.icon_color = icon_color;
    }

    public ArrayList<GroupReceipt> getList_of_receipts() {
        return list_of_receipts;
    }

    public void setList_of_receipts(ArrayList<GroupReceipt> list_of_receipts) {
        this.list_of_receipts = list_of_receipts;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getMoney_sum() {
        return money_sum;
    }

    public void setMoney_sum(float money_sum) {
        this.money_sum = money_sum;
    }

    public long getDate_of_creation() {
        return date_of_creation;
    }

    public void setDate_of_creation(long date_of_creation) {
        this.date_of_creation = date_of_creation;
    }

    public long getDate_of_warranty_ending() {
        return date_of_warranty_ending;
    }

    public void setDate_of_warranty_ending(long date_of_warranty_ending) {
        this.date_of_warranty_ending = date_of_warranty_ending;
    }

    public ArrayList<RCTag> getList_of_tags() {
        return list_of_tags;
    }

    public void setList_of_tags(ArrayList<RCTag> list_of_tags) {
        this.list_of_tags = list_of_tags;
    }

    public ArrayList<String> getImages_as_base64() {
        return images_as_base64;
    }

    public void setImages_as_base64(ArrayList<String> images_as_base64) {
        this.images_as_base64 = images_as_base64;
    }

    public void addReceipt(GroupReceipt receipt) {
        list_of_receipts.add(receipt);
    }

    public void addTag(RCTag tag) {
        list_of_tags.add(tag);
    }

    @Override
    public String toString() {
        StringBuilder stringToReturn = new StringBuilder(type + id + name + money_sum + icon_color + list_of_receipts + date_of_creation + date_of_warranty_ending);

        if(getType() == TYPE_RECEIPT){
            for (int i = 0; i < list_of_tags.size(); i++){
                stringToReturn.append(list_of_tags.get(i).getName());
            }
        }


        return stringToReturn.toString();
    }

    public void addPhoto(Bitmap bitmap) {
        images_as_bitmap.add(bitmap);
    }
}
