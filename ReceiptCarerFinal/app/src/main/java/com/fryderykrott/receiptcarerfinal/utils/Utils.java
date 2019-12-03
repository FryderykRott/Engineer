package com.fryderykrott.receiptcarerfinal.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.fryderykrott.receiptcarerfinal.model.Group;
import com.fryderykrott.receiptcarerfinal.model.Receipt;
import com.fryderykrott.receiptcarerfinal.model.Tag;
import com.fryderykrott.receiptcarerfinal.model.User;
import com.fryderykrott.receiptcarerfinal.receiptaddingUI.camerapreview.receiptsediting.ReceiptsAddingFragment;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static final String BASIC_GROUP_NAME = "Ogólne";
    public static final int INFINITE = 100000;
    public static final int NONE = -1;
    public static User user;

    public static String date_format = "yyyy-MM-dd";

    public static String bitmapToBase64(Bitmap bm){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
//String encodedImage = Base64.encode(b, Base64.DEFAULT);
        return Base64.getEncoder().encodeToString(b);
    }

    public static Bitmap base64ToBitmap(String encodedImage){

        byte[] decodedString = Base64.getDecoder().decode(encodedImage);
        return  BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context inContext, Bitmap bitmap) {
        Uri uri = getImageUri(inContext, bitmap);

        Cursor cursor = inContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String formatDateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(date_format, Locale.US);
        return dateFormat.format(date);
    }
    public static Date formatStringToDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat(date_format, Locale.US);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getTodaysDate(){
        return Calendar.getInstance().getTime();
    }
    //    pierwsza funkcja bedzie przyjmowała int w wartosic od 1-100 i zwracała tekst
    public static String convertWarrantyBarToString(int value){

        if (value == 0) {
            return "Brak gwarancji";
        }
        else if (value <= 1) {
            return "1 dzień";
        }
        else if(value <= 31){
            return value + " dni";
        }
        else if(value <= 70){
            int temp = (int) ((39) / 8);

            int temp_2 = (value - 31) / temp;

            if(temp_2 <= 1)
                return "1 tydzien";
            else
                return temp_2 + " tygodni";
        }
        else if(value < 100){
            int temp = (int) ((30) / 10);

            int temp_2 = (value - 70) / temp;

            if(temp_2 <= 1)
                return "1 rok";
            else
                return temp_2 + " lata";
        }else if(value == 100){
            return "Nieograniczona";
        }

        return "";
    }

    //    druga funkcja bedzie brać wartosc od 1-100 i zamieniać to na ilosć dni
    public static int convertWarrantyBarToDays(int value){
        if (value == 0) {
            return NONE;
        }
        else if (value <= 1) {
            return 1;
        }
        else if(value <= 31){
            return value;
        }
        else if(value <= 70){
            int temp = (int) ((39) / 8);

            int temp_2 = (value - 31) / temp;

            if(temp_2 <= 1)
                return 7;
            else
                return temp_2 * 7;
        }
        else if(value < 100){
            int temp = (int) ((30) / 10);

            int temp_2 = (value - 70) / temp;

            if(temp_2 <= 1)
                return 365;
            else
                return temp_2 * 365;
        }else if(value == 100){
            return INFINITE;
        }
        return 0;
    }

    //    trzecia funkcja będzie brać date i zwracać ile dni zostało jeszce do niej
    public static int convertDateToDays(Date date)
    {
//        String text = "";
//        Date currentTime = Calendar.getInstance().getTime();

        Date currentDate = new Date();

        long diff = date.getTime() - currentDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        int days = (int)(hours / 24);

        return days;
    }

    //    czwarta funcja będzie brać ilosc dni i zwracała date
    public static Date convertDaysToDate(int days){
        if(days == NONE)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Group findGroupByString(String basicGroupName) {
        ArrayList<Group> groups = user.getGroups();

        for (Group group: groups){
            if(group.getName().equals(basicGroupName))
                return group;
        }

        return null;
    }

    public static Group findGroupById(String UID){
        ArrayList<Group> groups = user.getGroups();

        for (Group group: groups){
            if(group.getGroupID().equals(UID))
                return group;
        }

        return null;
    }

    public static Tag findTagById(String UID){
        ArrayList<Tag> tags = user.getTags();

        for (Tag tag: tags){
            if(tag.getUid().equals(UID))
                return tag;
        }

        return null;
    }

    public static ArrayList<Receipt> finReceiptsOfGroup(Group group) {
        ArrayList<Receipt> receipts = new ArrayList<>();
        for(Receipt receipt: Utils.user.getReceipts()){
            if(group.getGroupID().equals(receipt.groupID))
                receipts.add(receipt);
        }
            return receipts;
    }
}
