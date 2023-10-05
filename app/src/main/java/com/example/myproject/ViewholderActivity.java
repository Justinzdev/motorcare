package com.example.myproject;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView; // เปลี่ยนเป็น BottomNavigationView แทน BottomNavigationItemView
import com.google.android.material.navigation.NavigationBarView;

public class ViewholderActivity extends AppCompatActivity {

    BottomNavigationView nav;

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
                        // Handle Home item click (replace with your logic)
                        Toast.makeText(ViewholderActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.notification:
                        // Handle Notification item click (replace with your logic)
                        Toast.makeText(ViewholderActivity.this, "Notification", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.profile:
                        // Handle Profile item click (replace with your logic)
                        Toast.makeText(ViewholderActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
