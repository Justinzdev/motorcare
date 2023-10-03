package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    Button loginButton, arrowBackButton;
    EditText userEditText, passwordEditText;
    DatabaseHelper databaseHelper;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getSupportActionBar().hide();

        loginButton = findViewById(R.id.loginBtn);
        //arrowBackButton = findViewById(R.id.arrowBack);
        userEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        databaseHelper = new DatabaseHelper(this);
        loginButton.setOnClickListener(view -> {
                String userData = userEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (isValid(userData, password)) {
                    boolean result = databaseHelper.checkHasUsernameLogin(userData, password);
                    // ถ้าข้อมูลถูกต้อง คุณสามารถดำเนินการ Login ตามต้องการ
                    if(result) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // ตัวอย่าง: เปลี่ยนหน้าหลัง Login
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Not found user", Toast.LENGTH_SHORT).show();
                    }
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
