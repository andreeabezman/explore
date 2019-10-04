package com.example.explore.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import com.example.explore.Common;
import com.example.explore.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.explore.Model.MyPlaces;
import com.example.explore.Model.Results;
import com.example.explore.Remote.IGoogleAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapSuggestions extends FragmentActivity implements OnMapReadyCallback
   {

    private static final int MY_PERMISSION_CODE = 1000;
    private GoogleMap mMap;
 //   private GoogleApiClient mGoogleApiClient;
    private double latitude, longitude;
    private Location mLastLocation;
    private Marker mMarker;
    private LocationRequest mLocationRequest;
    IGoogleAPIService mService;

    MyPlaces currentPlace;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    //init service

        mService = Common.getGoogleAPIService();
        //request runtime permission
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.action_hospital:
                        nearByPlace("park");
                        break;
                    case R.id.action_market:
                        nearByPlace("hotel");
                        break;
                    case R.id.action_school:
                        nearByPlace("museum");
                        break;
                    case R.id.action_restaurant:
                        nearByPlace("restaurant");
                        break;
                    default:
                        break;

                }
                return true;
            }
        });
        //init location
        buildLocationCallback();
        buildLocationRequest();

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());

    }
        @Override
      protected void onStop(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
      }
       private void buildLocationCallback() {

           mLocationRequest = new LocationRequest();
           mLocationRequest.setInterval(1000);
           mLocationRequest.setFastestInterval(1000);
           mLocationRequest.setSmallestDisplacement(10f);
           mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
       }
       private void buildLocationRequest() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();

                if (mMarker != null){
                    mMarker.remove();
                }
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                LatLng latLng = new LatLng(latitude,longitude);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Your position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMarker = mMap.addMarker(markerOptions);

                //move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));//11
            }
        };
       }
       private void nearByPlace(String placesType) {
        mMap.clear();
        String url = getUrl(latitude,longitude,placesType);
        mService.getNearByPlaces(url)
                .enqueue(new Callback<MyPlaces>() {
                    @Override
                    public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                        currentPlace = response.body();
                        //asigneaza valoare pentru locul curent
                        if(response.isSuccessful())
                        {
                            for (int i=0; i<response.body().getResults().length;i++)
                            {
                                MarkerOptions markerOptions = new MarkerOptions();
                                Results googlePlace = response.body().getResults()[i];
                                double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                                double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                                String placeName = googlePlace.getName();
                                String vicinity = googlePlace.getVicinity();
                                LatLng latLng = new LatLng(lat, lng);
                                markerOptions.position(latLng);
                                markerOptions.title(placeName);
                                String placeid = googlePlace.getId();
                                if (placeName.equals("park")) {
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                } else if(placeName.equals("restaurant")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant));
                                } else if (placeName.equals("museum")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_school));
                                } else if(placeName.equals("hotel"))
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_market));
//                                else {
                                // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.HU));
  //                              markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

  //                              }

                                markerOptions.snippet(String.valueOf(i));

                                mMap.addMarker((markerOptions));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyPlaces> call, Throwable t) {

                    }
                });
    }

    private String getUrl(double latitude, double longitude, String placesType) {

        StringBuilder googlePlacesUrl = new StringBuilder
                ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location="+latitude+","+longitude);
        googlePlacesUrl.append("&radius="+10000);
        googlePlacesUrl.append("&type="+placesType);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key="+"AIzaSyB0Y8jpKrw__OxhGqNHgsKU3Sb7hXxgLDA");
        Log.d("getUrl", googlePlacesUrl.toString());
        return googlePlacesUrl.toString();
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
                            mMap.setMyLocationEnabled(true);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    //Init googe play service

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {

                mMap.setMyLocationEnabled(true);
            }
        }
            else
                {

            mMap.setMyLocationEnabled(true);
            }
            //make event click on marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getSnippet() != null) {
                    //when user select marker, just get the resut of place and assign to static variable
                    Common.currentResult = currentPlace.getResults()[Integer.parseInt(marker.getSnippet())];
                    startActivity(new Intent(MapSuggestions.this, ViewPlace.class));
                }
                return true;
            }
        });
    }
}
