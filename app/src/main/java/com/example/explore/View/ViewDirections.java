package com.example.explore.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.example.explore.Common;
import com.example.explore.DirectionsHelper.FetchURL;
import com.example.explore.DirectionsHelper.TaskLoadedCallback;
import com.example.explore.Helper.DirectionsJSONParser;
import com.example.explore.R;
import com.example.explore.Remote.IGoogleAPIService;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDirections extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    //pt a lua locatia curenta
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Location mLastLocation;
    Marker mCurrentMarker;


    Polyline polyline;
    IGoogleAPIService mService;
    //
    MarkerOptions place1, place2, orig, dest
            ,markerOptionsO, markerOptionsD;
    Polyline currentPolyline;
    //



    List<MarkerOptions> markerOptionsList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_directions);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mService = Common.getGoogleAPIServiceScalars();

        buildLocationRequest();
        buildLocationCallback();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        //place1 = new MarkerOptions().position(new LatLng(37.4220, -122.0840)).title("Loc1");

//        place2 = new MarkerOptions().position(new LatLng(37.43, -122.0840)).title("Loc2");

       // orig =new MarkerOptions().position((mLastLocation.getLatitude(), mLastLocation.getLongitude());
    /*    dest = new MarkerOptions().position( new MarkerOptions()
                .position(destinationLatLng)
                .title(Common.currentResult.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
      */

      //  String url = getUrl(place1.getPosition(),place2.getPosition(), "driving");
//          String url = getUrl(orig.getPosition(),dest.getPosition(), "driving");

//        new FetchURL(ViewDirections.this).execute(url, "driving");
       // place1 = new MarkerOptions().position(new LatLng(27.650143, 85.3199503)).title("Loc1");

    }
/*
    private String getUrl(LatLng origin, LatLng destination, String directionMode){
        String str_origin = "origin=" + origin.latitude+ "," + origin.longitude;
        String str_destination = "destination=" + destination.latitude+ "," + destination.longitude;
        String mode = "mode=" + directionMode;
        String parameter = str_origin + "&" + str_destination + "&" + mode;
        String format = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + format + "?"
                + parameter +"&key=AIzaSyB0Y8jpKrw__OxhGqNHgsKU3Sb7hXxgLDA";
        return url;
    }
*/

    private String getUrl(LatLng origin, LatLng dest, String drivingMode) {
        String str_origin = "origin=" + origin.latitude +","+origin.longitude;
        String str_dest = "destination=" + dest.latitude +"," + dest.longitude;
        String mode = "mode=" + drivingMode;
        String output = "json";
        String parameters = str_origin + "&" + str_dest +"&" + mode;
        //https://maps.googleapis.com/maps/api/directions/
        String url = "https://maps.googleapis.com/maps/api/directions/" + output +
                "?" + parameters + "&key=" + "AIzaSyB0UKQSsZmlTHIiE29Fxzipd_ylWU_ta7A";

        //AIzaSyCH7gw0dPSLV0E2ffd4zoREYg_54pr7bfo nu a dat crash
        Log.wtf("url", url);
        return url;
    }

    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLastLocation = locationResult.getLastLocation();

                markerOptionsO = new MarkerOptions()
                        .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()))
                        .title("Your position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrentMarker = mMap.addMarker(markerOptionsO);

                //move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                //create marker for destination
                LatLng destinationLatLng = new LatLng
                        (Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()),
                                Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));


                mCurrentMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(destinationLatLng)
                                .title(Common.currentResult.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                );

                markerOptionsD =  new MarkerOptions()
                        .position(destinationLatLng)
                        .title(Common.currentResult.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                //drawPath(mLastLocation,Common.currentResult.getGeometry().getLocation());
                String url = getUrl(markerOptionsO.getPosition(),markerOptionsD.getPosition(), "driving");

            //    new FetchURL(ViewDirections.this).execute(url, "driving");
                new FetchURL(ViewDirections.this).execute(getUrl(markerOptionsO.getPosition(),markerOptionsD.getPosition(),"driving"),"driving");
            }
        };
    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

     //   mMap.addMarker(place1);
      //  mMap.addMarker(place2);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLastLocation = location;

                MarkerOptions markerOptionsO = new MarkerOptions()
                        .position(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()))
                        .title("Your position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mCurrentMarker = mMap.addMarker(markerOptionsO);

                //move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));

                //create marker for destination
                LatLng destinationLatLng = new LatLng
                        (Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()),
                                Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));


                MarkerOptions markerOptionsD =
                        new MarkerOptions()
                                .position(destinationLatLng)
                                .title(Common.currentResult.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                ;
                mCurrentMarker = mMap.addMarker(markerOptionsD);
                //drawPath(mLastLocation,Common.currentResult.getGeometry().getLocation());

                new FetchURL(ViewDirections.this).execute(getUrl(markerOptionsO.getPosition(),markerOptionsD.getPosition(),"driving"),"driving");

            }
        });




    }

   private void drawPath(Location mLastLocation, com.example.explore.Model.Location location) {
        //clear all polyline
        if(polyline!=null){
            polyline.remove();
        }
        String origin = new StringBuilder(String.valueOf(mLastLocation.getLatitude()))
                .append("")
                .append(String.valueOf(mLastLocation.getLongitude()))
                .toString();
        String destination = new StringBuilder(location.getLat())
                .append("")
                .append(location.getLng())
                .toString();

        mService.getDirections(origin,destination)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        new ParserTask().execute(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline!= null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions)values[0]);
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {
        AlertDialog waitingDialog = new SpotsDialog(ViewDirections.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
     //       waitingDialog.show();
       //     waitingDialog.setMessage("Please Wait");
            Log.d("mylog", "withoutpolyine draw");

        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            //  PolylineOptions polylineOptions;
            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = lists.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                polylineOptions.addAll(points);
                polylineOptions.width(12);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);

            }

            if (polylineOptions != null) {

                polyline = mMap.addPolyline(polylineOptions);

                waitingDialog.dismiss();

            } else {
                Log.d("mylog", "withoutpolyine draw");
            }
        }
    }}
