package com.example.myproject.model;

import com.google.gson.annotations.SerializedName;

public class userLocationData {
    @SerializedName("user_id")
    private Integer user_id;
    @SerializedName("user_lat")
    private String user_lat;
    @SerializedName("user_lng")
    private String user_lng;

    public userLocationData() {}

    public userLocationData(Integer user_id, String lat, String lng) {
        this.user_id = user_id;
        this.user_lat = lat;
        this.user_lng = lng;
    }
}
