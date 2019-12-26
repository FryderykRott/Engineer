package com.fryderykrott.receiptcarerfinal.utils;

import com.fryderykrott.receiptcarerfinal.chips.GroupChossingContainer;
import com.fryderykrott.receiptcarerfinal.Model.Group;

import java.util.ArrayList;

public class Validator {
    public static String massage = "";

    public static String getMassage() {
        return  massage;
    }

    public static boolean validateEmail(String emailString) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        massage = "";

        if(!emailString.matches(regex))
        {
            massage += "Nieprawidłowy format adresu email";
            return false;
        }

        return true;
    }

    public static boolean validateEditGroupName(String name, String orginal_name) {
        massage = "";
        if(name.isEmpty() ){
            massage += "To pole musi być wypełnione";
            return false;
        }

        if(name.length() >= 24 ){
            massage += "Nazwa nie może być dłuższa niż 24 znaki";
            return false;
        }

        if(isUniqGroup(name)){
            if(!name.equals(orginal_name)){
                return true;
            }else
                massage += "Grupa musi miec inną nazwę niż orginał";
        }
        else{
            if(name.equals(orginal_name)){
                return true;
            }else
                massage += "Istnieje już grupa o podanej nazwie";
        }


        return false;
    }

    public static boolean validateGroupName(String name) {
        massage = "";
        if(name.isEmpty() ){
            massage += "To pole musi być wypełnione";
            return false;
        }

        if(name.length() >= 24 ){
            massage += "Nazwa nie może być dłuższa niż 24 znaki";
            return false;
        }

        if(isUniqGroup(name)){
            return true;
        }

        massage += "Istnieje już grupa o podanej nazwie";
        return false;
    }

    private static boolean isUniqGroup(String name) {
        ArrayList<Group> groups = Utils.user.getGroups();

        for(int i = 0; i < groups.size(); i++){
            if(groups.get(i).getName().equals(name))
                return false;
        }

        return true;
    }

    public static boolean validatePassword(String passwordString){
        massage = "";
        if(passwordString.isEmpty() ){
            massage += "Trzeba wypełnić pole hasła";
            return false;
        }

        if(passwordString.length() < 8 ){
            massage += "\n Hasło musi mieć conajmniej 8 znaków";
            return false;
        }

        return true;
    }

    public static boolean validateReapetedPassword(String passwordString, String passwordReapedString) {
        massage = "";
        if(passwordString.isEmpty() || passwordReapedString.isEmpty()){
            massage += "Trzeba wypełnić pole hasła";
            return false;
        }

        if(passwordString.length() < 8 || passwordReapedString.length() < 8 ){
            massage += "\nHasło musi mieć conajmniej 8 znaków";
            return false;
        }

        if(!passwordString.equals(passwordReapedString)){
            massage += "\n Hasła muszą być takie same";
            return false;
        }

        return true;
    }

    public static boolean validateReceiptName(String receiptString){
        massage = "";
        if(receiptString.isEmpty() ){
            massage += "Trzeba wypełnić pole nazwy paragonu";
            return false;
        }
        return true;
    }


    public static boolean validateGroupChoosingChip(GroupChossingContainer groupChossingContainerChip) {
        return groupChossingContainerChip.getGroup() != null;
    }
}
