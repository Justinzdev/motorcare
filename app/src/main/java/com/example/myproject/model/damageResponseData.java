package com.example.myproject.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

public class damageResponseData {
    @SerializedName("id")
    private Integer id;

    @SerializedName("bp_id")
    private Integer bp_id;

    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("dm_brand")
    private String dm_brand;

    @SerializedName("dm_color")
    private String dm_subbrand;

    @SerializedName("dm_details")
    private String dm_detilas;

    @SerializedName("dm_picture")
    private JSONArray dm_picture;

    @SerializedName("dm_vehicle")
    private String dm_vehicle;

    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public damageResponseData () {}


}
