package com.example.expense_tracker;

import java.util.Locale;
import java.util.Random;

public class Randomstring {
    private final String Letter = "abcdefghijklmnopqrstuvwxyz";
    private final String Number = "0123456789";
    private final char[] AlphaNumeric=(Letter+Letter.toUpperCase() + Number).toCharArray();

    public String generateAlphaNumeric(int length){
        StringBuilder result =  new StringBuilder();
        for(int i=0;i<length; i++){
            result.append(AlphaNumeric[new Random().nextInt(AlphaNumeric.length)]);
        }
        return result.toString();
    }
}
