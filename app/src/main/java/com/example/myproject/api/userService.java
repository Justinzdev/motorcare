package com.example.myproject.api;
import com.example.myproject.model.UserDataResponse;
import com.example.myproject.model.userData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface userService {
//    @GET("/api/user/getuser/{user_username}/{user_email}/{user_phone}")
//    Call<UserDataResponse> getData (
//            @Path("user_username") String user_username,
//            @Path("user_email") String user_email,
//            @Path("user_phone") String user_phone
//    );
    @POST("/api/user/signup")
    Call<UserDataResponse> registerUser(@Body userData data);

    @POST("/api/user/signin")
    Call<UserDataResponse> loginUser(@Body userData data);
}
