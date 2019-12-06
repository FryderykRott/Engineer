package com.fryderykrott.receiptcarerfinal.Services;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;

public class ReceiptScaner implements OnSuccessListener<FirebaseVisionText>, OnFailureListener {
   Activity a;
   ArrayList<Bitmap> receiptsImages;
   OnSuccessListener<FirebaseVisionText>successListener;
   OnFailureListener failureListener;

   int receiptsRemained;
   ArrayList<String> results;

    public ReceiptScaner(Activity a, ArrayList<Bitmap> receiptsImages, OnSuccessListener<FirebaseVisionText> successListener, OnFailureListener failureListener) {
        this.a = a;
        this.receiptsImages = receiptsImages;
        this.successListener = successListener;
        this.failureListener = failureListener;
    }

    OnCompleteScanningListener onCompleteListener;

    public void proccesImages(OnCompleteScanningListener onCompleteListener){
        results = new ArrayList<>();
        receiptsRemained = receiptsImages.size();

        this.onCompleteListener = onCompleteListener;

        runTextRecognition(receiptsImages.get(0));

//        runOtherTextRecognition(receiptsImages.get(0));
    }

    private void runOtherTextRecognition(Bitmap image){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(a.getApplicationContext()).build();


        if(!textRecognizer.isOperational()){
            Log.i("skanowanie", "nie udalo sie");
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(image).build();

            SparseArray<TextBlock> items = textRecognizer.detect(frame);

            StringBuilder sb = new StringBuilder();

            for(int i = 0; i<items.size(); ++i)
            {

                TextBlock myItem = items.valueAt(i);

                sb.append((myItem.getValue()));
                sb.append("\n");
            }

            String result = sb.toString();
            Log.i("skanowanie", result);

        }
    }

    private void runTextRecognition(Bitmap mSelectedImage) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSelectedImage);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        recognizer.processImage(image)
                .addOnSuccessListener(this)
                .addOnFailureListener(this);
    }

    @Override
    public void onSuccess(FirebaseVisionText firebaseVisionText) {

        String result =  firebaseVisionText.getText();
        results.add(result);

        Log.i("skanowanie", firebaseVisionText.getText());

        receiptsRemained--;
        if(receiptsRemained == 0){
            extrahtDataFromReceipts();
            return;
        }

        Bitmap currentReceipt = receiptsImages.get(receiptsImages.size() -receiptsRemained);
        runTextRecognition(currentReceipt);
    }

    private void extrahtDataFromReceipts() {
        ArrayList<Receipt> receipts = new ArrayList<>(results.size());
        for(String result: results){
            receipts.add(ReceiptScanerDataExtrator.extrahtDataFromReceipt(result));
        }

        onCompleteListener.onCompleteScanningListener(receipts);
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        Log.i("skanowanie","NIE UDALO SIE ZESKANOWAC< PENWIE NIE MA CZEGO");
    }

    public interface OnCompleteScanningListener{
        public void onCompleteScanningListener(ArrayList<Receipt> results);
    }
}
