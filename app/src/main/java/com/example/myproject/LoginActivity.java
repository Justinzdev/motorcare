package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.api.userService;
import com.example.myproject.databinding.ActivityLoginBinding;
import com.example.myproject.model.UserDataResponse;
import com.example.myproject.model.userData;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button loginButton, arrowBackButton;
    EditText userEditText, passwordEditText;
//    DatabaseHelper databaseHelper;

    private static userService apiService;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(userService.class);

        //getSupportActionBar().hide();

        loginButton = findViewById(R.id.loginBtn);
        //arrowBackButton = findViewById(R.id.arrowBack);
        userEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

//        databaseHelper = new DatabaseHelper(this);
        loginButton.setOnClickListener(view -> {
                String userData = userEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isValid(userData, password)) {
                    userData requestUserData = new userData();
                    requestUserData.userDataLogin(userData, password);
                    Call<UserDataResponse> call = apiService.loginUser(requestUserData);
                    call.enqueue(new Callback<UserDataResponse>() {
                        @Override
                        public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                            if (response.isSuccessful()) {
                                UserDataResponse userDataResponse = response.body();
                                if (userDataResponse != null) {
                                    String msg = userDataResponse.getMsg();
                                    Integer user_id = userDataResponse.getUser_ID();

                                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("user_id", user_id);
                                    editor.apply();

                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                String errorResponse = null;
                                try {
                                    errorResponse = response.errorBody().string();
                                    Gson gson = new Gson();
                                    UserDataResponse userDataResponse = gson.fromJson(errorResponse, UserDataResponse.class);

                                    if (userDataResponse != null) {
                                        String msg = userDataResponse.getMsg();
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserDataResponse> call, Throwable t) {
                            Log.e("API Request Failed", t.getMessage());
                        }
                    });
                } else {
                    // ถ้าข้อมูลไม่ถูกต้อง ให้แสดงข้อความผิดพลาดหรือทำอย่างอื่นตามต้องการ
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        );

    }

    // ตรวจสอบความถูกต้องของข้อมูล (สามารถปรับเปลี่ยนตามความต้องการ)
    private boolean isValid(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }
}
