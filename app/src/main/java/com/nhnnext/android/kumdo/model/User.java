package com.nhnnext.android.kumdo.model;

public class User {
    private String email;
    private String nickname;
    private String enc_id;
    private String profile_image;
    private String age;
    private String gender;
    private String id;
    private String name;
    private String birthday;

    public User(String email, String nickname, String enc_id, String profile_image, String age,
                String gender, String id, String name, String birthday) {
        this.email = email;
        this.nickname = nickname;
        this.enc_id = enc_id;
        this.profile_image = profile_image;
        this.age = age;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEnc_id() {
        return enc_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEnc_id(String enc_id) {
        this.enc_id = enc_id;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", enc_id='" + enc_id + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
