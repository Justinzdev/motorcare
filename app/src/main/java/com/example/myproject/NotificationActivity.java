package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myproject.api.notificationService;
import com.example.myproject.model.NotificationData;
import com.example.myproject.model.NotificationLists;
import com.example.myproject.model.WorkingLists;
import com.example.myproject.model.damageResponseData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends AppCompatActivity {
    private static notificationService notificationAPIService;
    private ListView listView;
    private NotificationAdapter caseAdapter;
    private List<NotificationLists> caseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.notification);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", "User");
        ButtomNavbar navbar = new ButtomNavbar(this, nav);
        if ("Store".equals(userRole)) {
            navbar.updateStoreMenuItemVisibility(true);
        } else {
            navbar.updateStoreMenuItemVisibility(false);
        }
        navbar.setupBottomNavigation();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        notificationAPIService = retrofit.create(notificationService.class);

        listView = findViewById(R.id.listViewNotification);
        caseList = new ArrayList<>();
        caseAdapter = new NotificationAdapter(this, caseList);
        listView.setAdapter(caseAdapter);

        int userId = sharedPreferences.getInt("user_id", 0);
        if(userId != 0) {
            Call<NotificationData> call = notificationAPIService.getNotification(userId);
            call.enqueue(new Callback<NotificationData>() {
                @Override
                public void onResponse(Call<NotificationData> call, Response<NotificationData> response) {
                    if (response.isSuccessful()) {
                        NotificationData dataResponse = response.body();
                        if (dataResponse != null) {
                            JsonArray value = dataResponse.getValue();
                            for (JsonElement notiLists : value) {
                                if (notiLists.isJsonObject()) {
                                    JsonObject notiObject = notiLists.getAsJsonObject();
                                    Integer id = notiObject.get("id").getAsInt();
                                    Integer user_id = notiObject.get("user_id").getAsInt();
                                    String title = notiObject.get("noti_title").getAsString();
                                    String description = notiObject.get("noti_description").getAsString();

                                    NotificationLists notificationListsData = new NotificationLists(id, user_id, title, description);
                                    caseList.add(notificationListsData);
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
                                Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationData> call, Throwable t) {
                    Log.e("API Request Failed", t.getMessage());
                }
            });
        }
    }
}