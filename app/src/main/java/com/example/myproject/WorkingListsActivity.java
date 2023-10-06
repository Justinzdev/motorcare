package com.example.myproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myproject.api.damageService;
import com.example.myproject.model.MarkerInfo;
import com.example.myproject.model.UserDataResponse;
import com.example.myproject.model.WorkingLists;
import com.example.myproject.model.bikeRepairResponse;
import com.example.myproject.model.damageResponseData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myproject.api.bikeRepairService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkingListsActivity extends AppCompatActivity implements WorkingListsAdapter.OnButtonClickListener {

    private static bikeRepairService bikeRepairApiService;
    private static damageService damageApiSerivce;
    private ButtomNavbar bottomNavigationHandler;

    private ListView listView;
    private WorkingListsAdapter caseAdapter;
    private List<WorkingLists> caseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_lists);

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.worklists);
        ButtomNavbar navbar = new ButtomNavbar(this, nav);
        navbar.setupBottomNavigation();

        bottomNavigationHandler = new ButtomNavbar(this, nav);
        bottomNavigationHandler.setupBottomNavigation();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bikeRepairApiService = retrofit.create(bikeRepairService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Integer bp_id = sharedPreferences.getInt("bp_id", 0);

        listView = findViewById(R.id.listView);
        caseList = new ArrayList<>();
        caseAdapter = new WorkingListsAdapter(this, caseList);
        listView.setAdapter(caseAdapter);

        caseAdapter.setButtonClickListener(this);

        Call<damageResponseData> call = bikeRepairApiService.getJobs(bp_id);
        call.enqueue(new Callback<damageResponseData>() {
            @Override
            public void onResponse(Call<damageResponseData> call, Response<damageResponseData> response) {
                if (response.isSuccessful()) {
                    damageResponseData dataResponse = response.body();
                    if (dataResponse != null) {
                        JsonArray value = dataResponse.getValue();
                        for (JsonElement jobLists : value) {
                            if (jobLists.isJsonObject()) {
                                JsonObject jobObject = jobLists.getAsJsonObject();
                                Integer id = jobObject.get("id").getAsInt();
                                String title = jobObject.get("dm_brand").getAsString();
                                String description = jobObject.get("dm_details").getAsString();
                                String user_username = jobObject.get("user_username").getAsString();
                                String user_phone = jobObject.get("user_phone").getAsString();
                                String user_lat = jobObject.get("user_lat").getAsString();
                                String user_lng = jobObject.get("user_lng").getAsString();
                                String bp_lat = jobObject.get("bp_lat").getAsString();
                                String bp_lng = jobObject.get("bp_lng").getAsString();
                                WorkingLists workingList = new WorkingLists(id, bp_id, title, description, user_username, user_lat, user_lng, user_phone, bp_lat, bp_lng);
                                caseList.add(workingList);
                            }
                        }

                        caseAdapter.notifyDataSetChanged();
                    }
                } else {
                    String errorResponse = null;
                    try {
                        errorResponse = response.errorBody().string();
                        Gson gson = new Gson();
                        damageResponseData damageResponse = gson.fromJson(errorResponse, damageResponseData.class);

                        if (damageResponse != null) {
                            String msg = damageResponse.getMsg();
                            Toast.makeText(WorkingListsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<damageResponseData> call, Throwable t) {
                Log.e("API Request Failed", t.getMessage());
            }
        });
    }

    @Override
    public void onButtonClickConfirm(int position) {
        WorkingLists clickedItem = caseList.get(position);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        damageApiSerivce = retrofit.create(damageService.class);

        WorkingLists jobConfirmCase = caseList.get(position);
        String userLocation_Lat = jobConfirmCase.getLat();
        String userLocation_Lng = jobConfirmCase.getLng();
        String bikeRepairLocation_Lat = jobConfirmCase.getBPLat();
        String bikeRepairLocation_Lng = jobConfirmCase.getBPLng();

        Call<damageResponseData> call = damageApiSerivce.confirmDamageJob(clickedItem.getID(), clickedItem.getBPID());
        call.enqueue(new Callback<damageResponseData>() {
            @Override
            public void onResponse(Call<damageResponseData> call, Response<damageResponseData> response) {
                if (response.isSuccessful()) {
                    damageResponseData dataResponse = response.body();
                    if (dataResponse != null) {
                        String msg = dataResponse.getMsg();
                        Toast.makeText(WorkingListsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        caseList.remove(position);
                        caseAdapter.notifyDataSetChanged();

                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        Integer bp_id = sharedPreferences.getInt("bp_id", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("originLat", bikeRepairLocation_Lat);
                        editor.putString("originLng", bikeRepairLocation_Lng);
                        editor.putString("destLat", userLocation_Lat);
                        editor.putString("destLng", userLocation_Lng);
                        editor.putInt("bp_id_working", bp_id);
                        editor.apply();

                        Log.d("Debug", userLocation_Lat);
                        Log.d("Debug", userLocation_Lng);
                        Log.d("Debug", bikeRepairLocation_Lat);
                        Log.d("Debug", bikeRepairLocation_Lng);

                        Intent intent = new Intent(WorkingListsActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    String errorResponse = null;
                    try {
                        errorResponse = response.errorBody().string();
                        Gson gson = new Gson();
                        damageResponseData damageResponse = gson.fromJson(errorResponse, damageResponseData.class);

                        if (damageResponse != null) {
                            String msg = damageResponse.getMsg();
                            Toast.makeText(WorkingListsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<damageResponseData> call, Throwable t) {
                Log.e("API Request Failed", t.getMessage());
            }
        });
    }

    @Override
    public void onButtonClickCancel(int position) {
        WorkingLists clickedItem = caseList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยกเลิกรายการ");
        builder.setMessage("คุณต้องการที่จะยกเลิกรายงานนี้ใช่หรือไม่ ?");

        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(AppConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                damageApiSerivce = retrofit.create(damageService.class);

                Call<damageResponseData> call = damageApiSerivce.cancelDamageJob(clickedItem.getID(), clickedItem.getBPID());
                call.enqueue(new Callback<damageResponseData>() {
                    @Override
                    public void onResponse(Call<damageResponseData> call, Response<damageResponseData> response) {
                        if (response.isSuccessful()) {
                            damageResponseData dataResponse = response.body();
                            if (dataResponse != null) {
                                String msg = dataResponse.getMsg();
                                Toast.makeText(WorkingListsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                caseList.remove(position);
                                caseAdapter.notifyDataSetChanged();
                            }
                        } else {
                            String errorResponse = null;
                            try {
                                errorResponse = response.errorBody().string();
                                Gson gson = new Gson();
                                damageResponseData damageResponse = gson.fromJson(errorResponse, damageResponseData.class);

                                if (damageResponse != null) {
                                    String msg = damageResponse.getMsg();
                                    Toast.makeText(WorkingListsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<damageResponseData> call, Throwable t) {
                        Log.e("API Request Failed", t.getMessage());
                    }
                });
            }
        });

        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}