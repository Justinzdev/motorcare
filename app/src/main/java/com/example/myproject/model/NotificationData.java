package com.example.myproject.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class NotificationData {
    @SerializedName("id")
    private Integer id;

    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("noti_title")
    private String noti_title;

    @SerializedName("noti_description")
    private String noti_description;

    @SerializedName("value")
    private JsonArray value;

    @SerializedName("msg")
    private String msg;

    public NotificationData (Integer id, Integer user_id, String title, String description) {
        this.id = id;
        this.user_id = user_id;
        this.noti_title = title;
        this.noti_description = description;
    }

    public String getMsg() { return msg; }

    public JsonArray getValue() { return value; }

    public Integer getID () { return id; }
    public Integer getUserID () { return user_id; }
    public String getTitle () { return noti_title; }
    public String getDescription () { return noti_description; }
}
