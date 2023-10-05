package com.example.myproject.api;

import com.example.myproject.model.bikeRepairResponse;
import com.example.myproject.model.damageResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface bikeRepairService {
    @GET("/api/bikerepair/getstores")
    Call<bikeRepairResponse> getData ();

    @GET("/api/bikerepair/getjobs/{bp_id}")
    Call<damageResponseData> getJobs (
            @Path("bp_id") Integer bp_id
    );
}
