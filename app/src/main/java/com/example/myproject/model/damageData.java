package com.example.myproject.model;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

public class damageData {
    @SerializedName("id")
    private Integer id;

    @SerializedName("bp_id")
    private Integer bp_id;

    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("dm_brand")
    private String dm_brand;

    @SerializedName("dm_color")
    private String dm_color;

    @SerializedName("dm_details")
    private String dm_detilas;

    @SerializedName("dm_picture")
    private JsonArray dm_picture;

    @SerializedName("dm_vehicle")
    private String dm_vehicle;

    @SerializedName("dm_status")
    private Integer dm_status;

    public damageData () {}

    public void damageAdd (Integer bp_id, Integer user_id, String dm_brand, String dm_color, String dm_detilas, JsonArray dm_picture, String dm_vehicle) {
        this.bp_id = bp_id;
        this.user_id = user_id;
        this.dm_brand = dm_brand;
        this.dm_color = dm_color;
        this.dm_detilas = dm_detilas;
        this.dm_picture = dm_picture;
        this.dm_vehicle = dm_vehicle;
    }
}
