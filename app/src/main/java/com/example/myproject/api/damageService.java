package com.example.myproject.api;

import com.example.myproject.model.damageData;
import com.example.myproject.model.damageResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface damageService {
    @POST("/api/damage/add")
    Call<damageResponseData> addDamage(@Body damageData data);
}
