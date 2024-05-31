package com.example.myandroidproject.helpers;

public class StringHelper {

    public static boolean regexEmailValidationPattern(String email) {
        String regex = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})";
//        String regex = "@\"^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$\"";

        if (email.matches(regex)) {
            return true;
        }
        return false;
    }
}
