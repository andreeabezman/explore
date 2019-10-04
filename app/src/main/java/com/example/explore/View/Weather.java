package com.example.explore.View;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.explore.Adapter.ForecastDaysAdapter;
import com.example.explore.Model.MyPlaces;
import com.example.explore.R;
import com.example.explore.Remote.IGoogleAPIService;
import com.example.explore.WeatherAPI;
import com.example.explore.WeatherModel.ForecastDay;
import com.example.explore.WeatherModel.ForecastResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.explore.WeatherAPI.BASE_URL;


public class Weather extends AppCompatActivity {

    private static final int MY_PERMISSION_CODE = 1000;
    private static final String CHANNEL_ID = "Location Changes Notification";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private Spinner mTimeSpinner;

    private List<ForecastDay> mForecasts = new ArrayList<>();

    private static final int RC_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CAMERA = 101;
    private static final int PERMISSION_REQUEST_LOCATION = 102;

    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;

    private double latitude, longitude;
    private Location mLastLocation;

    private LocationRequest mLocationRequest;

    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mRecyclerView = findViewById(R.id.rv_days);
        mToolbar = findViewById(R.id.toolbar_list);
        mTimeSpinner = findViewById(R.id.spinner_time);

        mTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Weather.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("day",i==0);
                editor.apply();
                setupRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");

        createNotificationChannel();
        loadWeatherData(null);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    //   showNotification(location);
                    loadWeatherData(location);
                }
            }

            ;
        };
    }

    private void showNotification(Location location) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.sun)
                .setContentTitle(getString(R.string.title_notification))
                .setContentText(getString(R.string.message_notification)+location.getLatitude()+", "
                        +location.getLongitude())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1001, builder.build());
    }

    private void setupRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ForecastDaysAdapter(mForecasts, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void loadWeatherData(Location location) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        Call<ForecastResponse> call = weatherAPI.getForecastsForLocation(
             //   location == null ? 44.4268 : location.getLatitude(),
               // location == null ? 26.1025 : location.getLongitude(),//Bucharest
                latitude,
                longitude,
                10,
                WeatherAPI.API_KEY);
        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful()) {
                    mForecasts = response.body().getForecastDays();
                    setupRecyclerView();
                } else {
                    System.out.println(response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    //dispatchTakePictureIntent();
                }
                break;
            }
            case PERMISSION_REQUEST_LOCATION:
                if (grantResults.length >1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    updateLocation();
                }
                break;

        }
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }else{
            updateLocation();
        }

    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationRequest request = new LocationRequest();
        request.setInterval(10 * 60 * 1000);
        request.setMaxWaitTime(60 * 60 * 1000);
      //  fusedLocationClient.requestLocationUpdates(request,
        //        locationCallback,
          //      null /* Looper */);
        buildLocationCallback();
        buildLocationRequest();

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient
                .requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Location";
            String description = "Displays a notification when the location has changed";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission
                .ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]
                                {
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                }
                        , MY_PERMISSION_CODE);
            }else
            {
                ActivityCompat.requestPermissions(this, new String[]
                                {
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                }
                        , MY_PERMISSION_CODE);
            }
            return false;
        }
        else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_CODE:
            {
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest
                            .permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                    //    mMap.setMyLocationEnabled(true);

                        //init location
                        buildLocationCallback();
                        buildLocationRequest();

                        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this);
                        fusedLocationProviderClient
                                .requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());
                    }
                }
            }
            break;
        }
    }

    private void buildLocationRequest() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();

          //      if (mMarker != null) {
            //        mMarker.remove();
              //  }
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                LatLng latLng = new LatLng(latitude, longitude);
        /*        MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Your position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMarker = mMap.addMarker(markerOptions);

                //move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        };
        */
            }


        };
    }
    private void buildLocationCallback() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        loadWeatherData(mLastLocation);

    }
}
