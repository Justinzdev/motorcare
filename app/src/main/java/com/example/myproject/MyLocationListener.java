package com.example.myproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myproject.api.bikeRepairService;
import com.example.myproject.api.userService;
import com.example.myproject.model.UserDataResponse;
import com.example.myproject.model.userData;
import com.example.myproject.model.userLocationData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyLocationListener implements LocationListener {
    private GoogleMap googleMap;
    private BitmapDescriptor userLocationIcon;
    private Marker userLocationMarker;

    private Integer user_id;

    private static userService apiService;

    public MyLocationListener(GoogleMap googleMap, BitmapDescriptor userLocationIcon, Integer user_id) {
        this.googleMap = googleMap;
        this.userLocationIcon = userLocationIcon;
        this.user_id = user_id;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (googleMap != null) {
            LatLng updatedLocation = new LatLng(location.getLatitude(), location.getLongitude());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(userService.class);

            userLocationData requestUserData = new userLocationData(user_id, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            Call<userLocationData> call = apiService.updateLocation(requestUserData);
            call.enqueue(new Callback<userLocationData>() {
                @Override
                public void onResponse(Call<userLocationData> call, Response<userLocationData> response) {
                    if (response.isSuccessful()) {
                        userLocationData userDataResponse = response.body();
                        if (userDataResponse != null) {}
                    } else {
                        String errorResponse = null;
                        try {
                            errorResponse = response.errorBody().string();
                            Gson gson = new Gson();
                            UserDataResponse userDataResponse = gson.fromJson(errorResponse, UserDataResponse.class);

                            if (userDataResponse != null) {}
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<userLocationData> call, Throwable t) {
                    Log.e("API Request Failed", t.getMessage());
                }
            });

            if (userLocationMarker != null) {
                userLocationMarker.setPosition(updatedLocation);
            } else {
                userLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(updatedLocation)
                        .icon(userLocationIcon)
                        .title("Your Location"));
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLng(updatedLocation));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
