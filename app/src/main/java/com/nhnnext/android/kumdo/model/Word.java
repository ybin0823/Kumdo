package com.nhnnext.android.kumdo.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Word {
    private final ArrayList<String> words;

    public Word(Context context, String part) {
        final int id = context.getResources().getIdentifier("dictionary_" + part,
                "array", context.getPackageName());
        words = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(id)));
    }

    public String loadWord() {
        Random random = new Random();
        int index = random.nextInt(words.size());

        return words.get(index);
    }
}