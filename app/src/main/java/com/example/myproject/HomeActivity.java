package com.example.myproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myproject.api.bikeRepairService;
import com.example.myproject.api.userService;
import com.example.myproject.model.MarkerInfo;
import com.example.myproject.model.UserDataResponse;
import com.example.myproject.model.bikeRepairData;
import com.example.myproject.model.bikeRepairResponse;
import com.example.myproject.model.userData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends ViewholderActivity implements OnMapReadyCallback, HomeActivity_MarkerClick, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean allowClickMap = true;
    private static bikeRepairService apiService;

    private Map<Marker, MarkerInfo> markerToIndexMap = new HashMap<>();

    private ButtomNavbar bottomNavigationHandler;

    private static final long MIN_TIME_BETWEEN_UPDATES = 1000; // 1 seconds
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10.0f; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestLocationPermissions();


        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);
        nav.setSelectedItemId(R.id.home);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("user_role", "User");
        ButtomNavbar navbar = new ButtomNavbar(this, nav);
        if ("Store".equals(userRole)) {
            navbar.updateStoreMenuItemVisibility(true);
        } else {
            navbar.updateStoreMenuItemVisibility(false);
        }
        navbar.setupBottomNavigation();

        bottomNavigationHandler = new ButtomNavbar(this, nav);
        bottomNavigationHandler.setupBottomNavigation();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(bikeRepairService.class);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeMap();
        }
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            }
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.MyMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
            mMap.setOnMarkerClickListener(this);

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            Integer user_id = sharedPreferences.getInt("user_id", 0);
            String userRole = sharedPreferences.getString("user_role", "User");

            if (!"Store".equals(userRole)) {
                float hue = 120;
                BitmapDescriptor userLocationIcon = BitmapDescriptorFactory.defaultMarker(hue);

                MyLocationListener locationListener = new MyLocationListener(mMap, userLocationIcon, user_id);
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BETWEEN_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListener
                );
            } else {
                requestDirections();
            }

            Call<bikeRepairResponse> call = apiService.getData();
            call.enqueue(new Callback<bikeRepairResponse>() {
                @Override
                public void onResponse(Call<bikeRepairResponse> call, Response<bikeRepairResponse> response) {
                    if (response.isSuccessful()) {
                        bikeRepairResponse bikeResponse = response.body();
                        if (bikeResponse != null) {
                            JsonArray value = bikeResponse.getValue();
                            if (value != null) {
                                List<LatLng> locationList = new ArrayList<>();

                                for (JsonElement locationElement : value) {
                                    if (locationElement.isJsonObject()) {
                                        JsonObject locationObject = locationElement.getAsJsonObject();

                                        double bp_lat = locationObject.get("bp_lat").getAsDouble();
                                        double bp_lng = locationObject.get("bp_lng").getAsDouble();

                                        LatLng location = new LatLng(bp_lat, bp_lng);

                                        locationList.add(location);

                                        Marker marker = mMap.addMarker(new MarkerOptions()
                                                .position(location)
                                                .title(locationObject.get("bp_name").getAsString())
                                                .snippet("เบอร์โทรศัพท์: " + locationObject.get("bp_phone").getAsString() +
                                                        "\nเวลาทำการ: " + locationObject.get("bp_open").getAsString() +
                                                        " - " + locationObject.get("bp_close").getAsString()));

                                        MarkerInfo markerInfo = new MarkerInfo(locationObject.get("bp_count_use").getAsInt(), locationObject.get("bp_id").getAsInt());
                                        markerToIndexMap.put(marker, markerInfo);
                                    }
                                }

                                if (!locationList.isEmpty()) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationList.get(0)));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                }
                            }
                        }
                    } else {
                        String errorResponse = null;
                        try {
                            errorResponse = response.errorBody().string();
                            Gson gson = new Gson();
                            UserDataResponse userDataResponse = gson.fromJson(errorResponse, UserDataResponse.class);

                            if (userDataResponse != null) {
                                String msg = userDataResponse.getMsg();
                                Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<bikeRepairResponse> call, Throwable t) {
                    Log.e("API Request Failed", t.getMessage());
                }
            });
        }
    }

    public void requestDirections() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Integer bp_id = sharedPreferences.getInt("bp_id", 0);
        String originLatRaw = sharedPreferences.getString("originLat", "0.00");
        String originLngRaw = sharedPreferences.getString("originLng", "0.00");
        String destLatRaw = sharedPreferences.getString("destLat", "0.00");
        String destLngRaw = sharedPreferences.getString("destLng", "0.00");
        Integer bp_id_working = sharedPreferences.getInt("bp_id_working", 0);

        double originLat = Double.parseDouble(originLatRaw);
        double originLng = Double.parseDouble(originLngRaw);
        double destLat = Double.parseDouble(destLatRaw);
        double destLng = Double.parseDouble(destLngRaw);

        if(originLat != 0.00 && originLng != 0.00 && destLat != 0.00 && destLng != 0.00 && bp_id_working == bp_id) {
            com.google.maps.model.LatLng destinationLatLng = new com.google.maps.model.LatLng(destLat, destLng);

            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(AppConfig.GoogleAPI)
                    .build();

            DirectionsApi.newRequest(context)
                    .origin(new com.google.maps.model.LatLng(originLat, originLng))
                    .destination(destinationLatLng)
                    .mode(TravelMode.DRIVING)
                    .setCallback(new PendingResult.Callback<DirectionsResult>() {
                        @Override
                        public void onResult(DirectionsResult result) {
                            if (result != null && result.routes.length > 0) {
                                List<LatLng> points = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());

                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .addAll(points)
                                        .width(20)
                                        .color(Color.RED);

                                runOnUiThread(() -> {
                                    mMap.addPolyline(polylineOptions);

                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(destLat, destLng))
                                            .title("Destination")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng point : points) {
                                        builder.include(point);
                                    }
                                    LatLngBounds bounds = builder.build();

                                    int padding = 100;
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                    mMap.animateCamera(cameraUpdate);

                                });
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            // Handle failure
                        }
                    });
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
        if(allowClickMap) {
            MarkerInfo markerInfo = markerToIndexMap.get(marker);
            if (markerInfo != null) {
                Integer bp_count_use = markerInfo.getBPCountUse();
                Integer bp_id = markerInfo.getBPID();
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

                buttonProfileShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Debug", "Click Store Profile");
                        onCloseShopInfomation();
                        replaceFragment(StoreProfileFragment.newInstance(marker.getTitle(), marker.getSnippet(), bp_count_use));
                        setAllowClickMap(false);
                    }
                });

                buttonUseService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCloseShopInfomation();
                        Intent intent = new Intent(HomeActivity.this, DamageActivity.class);
                        intent.putExtra("bp_id", bp_id);
                        startActivity(intent);
                    }
                });
            }
        }
        return true;
    }

    public void setAllowClickMap(boolean allow) {
        allowClickMap = allow;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
        Log.d("Debug", "Open Fragment");
    }
}
