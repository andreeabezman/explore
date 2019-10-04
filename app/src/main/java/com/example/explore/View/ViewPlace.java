package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explore.APIClient;
import com.example.explore.ApiInterface;
import com.example.explore.Common;
import com.example.explore.Favorite;
import com.example.explore.R;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import com.example.explore.Model.PlaceDetail;
import com.example.explore.Remote.IGoogleAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewPlace extends AppCompatActivity {

    ImageView photo;
    RatingBar ratingBar;
    TextView opening_hours, place_address, place_name;
    Button btnViewOnMap, btnShowDirection, btnFav;

    //dupa ce am creat metoda in IGoogleapiservice pt retragerea detaliilor declaram un ob pt poze si detalii
    IGoogleAPIService mService;
    PlaceDetail mPlace;
    int currentUserId;
    private String currentPlaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);

        mService = Common.getGoogleAPIService();
        photo = (ImageView)findViewById(R.id.photo);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        place_address = (TextView)findViewById(R.id.place_address);
        place_name = (TextView) findViewById(R.id.place_name);
        opening_hours = (TextView)findViewById(R.id.place_open_hour);

        btnShowDirection = (Button) findViewById(R.id.btn_show_direction);

        place_name.setText("");
        place_address.setText("");
        opening_hours.setText("");

        btnFav = findViewById(R.id.btn_add_fav);
        btnShowDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(ViewPlace.this, ViewDirections.class);
                startActivity(mapIntent);
            }
        });
        if(Common.currentResult.getPhotos() !=null && Common.currentResult.getPhotos().length>0)
        {
            Picasso.with(this)
                    .load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000))
                    //Deoarece getPhotos este array, vom lua primul item
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .error(R.drawable.ic_photo_black_24dp)
                    .into(photo);
        }
        if (Common.currentResult.getRating()!=null && !TextUtils.isEmpty(Common.currentResult.getRating()))
        {
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }
        else
        {
            ratingBar.setVisibility(View.GONE);
        }
        //Opening hours
        if (Common.currentResult.getOpening_hours()!=null)
        {
            opening_hours.setText("Open now : "+Common.currentResult.getOpening_hours().getOpen_now());
        }
        else
        {
            opening_hours.setVisibility(View.GONE);
        }

        //User Service to fetch Address and Name
        btnFav.setEnabled(false);
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        mPlace = response.body();
                        if (mPlace == null){
                            return;
                        }
                        place_address.setText(mPlace.getResult().getFormatted_address());
                        place_name.setText(mPlace.getResult().getName());
                        currentPlaceId = mPlace.getResult().getPlace_id();
                        Log.wtf("placeid", "value "+mPlace.getResult().getPlace_id());
                        callFavoriteCheck(currentPlaceId, currentUserId);
                        btnFav.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {
                        t.printStackTrace();
                        place_address.setText("nu a mers");
                        place_name.setText("nu a mers");
                        btnFav.setEnabled(true);
                    }
                });

        SharedPreferences sharedPreferences = getSharedPreferences("explore", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userid", 0);

        // textul butonului aici
        // apelezi loadfav dupstai ce ai setat user id

        btnFav = findViewById(R.id.btn_add_fav);

        btnFav.setEnabled(false);
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnFav.getText().toString()== "Remove favorite"){
                    Favorite favorite = new Favorite();
                    favorite.setPlaceid(currentPlaceId);
                    favorite.setUserid(currentUserId);
                    callRemoveFavorite(favorite);
                    btnFav.setEnabled(false);
                    //  btnFav.setText("Add favorite");
                }else if(btnFav.getText().toString() =="Add favorite") {
                    Favorite favorite = new Favorite();
                    favorite.setPlaceid(currentPlaceId);
                    favorite.setUserid(currentUserId);
                    callAddFavorite(favorite);
                    btnFav.setEnabled(false);
                    //    btnFav.setText("Remove favorite");
                }

            }
        });
    }

    private void callFavoriteCheck(String cplaceid, int userid){
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Boolean> call = apiInterface.checkfav(cplaceid, userid);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null && response.body()){
                    btnFav.setText("Remove favorite");
                    btnFav.setEnabled(true);
                }else{
                    btnFav.setText("Add favorite");
                    btnFav.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                btnFav.setText("Add favorite");
                //  Toast.makeText(ViewPlace.this, "Failed to load place", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void callAddFavorite(Favorite favorite){
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Favorite> call = apiInterface.insertFavorite(favorite);
        call.enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                btnFav.setText("Remove favorite");
                btnFav.setEnabled(true);

                Toast.makeText(ViewPlace.this, "Place Added to Favorites", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {
                btnFav.setEnabled(true);

                Toast.makeText(ViewPlace.this, "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void callRemoveFavorite(Favorite favorite){
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Void> call = apiInterface.deleteFavorite(favorite.getPlaceid(), favorite.getUserid());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnFav.setText("Add favorite");
                Toast.makeText(ViewPlace.this, "Place Removed from Favorites", Toast.LENGTH_SHORT).show();
                btnFav.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ViewPlace.this, "Failed to remove place from Favorites", Toast.LENGTH_SHORT).show();
                btnFav.setText("Remove favorite");
                btnFav.setEnabled(true);

            }
        });

    }

    private String getPlaceDetailUrl(String place_id) {
       /* StringBuilder url = new StringBuilder("https://maps.googleapis.com/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key="+"AIzaSyB0Y8jpKrw__OxhGqNHgsKU3Sb7hXxgLDA");
        return url.toString();
        */
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id
                +"&key=AIzaSyB0Y8jpKrw__OxhGqNHgsKU3Sb7hXxgLDA";
    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+maxWidth+"&photoreference="+ photo_reference +
                "&key=AIzaSyB0Y8jpKrw__OxhGqNHgsKU3Sb7hXxgLDA";
    }
}
