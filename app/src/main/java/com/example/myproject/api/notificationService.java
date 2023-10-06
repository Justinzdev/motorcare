package com.example.myproject.api;

import com.example.myproject.model.NotificationData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface notificationService {
    @GET("/api/notification/{user_id}")
    Call<NotificationData> getNotification(
            @Path("user_id") Integer user_id
    );
}
