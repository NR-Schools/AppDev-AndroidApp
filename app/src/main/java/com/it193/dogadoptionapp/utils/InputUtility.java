package com.it193.dogadoptionapp.utils;

import java.util.List;

public class InputUtility {

    public static boolean stringsAreNotNullOrEmpty(String... stringList) {
        for (String str : stringList) {
            if (str.toString().trim().isEmpty() || str == null)
                return false;
        }
        return true;
    }

    public static int getIndexFromObject(Object needle, List<Object> haystack) {
        for (int i = 0; i < haystack.size(); i++) {
            if (needle.equals(haystack.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
