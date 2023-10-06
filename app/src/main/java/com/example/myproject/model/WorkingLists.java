package com.example.myproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class WorkingLists {
    @SerializedName("id")
    private Integer id;
    @SerializedName("bp_id")
    private Integer bp_id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("date")
    private Date date;
    @SerializedName("status")
    private String status;

    @SerializedName("user_username")
    private String user_username;

    @SerializedName("user_lat")
    private String user_lat;

    @SerializedName("user_lng")
    private String user_lng;

    @SerializedName("user_phone")
    private String user_phone;

    @SerializedName("bp_lat")
    private String bp_lat;

    @SerializedName("bp_lng")
    private String bp_lng;

    public WorkingLists(Integer id, Integer bp_id, String title, String description, String user_username, String user_lat, String user_lng, String user_phone, String bp_lat, String bp_lng) {
        this.id = id;
        this.bp_id = bp_id;
        this.title = title;
        this.description = description;
        this.user_username = user_username;
        this.user_phone = user_phone;
        this.user_lat = user_lat;
        this.user_lng = user_lng;
        this.bp_lat = bp_lat;
        this.bp_lng = bp_lng;
    }

    public Integer getID () { return id; }

    public Integer getBPID () { return bp_id; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() { return user_username; }

    public String getPhone() { return user_phone; }
    public String getLat() { return user_lat; }
    public String getLng() { return user_lng; }
    public String getBPLat () { return bp_lat; }
    public String getBPLng () { return bp_lng; }
    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
