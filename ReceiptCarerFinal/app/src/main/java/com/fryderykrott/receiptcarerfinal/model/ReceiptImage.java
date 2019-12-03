package com.fryderykrott.receiptcarerfinal.model;

import java.io.Serializable;

public class ReceiptImage implements Serializable {

    public String receiptID;
    public String base64Image;

    ReceiptImage(){

    }


    public ReceiptImage(String receiptID, String base64Image) {
        this.receiptID = receiptID;
        this.base64Image = base64Image;
    }

    public String getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(String receiptID) {
        receiptID = receiptID;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
