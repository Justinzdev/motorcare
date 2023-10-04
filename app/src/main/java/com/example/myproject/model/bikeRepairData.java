package com.example.myproject.model;

import com.google.gson.annotations.SerializedName;

public class bikeRepairData {
    @SerializedName("bp_id")
    private Integer bp_id;

    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("bp_name")
    private String bp_name;

    @SerializedName("bp_phone")
    private String bp_phone;

    @SerializedName("bp_details")
    private String bp_details;

    @SerializedName("bp_open")
    private String bp_open;

    @SerializedName("bp_close")
    private String bp_close;

    @SerializedName("bp_state")
    private Integer bp_state;

    @SerializedName("bp_lat")
    private String bp_lat;

    @SerializedName("bp_lng")
    private String bp_lng;

    @SerializedName("bp_count_use")
    private String bp_count_use;

    public bikeRepairData () { }
}
