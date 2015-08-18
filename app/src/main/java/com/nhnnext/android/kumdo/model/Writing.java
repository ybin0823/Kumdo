package com.nhnnext.android.kumdo.model;

import java.util.Arrays;

public class Writing {
    private User user;
    private String text;
    private Object[] words;

    public Writing(User user, String text, Object[] words) {
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

    public Object[] getWords() {
        return words;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWords(Object[] words) {
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
