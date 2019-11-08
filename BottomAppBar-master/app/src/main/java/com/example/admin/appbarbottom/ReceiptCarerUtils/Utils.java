package com.example.admin.appbarbottom.ReceiptCarerUtils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.admin.appbarbottom.R;
import com.example.admin.appbarbottom.model.GroupReceipt;
import com.example.admin.appbarbottom.model.RCTag;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static final int NONE = 0;
    public static final int INFINITE = -1;
    public static ArrayList<GroupReceipt> global_Group_Set;
    public static ArrayList<GroupReceipt> global_Receipts_Set;
    public static ArrayList<RCTag> global_Tags_set;


    public static String date_format = "dd.MM.yyyy";

    public static ArrayList<GroupReceipt> generateGroupsAndReceipts(Context context) {
        ArrayList<GroupReceipt> groups = new ArrayList<>();
        global_Receipts_Set = new ArrayList<>();

        ArrayList<String> photos_as_base_64 = new ArrayList<>();
        photos_as_base_64.add("https://c2.staticflickr.com/4/3285/2819978026_175072995a_z.jpg?zz=1");

        GroupReceipt receipt_1 = GroupReceipt.buildReceipt("Zakupy nuda", 2.2f,12312L, 31L, photos_as_base_64, null);
        receipt_1.addTag(new RCTag("Media Ekspert"));
        receipt_1.addTag(new RCTag("Pierogi"));
        receipt_1.addTag(new RCTag("Masło"));
        receipt_1.addTag(new RCTag("Ksiażka"));
        receipt_1.addTag(new RCTag("Tanio"));

        GroupReceipt receipt_2 = GroupReceipt.buildReceipt("Recepta z Lidla", 2.2f,12312L, 31L, photos_as_base_64, null);
        receipt_2.addTag(new RCTag("Lidl"));
        receipt_2.addTag(new RCTag("Smietana"));
        receipt_2.addTag(new RCTag("Grześki"));
        receipt_2.addTag(new RCTag("panpersy"));
        receipt_2.addTag(new RCTag("Lody"));
        receipt_2.addTag(new RCTag("Tanio"));

        GroupReceipt receipt_3 = GroupReceipt.buildReceipt("Recepta z Castoramy", 2.2f,112L, 31L, photos_as_base_64, null);
        receipt_1.addTag(new RCTag("Castorama"));
        receipt_1.addTag(new RCTag("Pierogi"));
        receipt_1.addTag(new RCTag("Masło"));
        receipt_1.addTag(new RCTag("Ksiażka"));
        receipt_1.addTag(new RCTag("Tanio"));

        global_Receipts_Set.add(receipt_1);
        global_Receipts_Set.add(receipt_2);
        global_Receipts_Set.add(receipt_3);

        GroupReceipt group_1 = GroupReceipt.buildGroup( "Other", 14.23f, context.getColor(R.color.folder_icon_color_1));
        group_1.addReceipt(receipt_1);
        group_1.addReceipt(receipt_2);
        group_1.addReceipt(receipt_3);

        groups.add(group_1);

        GroupReceipt group_2 = GroupReceipt.buildGroup( "AGD", 1214.00f, context.getColor( R.color.folder_icon_color_2));
        group_2.addReceipt(receipt_1);
        group_2.addReceipt(receipt_2);
        group_2.addReceipt(receipt_3);
        groups.add(group_2);

        GroupReceipt group_3 = GroupReceipt.buildGroup( "Fun", 12.02f, context.getColor( R.color.folder_icon_color_3));
        group_3.addReceipt(receipt_1);
        group_3.addReceipt(receipt_2);
        group_3.addReceipt(receipt_3);
        groups.add(group_3);

        GroupReceipt group_4 = GroupReceipt.buildGroup( "Trip to Paris", 21412.02f, context.getColor( R.color.folder_icon_color_4));
        group_4.addReceipt(receipt_1);
        group_4.addReceipt(receipt_2);
        group_4.addReceipt(receipt_3);
        groups.add(group_4);

        global_Group_Set = groups;
        global_Tags_set = extractTags();
        return groups;
    }

//    public static ArrayList<GroupReceipt> generateGroup(Context context){
//        ArrayList<GroupReceipt> groups = new ArrayList<>();
//        ArrayList<GroupReceipt> groupsAndReceiptsMix = generateGroupsAndReceipts(null);
//
//        for(int i = 0; i < groupsAndReceiptsMix.size(); i++){
//            if(groupsAndReceiptsMix.get(i).getType() == GroupReceipt.TYPE_GROUP)
//                groups.add(groupsAndReceiptsMix.get(i));
//        }
//
//        return groups;
//    }

//    public static ArrayList<GroupReceipt> generateReceipts(Context context){
//        ArrayList<GroupReceipt> receipts = new ArrayList<>();
//        ArrayList<GroupReceipt> groupsAndReceiptsMix = generateGroupsAndReceipts(null);
//
//        for(int i = 0; i < groupsAndReceiptsMix.size(); i++){
//            if(groupsAndReceiptsMix.get(i).getType() == GroupReceipt.TYPE_RECEIPT)
//                receipts.add(groupsAndReceiptsMix.get(i));
//        }
//
//        return receipts;
//    }

    private static ArrayList<RCTag> extractTags() {
        ArrayList<RCTag> global_tags = new ArrayList<>(100);

        ArrayList<GroupReceipt> receipts;
        ArrayList<RCTag> tags;

        for(int i = 0; i < global_Group_Set.size(); i++){
            receipts = global_Group_Set.get(i).getList_of_receipts();
            for(int j = 0; j < receipts.size(); j++){
                tags = receipts.get(j).getList_of_tags();
                global_tags.addAll(tags);
            }
        }

        return global_tags;
    }

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
        SimpleDateFormat dateFormat = new SimpleDateFormat(date_format);
        return dateFormat.format(date);
    }
//    pierwsza funkcja bedzie przyjmowała int w wartosic od 1-100 i zwracała tekst
    public static String convertWarrantyBarToString(int value){

        if (value == 0) {
            return "No warranty";
        }
       else if (value <= 1) {
            return "1 day";
        }
        else if(value <= 31){
            return value + " days";
        }
        else if(value <= 70){
            int temp = (int) ((39) / 8);

            int temp_2 = (value - 31) / temp;

            if(temp_2 <= 1)
                return "1 week";
            else
                return temp_2 + " weeks";
        }
        else if(value < 100){
            int temp = (int) ((30) / 10);

            int temp_2 = (value - 70) / temp;

            if(temp_2 <= 1)
                return "1 year";
            else
                return temp_2 + " years";
        }else if(value == 100){
            return "Infinite warranty";
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

//    public static String convertWarrantyDateToString(java.util.Date warranty_date){
//        String text = "";
//        Date currentTime = Calendar.getInstance().getTime();
//
//        Date currentDate = new Date();
//
//        long diff = warranty_date.getTime() - currentDate.getTime();
//        long seconds = diff / 1000;
//        long minutes = seconds / 60;
//        long hours = minutes / 60;
//        long days = hours / 24;
//
////        wiec gdy mamy od 1-100 wartości więc trzeba je rozpraowadzić
////        1-31 to dni: 1 day left, 2 days left, 31 days left
////        32-60 to tygodnie: 1 week, 2 weeks, 4 weeks
//
//        if (days == 1) {
//            return "1 day left";
//        }
//        else if(days <= 31){
//
//        }
//
//        return text;
//    }
}
