package com.example.myproject.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.Date;

public class bikeRepairResponse {
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
    private Time bp_open;

    @SerializedName("bp_close")
    private Time bp_close;

    @SerializedName("bp_state")
    private Integer bp_state;

    @SerializedName("bp_lat")
    private String bp_lat;

    @SerializedName("bp_lng")
    private String bp_lng;

    @SerializedName("bp_count_use")
    private String bp_count_use;

    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    @SerializedName("value")
    private JsonArray value;

    public JsonArray getValue() {
        return value;
    }

    public bikeRepairResponse () {}

    public bikeRepairResponse (Integer bp_id, Integer user_id, String bp_name, String bp_phone, String bp_details, Time bp_open, Time bp_close, Integer bp_state, String bp_lat, String bp_lng) {
        this.bp_id = bp_id;
        this.user_id = user_id;
        this.bp_name = bp_name;
        this.bp_phone = bp_phone;
        this.bp_details = bp_details;
        this.bp_open = bp_open;
        this.bp_close = bp_close;
        this.bp_state = bp_state;
        this.bp_lat = bp_lat;
        this.bp_lng = bp_lng;
    }
}
