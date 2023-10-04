package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;
import android.widget.TextView;

import com.example.myproject.api.userService;
import com.example.myproject.model.UserDataResponse;
import com.example.myproject.model.userData;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class SignupActivity extends AppCompatActivity {
    Button SignupButton;
    EditText usernameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    TextView AHA;
    DatabaseHelper databaseHelper;

    private static userService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(userService.class);

        AHA = findViewById(R.id.login);
        SignupButton = findViewById(R.id.signUpBtn);
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.con_password);

        databaseHelper = new DatabaseHelper(this);
        SignupButton.setOnClickListener(view -> {

            String user = usernameEditText.getText().toString();
            String mail = emailEditText.getText().toString();
            String number_phone = phoneEditText.getText().toString();
            String pas = passwordEditText.getText().toString();
            String cPas = confirmPasswordEditText.getText().toString();

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pas) || TextUtils.isEmpty(cPas) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(number_phone))
                Toast.makeText(SignupActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else {
                if (pas.equals(cPas)) {
                    // เช็ค User
                    userData requestUserData = new userData(user, pas, number_phone, mail);
                    Call<UserDataResponse> call = apiService.registerUser(requestUserData);
                    call.enqueue(new Callback<UserDataResponse>() {
                        @Override
                        public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                            if (response.isSuccessful()) {
                                UserDataResponse userDataResponse = response.body();
                                if (userDataResponse != null) {
                                    String msg = userDataResponse.getMsg();
                                    Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
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
                                        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SignupActivity.this, "password does not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i); // เพิ่มบรรทัดนี้เพื่อเปิด LoginActivity
            }
        });
    }
}