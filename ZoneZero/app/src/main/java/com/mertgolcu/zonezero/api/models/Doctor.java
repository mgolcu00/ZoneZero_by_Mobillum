package com.mertgolcu.zonezero.api.models;

import com.google.gson.annotations.SerializedName;

public class Doctor {

    @SerializedName("full_name")
    private String full_name;
    @SerializedName("user_status")
    private String user_status;
    @SerializedName("gender")
    private String gender;
    @SerializedName("image")
    private Image image;

    public Doctor(String full_name, String user_status, String gender, Image image) {
        this.full_name = full_name;
        this.user_status = user_status;
        this.gender = gender;
        this.image = image;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
