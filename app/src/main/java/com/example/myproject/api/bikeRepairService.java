package com.example.myproject.api;

import com.example.myproject.model.bikeRepairResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface bikeRepairService {
    @GET("/api/bikerepair/getstores")
    Call<bikeRepairResponse> getData ();
}
