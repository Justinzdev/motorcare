package com.example.myproject.model;

import com.google.gson.annotations.SerializedName;

public class UserDataResponse {
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

    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }
    public Integer getUser_ID() { return user_id; }

    public UserDataResponse(Integer user_id, String user_username, String user_password, String user_firstname, String user_lastname, String user_phone, String user_picture, String user_lat, String user_lng) {
        this.user_id = user_id;
        this.user_username = user_username;
        this.user_password = user_password;
        this.user_firstname = user_firstname;
        this.user_lastname = user_lastname;
        this.user_phone = user_phone;
        this.user_picture = user_picture;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
    }
}
