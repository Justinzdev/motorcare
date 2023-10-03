package com.example.myproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, HomeActivity_MarkerClick, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, proceed with map initialization
            initializeMap();  //บรรทัดที่36
        }
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with map initialization
                initializeMap();
            } else {
                // Permission denied, handle it or show a message to the user
            }
        }
    }

    // Initialize the map
    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.MyMap);
        mapFragment.getMapAsync(this); //บรรทัดที่58
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permission again
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, enable the "My Location" button on the map
            mMap.setMyLocationEnabled(true);

            mMap.setOnMarkerClickListener(this);


            LatLng[] locations = new LatLng[]{
                    new LatLng(7.000322437764077, 100.49198694532629),
                    new LatLng(7.000504133861651, 100.49419373279413),
                    new LatLng(6.996795655918778, 100.4916382581544),
                    new LatLng(7.012590425888277, 100.48535798428674),
                    new LatLng(6.99818733693373, 100.47974534338373),
                    new LatLng(7.014167743739746, 100.49243889754723),
                    new LatLng(7.019960514056645, 100.49750827271585),
                    // เพิ่มตำแหน่งอื่นๆ ที่ต้องการ
            };

//            for (LatLng location : locations) {
//                mMap.addMarker(new MarkerOptions().position(locations[0]).title("ร้านซ่อมมอเตอร์ไซค์พี่บีมอ."));
//                mMap.addMarker(new MarkerOptions().position(locations[1]).title("ร้านช่างตี้มอเตอร์ไซค์"));
//                mMap.addMarker(new MarkerOptions().position(locations[2]).title("ร้านช่างจู๊ด"));
//                mMap.addMarker(new MarkerOptions().position(locations[3]).title("ช่างเทน เซอร์วิสหาดใหญ่"));
//                mMap.addMarker(new MarkerOptions().position(locations[4]).title("ช่างเดี่ยวเซอร์วิส"));
//                mMap.addMarker(new MarkerOptions().position(locations[5]).title("เออีโอ มอเตอร์สปอร์ต"));
//                mMap.addMarker(new MarkerOptions().position(locations[6]).title("Pop Up Shop"));
//            }

            for (int i = 0; i < locations.length; i++) {
                LatLng location = locations[i];
                String title = "ร้านซ่อมมอเตอร์ไซค์พี่บีมอ.";
                String timeOpen = "";
                switch (i) {
                    case 0:
                        title = "ร้านซ่อมมอเตอร์ไซค์พี่บีมอ.";
                        timeOpen = "9:00 AM - 6:00 PM";
                        break;
                    case 1:
                        title = "ร้านช่างตี้มอเตอร์ไซค์";
                        timeOpen = "8:30 AM - 5:30 PM";
                        break;
                    case 2:
                        title = "ร้านช่างจู๊ด";
                        timeOpen = "10:00 AM - 7:00 PM";
                        break;
                    case 3:
                        title = "ช่างเทน เซอร์วิสหาดใหญ่";
                        timeOpen = "10:00 AM - 7:00 PM";
                        break;
                    case 4:
                        title = "ช่างเดี่ยวเซอร์วิส";
                        timeOpen = "9:00 AM - 6:00 PM";
                        break;
                    case 5:
                        title = "เออีโอ มอเตอร์สปอร์ต";
                        timeOpen = "9:00 AM - 6:00 PM";
                        break;
                    case 6:
                        title = "Pop Up Shop";
                        timeOpen = "9:00 AM - 6:00 PM";
                        break;
                }

                mMap.addMarker(new MarkerOptions().position(location).title(title).snippet("เวลาเปิด: " + timeOpen));
            }


            if (locations.length > 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(locations[0]));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            }
        }
    }

    public void onCloseShopInfomation () {
        View Shop_View = findViewById(R.id.Shop_View);
        TextView shopName = findViewById(R.id.shopName);
        TextView shopTimeOpen = findViewById(R.id.timeOpen);
        Button buttonProfileShop = findViewById(R.id.buttonProfileShop);
        Button buttonUseService = findViewById(R.id.buttonUseService);
        Button closeButton = findViewById(R.id.closeButton);

        shopName.setText("");
        shopTimeOpen.setText("");
        Shop_View.setVisibility(View.INVISIBLE);
        shopName.setVisibility(View.INVISIBLE);
        shopTimeOpen.setVisibility(View.INVISIBLE);
        buttonProfileShop.setVisibility(View.INVISIBLE);
        buttonUseService.setVisibility(View.INVISIBLE);
        closeButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        View Shop_View = findViewById(R.id.Shop_View);
        TextView shopName = findViewById(R.id.shopName);
        TextView shopTimeOpen = findViewById(R.id.timeOpen);
        Button buttonProfileShop = findViewById(R.id.buttonProfileShop);
        Button buttonUseService = findViewById(R.id.buttonUseService);
        Button closeButton = findViewById(R.id.closeButton);

        shopName.setText(marker.getTitle());
        shopTimeOpen.setText(marker.getSnippet());
        Shop_View.setVisibility(View.VISIBLE);
        shopName.setVisibility(View.VISIBLE);
        shopTimeOpen.setVisibility(View.VISIBLE);
        buttonProfileShop.setVisibility(View.VISIBLE);
        buttonUseService.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.VISIBLE);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                onCloseShopInfomation();
            }
        });

//        Toast.makeText(HomeActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
