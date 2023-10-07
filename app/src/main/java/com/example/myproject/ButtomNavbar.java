package com.example.myproject;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ButtomNavbar {
    private AppCompatActivity activity;
    private BottomNavigationView nav;

    public ButtomNavbar(AppCompatActivity activity, BottomNavigationView nav) {
        this.activity = activity;
        this.nav = nav;
    }

    public void setupBottomNavigation() {
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Navigate(HomeActivity.class);
                        break;
                    case R.id.notification:
                        Navigate(NotificationActivity.class);
                        break;
                    case R.id.worklists:
                        Navigate(WorkingListsActivity.class);
                        break;
                    case R.id.profile:
                        Navigate(ProfileActivity.class);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void Navigate(Class<?> destinationClass) {
        Context context = activity.getApplicationContext();
        Intent intent = new Intent(context, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public void updateStoreMenuItemVisibility(boolean isVisible) {
        if (nav.getMenu().findItem(R.id.worklists) != null) {
            nav.getMenu().findItem(R.id.worklists).setVisible(isVisible);
        }
    }
}
