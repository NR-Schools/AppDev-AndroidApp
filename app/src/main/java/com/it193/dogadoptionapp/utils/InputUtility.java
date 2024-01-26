package com.it193.dogadoptionapp.utils;

public class InputUtility {

    public static boolean stringsAreNotNullOrEmpty(String... stringList) {
        for (String str : stringList) {
            if (str.toString().trim().isEmpty() || str == null)
                return false;
        }
        return true;
    }
}
