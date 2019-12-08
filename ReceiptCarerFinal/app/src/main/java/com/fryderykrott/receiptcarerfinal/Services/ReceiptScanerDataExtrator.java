package com.fryderykrott.receiptcarerfinal.Services;

import android.graphics.Point;
import android.util.Log;

import com.fryderykrott.receiptcarerfinal.Model.Receipt;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReceiptScanerDataExtrator {

    public static String[] shops = {"Lidl","Zabka", "Żabka", "Media Expert", "Mediaexpert", "HERT", "Kaufland", "Biedronka", "Rossmann"};

    public static String[] formats = {"yyyy-MM-dd","dd-MM-yyyy"};
    public static String[] regexes = {"^\\d{4}-\\d{2}-\\d{2}$", "^\\d{2}-\\d{2}-\\d{4}$"};

    static boolean shopNameFlag;
    static boolean dateFlag;
    static boolean priceFlag;

    static String shopName;
    static String date = "";
    static float price = 0f;

    static DateFormat sdf;

    static Receipt extrahtDataFromReceipt(String result, List<FirebaseVisionText.TextBlock> result_blocks) {
        Receipt receipt = new Receipt();

        shopNameFlag = true;
        dateFlag = true;
        priceFlag = true;

        String delimiter1 = " ";
        String delimiter2 = "\n";
        result = result.replaceAll(delimiter2, delimiter1);

        String[] currentWords = result.split(delimiter1);
        String previousWord;
        String currentWord;
        String nextWord;

        ArrayList<FirebaseVisionText.TextBlock> words_surrending_SUMA = new ArrayList<>();
        int number_Of_surrending_words = 3;

        FirebaseVisionText.TextBlock block;
        for (int i = 0; i < result_blocks.size(); i++) {
            block = result_blocks.get(i);

            if (block.getText().toLowerCase().contains("pln")) {

                for (int j = 1; j <= number_Of_surrending_words; j++) {
                    words_surrending_SUMA.add(result_blocks.get(i + j));
                    words_surrending_SUMA.add(result_blocks.get(i - j));
                }

                findIfPriceIsThere(block, words_surrending_SUMA);
                break;
            }

        }

        for (int i = 0; i < currentWords.length; i++) {
            currentWord = currentWords[i];

            if(!shopNameFlag && !dateFlag)
                break;

//            szukamy sklepu z aktualnej lini
            if (shopNameFlag && i < 20) {
                findShopNameOf(currentWord);
            }
//            szukamy czy to to data

            if (dateFlag) {
                findIfStringIsData(currentWord);
            }
        }

        Log.i("shopName", shopName + "");
        Log.i("date", date + "");
        Log.i("price", String.valueOf(price));
//      Stworzenie paragonu

        if(!shopName.equals("")){
            receipt.setName("Paragon ze sklepu " + shopName);
            receipt.addTag(shopName);
        }
        else
            receipt.setName("Paragon");

        if(!date.equals(""))
            receipt.setDateOfCreation(date);

        receipt.setSumTotal(price);

        return receipt;
    }

    private static void findIfPriceIsThere(FirebaseVisionText.TextBlock currentWord, ArrayList<FirebaseVisionText.TextBlock> surrending_words) {
        float point = currentWord.getCornerPoints()[0].y;
        String currentWordString = currentWord.getText();

        String delimiter1 = " ";
        String delimiter2 = "\n";
        currentWordString = currentWordString.replaceAll(delimiter2, delimiter1);

        String[] words = currentWordString.split(delimiter1);
        String word;
        for(int i = 0; i < words.length; i++){
            word = words[i];
            price = isItFloat(word);
            if(price != 0){
                return;
            }
        }

        float number;
        for(int i = 0; i < surrending_words.size(); i++){
            number = surrending_words.get(i).getCornerPoints()[0].y;

            if( Math.abs(point - number) <= 50){
                price = isItFloat(surrending_words.get(i).getText());
                priceFlag = false;
                break;
            }

        }
//        ArrayList<Float> possible_numbers = new ArrayList<>(surrending_words.size());
//        float number;
//        for(int i = 0; i < surrending_words.size(); i++){
//            number = ReceiptScanerDataExtrator.isItFloat(surrending_words.get(i));
//            if(number != 0f)
//                possible_numbers.add(number);
//        }

    }

    private static void findIfStringIsData(String currentword) {
        if( findIfStringIsDateOfFormat(currentword)){
            dateFlag = false;
        }

    }

    private static void findShopNameOf(String currentLine) {
        shopName = ReceiptScanerDataExtrator.findRecegnizableShopName(currentLine);
        if(!shopName.equals("")){
            shopNameFlag = false;
        }

    }

    public static String findRecegnizableShopName(String suppousedShop){
        suppousedShop = suppousedShop.toLowerCase();

        double similatiry;
        for(String shopName: shops){
            if(suppousedShop.contains(shopName.toLowerCase())) {
                return shopName;
            }

            else similatiry = similarityOfWords(suppousedShop, shopName.toLowerCase());
            if(similatiry >= 0.7) {
                return shopName;
            }
        }

        return "";
    }

    public static double similarityOfWords(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
    /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static boolean findIfStringIsDateOfFormat(String date){

        for(int i = 0; i < formats.length; i++){
            if(!date.matches(regexes[i]))
                continue;

            sdf = new SimpleDateFormat(formats[i]);
            try {
                Date dateDat = sdf.parse(date);
                DateFormat targetFormat = new SimpleDateFormat(formats[0]);

                ReceiptScanerDataExtrator.date = targetFormat.format(dateDat);  // 20120821
                return true;

            } catch (ParseException e) {
                return false;
            }
        }

        return false;
    }

    private static String convertDate(String strDate, String origalPatern)
    {
        //for strdate = 2017 July 25

        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern(origalPatern)
                .toFormatter();

        LocalDate parsedDate = LocalDate.parse(strDate, f);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern(formats[0]);

        String newDate = parsedDate.format(f2);

        return newDate;
    }

    public static boolean isTherePriceTag(String word, String previous, String next){
//        1. sprawidzc czy zawiera to słowo
//        2. sprawdzic czy dystans jest na poziomie 70
        word = word.toLowerCase();
        previous = previous.toLowerCase();
        next = next.toLowerCase();

        String suma_word = "suma";
        String suma_word_2 = "suma:";
        String pln_word = "pln";

        return word.contains(pln_word);
//
//        if(word.contains(suma_word) || word.contains(suma_word_2))
//            return previous.contains(pln_word) || next.contains(pln_word);

//        double similatiry = similarityOfWords(word, suma_word);
//        if(similatiry >= 0.7)
//            return true;

//        return false;
    }

    public static float isItFloat(String word) {
        String delimiter1 = ".";
        String delimiter2 = ",";
        word = word.replaceAll(delimiter2, delimiter1);

        String regex = "^\\d+,\\d{2}$";
        String regex2 = "([0-9]*[.])[0-9]{2}";
        if(!word.matches(regex2))
            return 0f;

        float result;
        try {
            result = Float.parseFloat(word);
        } catch ( NumberFormatException e) {
            return 0f;
        }

        return result;
    }
}
