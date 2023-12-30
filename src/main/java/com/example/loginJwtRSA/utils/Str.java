package com.example.loginJwtRSA.utils;

public class Str {
    public static int findThIndex(String phrase, String needle, int th) {
        int index = -1;
        for (int ii = 0; ii < th; ++ii) {
            int newIndex = phrase.indexOf(needle, index + 1);
            if (newIndex < 0) {
                return newIndex;
            }
            index = newIndex;
        }

        return index;
    }
}
