package com.nhnnext.android.kumdo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Writing implements Parcelable {
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

    public Writing(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.sentence = in.readString();
        this.words = in.readString();
        this.imageUrl = in.readString();
        this.category = in.readInt();
        this.date = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(sentence);
        dest.writeString(words);
        dest.writeString(imageUrl);
        dest.writeInt(category);
        dest.writeString(date);
    }

    public static final Parcelable.Creator<Writing> CREATOR = new Parcelable.Creator<Writing>() {
        public Writing createFromParcel(Parcel in) {
            return new Writing(in);
        }

        @Override
        public Writing[] newArray(int size) {
            return new Writing[size];
        }
    };
}