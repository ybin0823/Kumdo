package com.nhnnext.android.kumdo.model;

public class Writing {
    private String userEmail;
    private String text;
    private String words;
    private String imageUrl;

    public Writing(String userEmail, String text, String words, String imageUrl) {
        this.userEmail = userEmail;
        this.text = text;
        this.words = words;
        this.imageUrl = imageUrl;
    }

    public Writing(String userEmail, String text, String words) {
        this(userEmail, text, words, "");
    }

    public String getUser() {
        return userEmail;
    }

    public String getText() {
        return text;
    }

    public String getWords() {
        return words;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUser(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Writing{" +
                "userEmail=" + userEmail +
                ", text='" + text + '\'' +
                ", words=" + words +
                ", imageUrl=" + imageUrl +
                '}';
    }
}
