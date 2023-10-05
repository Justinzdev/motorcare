package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

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

    private static bikeRepairService apiService;
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

        apiService = retrofit.create(bikeRepairService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Integer bp_id = sharedPreferences.getInt("bp_id", 0);

        listView = findViewById(R.id.listView);
        caseList = new ArrayList<>();
        caseAdapter = new WorkingListsAdapter(this, caseList);
        listView.setAdapter(caseAdapter);

        caseAdapter.setButtonClickListener(this);

        Call<damageResponseData> call = apiService.getJobs(bp_id);
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
                                String title = jobObject.get("dm_brand").getAsString();
                                String description = jobObject.get("dm_details").getAsString();
                                WorkingLists workingList = new WorkingLists(title, description);
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
        
    }

    @Override
    public void onButtonClickCancel(int position) {

    }
}