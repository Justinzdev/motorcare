package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;
import android.widget.TextView;
public class SignupActivity extends AppCompatActivity {
    Button SignupButton;
    EditText usernameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    TextView AHA;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                    Integer checkUser = databaseHelper.checkUsername(user, mail, number_phone);

                    if(checkUser == 1) {
                        Toast.makeText(SignupActivity.this, "Username นี้ถูกใช้งานไปแล้ว", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(checkUser == 2) {
                        Toast.makeText(SignupActivity.this, "Email นี้ถูกใช้งานไปแล้ว", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(checkUser == 3) {
                        Toast.makeText(SignupActivity.this, "เบอร์โทรศัพท์ นี้ถูกใช้งานไปแล้ว", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean insert = databaseHelper.insertData(user, mail, number_phone, pas);
                    if (insert) {
                        Toast.makeText(SignupActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "registered failed", Toast.LENGTH_SHORT).show();
                    }
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