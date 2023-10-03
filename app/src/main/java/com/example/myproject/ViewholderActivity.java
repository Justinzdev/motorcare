package com.example.myproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView; // เปลี่ยนเป็น BottomNavigationView แทน BottomNavigationItemView
import com.google.android.material.navigation.NavigationBarView;

public class ViewholderActivity extends AppCompatActivity {

    BottomNavigationView nav; // เปลี่ยนเป็น BottomNavigationView แทน BottomNavigationItemView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewholder);

        nav = findViewById(R.id.bottomNavigationView);

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(ViewholderActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notification:
                        Toast.makeText(ViewholderActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        Toast.makeText(ViewholderActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        break;
//                    case R.id.setting:
//                        Toast.makeText(ViewholderActivity.this, "Setting", Toast.LENGTH_SHORT).show();
//                        break;
                    default:
                }
                return true;
            }
        });
    }
}
