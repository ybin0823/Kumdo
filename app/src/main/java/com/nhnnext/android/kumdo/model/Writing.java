package com.nhnnext.android.kumdo.model;

public class Writing {
    private String name;
    private String email;
    private String sentence;
    private String words;
    private String imageUrl;
    private int category;
    private String date;

    public Writing(String name, String email, String sentence, String words, String imageUrl, int category, String date) {
        this.name = name;
        this.email = email;
        this.sentence = sentence;
        this.words = words;
        this.imageUrl = imageUrl;
        this.category = category;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSentence() {
        return sentence;
    }

    public String getWords() {
        return words;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Writing{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", sentence='" + sentence + '\'' +
                ", words='" + words + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + category +
                ", date='" + date + '\'' +
                '}';
    }
}