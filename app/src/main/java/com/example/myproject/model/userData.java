package com.example.myproject.model;

import com.google.gson.annotations.SerializedName;

public class userData {
    @SerializedName("user_id")
    private Integer user_id;
    @SerializedName("user_username")
    private String user_username;
    @SerializedName("user_password")
    private String user_password;
    @SerializedName("user_firstname")
    private String user_firstname;
    @SerializedName("user_lastname")
    private String user_lastname;
    @SerializedName("user_phone")
    private String user_phone;
    @SerializedName("user_picture")
    private String user_picture;
    @SerializedName("user_lat")
    private String user_lat;
    @SerializedName("user_lng")
    private String user_lng;

    @SerializedName("user_email")
    private String user_email;

    public userData() {

    }

    public userData(String user_username, String user_password, String user_phone, String user_email) {
        this.user_username = user_username;
        this.user_password = user_password;
        this.user_phone = user_phone;
        this.user_email = user_email;
    }

    public void userDataLogin(String user_username, String user_password) {
        this.user_username = user_username;
        this.user_password = user_password;
    }
}
