package com.fryderykrott.receiptcarerfinal.services;

import android.util.Log;

import com.fryderykrott.receiptcarerfinal.model.Receipt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReceiptScanerDataExtrator {

    public static String[] shops = {"Lidl","Zabka", "Żabka", "Media Expert", "Mediaexpert", "HERT"};

    static boolean shopNameFlag;
    static boolean dateFlag;
    static boolean priceFlag;

    static String shopName;
    static String date = "";
    static float price;

    static Receipt extrahtDataFromReceipt(String result) {
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


        ArrayList<String> words_surrending_SUMA;
        int number_Of_surrending_words = 3;
        for (int i = 0; i < currentWords.length; i++) {
            currentWord = currentWords[i];

//            szukamy sklepu z aktualnej lini
            if (shopNameFlag) {
                findShopNameOf(currentWord);
            }
//            szukamy czy to to data
            else if (dateFlag) {
                findIfStringIsData(currentWord);
            }
//            szukamy czy kwota
            else if (priceFlag && i > number_Of_surrending_words && i < currentWords.length - number_Of_surrending_words) {
                words_surrending_SUMA = new ArrayList<>();
                for(int j = 1; j <= number_Of_surrending_words; j++){
                    words_surrending_SUMA.add(currentWords[i + j]);
                    words_surrending_SUMA.add(currentWords[i - j]);
                }

                findIfPriceIsThere(currentWord, words_surrending_SUMA);
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

        if(!date.equals(""))
            receipt.setDateOfCreation(date);

        receipt.setSumTotal(price);

        return receipt;
    }

    private static void findIfPriceIsThere(String currentWord, ArrayList<String> surrending_words) {
        String previous = surrending_words.get(0);
        String next = surrending_words.get(1);
        if(ReceiptScanerDataExtrator.isTherePriceTag(currentWord, previous, next )){
            ArrayList<Float> possible_numbers = new ArrayList<>(surrending_words.size());
            float number;
            for(int i = 0; i < surrending_words.size(); i++){
                number = ReceiptScanerDataExtrator.isItFloat(surrending_words.get(i));
                if(number != 0f)
                    possible_numbers.add(number);
            }

            if(possible_numbers.isEmpty())
                price = 0f;
            else
                price = largest(possible_numbers);

            priceFlag = false;
        }
    }

    static float largest(ArrayList<Float> arr)
    {
        float max = arr.get(0);

        for (int i = 1; i < arr.size(); i++)
            if (arr.get(i) > max)
                max = arr.get(i);

        return max;
    }

    private static void findIfStringIsData(String currentword) {
        if( ReceiptScanerDataExtrator.findIfStringIsDate(currentword)){
            date = currentword;
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

    static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static boolean findIfStringIsDate(String date){
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        if(!date.matches(regex))
            return false;

        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }

        return true;
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

        if(word.contains(suma_word) || word.contains(suma_word_2))
            return previous.contains(pln_word) || next.contains(pln_word);

//        double similatiry = similarityOfWords(word, suma_word);
//        if(similatiry >= 0.7)
//            return true;

        return false;
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
