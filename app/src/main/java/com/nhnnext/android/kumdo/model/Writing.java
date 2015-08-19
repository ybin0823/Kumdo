package com.nhnnext.android.kumdo.model;

import java.util.Arrays;

public class Writing {
    private User user;
    private String text;
    private String[] words;

    public Writing(User user, String text, String[] words) {
        this.user = user;
        this.text = text;
        this.words = words;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String[] getWords() {
        return words;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "Writing{" +
                "user=" + user +
                ", text='" + text + '\'' +
                ", words=" + Arrays.toString(words) +
                '}';
    }
}
