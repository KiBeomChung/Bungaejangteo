package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexPhonNum(String target) {
        String regex = "^01([0|1|6|7|8|9])([0-9]{4})([0-9]{4})$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean isRegexLangType(String target) {
        String regex = "^[0-9a-zA-Zㄱ-ㅎ가-힣]*$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

}

