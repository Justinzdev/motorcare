package com.example.myproject.api;

import com.example.myproject.model.damageData;
import com.example.myproject.model.damageResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface damageService {
    @POST("/api/damage/add")
    Call<damageResponseData> addDamage(@Body damageData data);

    @PATCH("/api/damage/confirm/{dm_id}/{bp_id}")
    Call<damageResponseData> confirmDamageJob (
        @Path("dm_id") Integer dm_id,
        @Path("bp_id") Integer bp_id
    );
    @PATCH("/api/damage/cancel/{dm_id}/{bp_id}")
    Call<damageResponseData> cancelDamageJob (
            @Path("dm_id") Integer dm_id,
            @Path("bp_id") Integer bp_id
    );

    @PATCH("/api/damage/finish/{dm_id}/{bp_id}")
    Call<damageResponseData> finishDamageJob (
            @Path("dm_id") Integer dm_id,
            @Path("bp_id") Integer bp_id
    );
}