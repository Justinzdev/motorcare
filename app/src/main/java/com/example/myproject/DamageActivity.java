package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myproject.api.damageService;
import com.example.myproject.model.damageData;
import com.example.myproject.model.damageResponseData;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DamageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static damageService apiService;
    private List<String> base64Images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(damageService.class);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);
        int bp_id = getIntent().getIntExtra("bp_id", 0);

        if(bp_id != 0) {
            ImageButton imageButton = findViewById(R.id.imageButtonBack);
            Button addImageButton = findViewById(R.id.addImageButton);
            Button submitButton = findViewById(R.id.submitButton);
            EditText vehicleTypeEditText = findViewById(R.id.vehicleTypeEditText);
            EditText plateEditText = findViewById(R.id.plateEditText);
            EditText damageEditText = findViewById(R.id.damageEditText);
            EditText vehicleColorEditText = findViewById(R.id.vehicleColorEditText);

            // Get User Location (Lat, Lng)

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DamageActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });


            addImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }
            });

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vehicleType = vehicleTypeEditText.getText().toString();
                    String vehiclePlate = plateEditText.getText().toString();
                    String vehicleDamage = damageEditText.getText().toString();
                    String vehicleColor = vehicleColorEditText.getText().toString();
                    JsonArray jsonArray = new JsonArray();
                    for (String base64Image : base64Images) {
                        jsonArray.add(base64Image);
                    }

                    if(isValid(vehicleType, vehicleColor, vehicleDamage, vehiclePlate)) {
                        damageData damageDataRequest = new damageData();
                        damageDataRequest.damageAdd(bp_id, userId, vehicleType, vehicleColor, vehicleDamage, jsonArray, vehiclePlate);
                        Call<damageResponseData> call = apiService.addDamage(damageDataRequest);
                        call.enqueue(new retrofit2.Callback<damageResponseData>() {
                            @Override
                            public void onResponse(Call<damageResponseData> call, Response<damageResponseData> response) {
                                if (response.isSuccessful()) {
                                    damageResponseData responseData = response.body();
                                    if (responseData != null) {
                                        String msg = responseData.getMsg();
                                        Toast.makeText(DamageActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(DamageActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    String errorResponse = null;
                                    try {
                                        errorResponse = response.errorBody().string();
                                        Gson gson = new Gson();
                                        damageResponseData responseData = gson.fromJson(errorResponse, damageResponseData.class);

                                        if (responseData != null) {
                                            String msg = responseData.getMsg();
                                            Toast.makeText(DamageActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    } else {
                        Toast.makeText(DamageActivity.this, "Invalid Field", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            ImageView imageView = new ImageView(this);

            // Set the desired width and height (in pixels) for the ImageView
            int desiredWidth = 1000;
            int desiredHeight = 600;

            // Set margins (in pixels) between images
            int marginInPixels = 10;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(desiredWidth, desiredHeight);
            layoutParams.setMargins(marginInPixels, 0, marginInPixels, 0);
            imageView.setLayoutParams(layoutParams);

            Picasso.get().load(selectedImageUri).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                    base64Images.add(base64Image);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("ImageLoadError", "Error loading image: " + e.getMessage());
                }
            });

            // Add the ImageView to the horizontal container
            LinearLayout imageContainer = findViewById(R.id.imageContainer);
            imageContainer.addView(imageView);
        }
    }

    private boolean isValid(String vehicleType, String vehicleColor, String vehicleDamage, String vehiclePlate) {
        return !vehicleType.isEmpty() && !vehicleColor.isEmpty() && !vehicleDamage.isEmpty() && !vehiclePlate.isEmpty();
    }

}