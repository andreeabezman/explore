package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.explore.APIClient;
import com.example.explore.ApiInterface;
import com.example.explore.Favorite;
import com.example.explore.Profile;
import com.example.explore.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {


    private LinearLayout clContainer;
    private ImageView searchPlaces;
    private ImageView nearbyPlaces;
    private ImageView profile;
    private ImageView posts;
    private ImageView favoriteplaces;
    private ImageView savedplaces;
    private ImageView weather;
    private ImageView settings;
    private ImageView logout;
    String currentUsername;
    int currentUserId;
    private List<Favorite> favoriteList = new ArrayList<>();
    private int favIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("explore", MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");
        SharedPreferences sharedPref = getSharedPreferences("explore", MODE_PRIVATE);
        currentUserId = sharedPref.getInt("userid",0);

        clContainer = findViewById(R.id.clContainer);

        TextView tv =  findViewById(R.id.hellohome);
        tv.setText("Hello "+ currentUsername);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        tv.setTypeface(face);
        searchPlaces = (ImageView) findViewById(R.id.search_places);
        searchPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToMapSearch = new Intent(HomeActivity.this, MapSearch.class);
                startActivity(moveToMapSearch);
            }
        });
        nearbyPlaces = (ImageView) findViewById(R.id.nearbyplaces);
        nearbyPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToMapSuggestions = new Intent(HomeActivity.this, MapSuggestions.class);
                startActivity(moveToMapSuggestions);
            }
        });
        profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToProfile = new Intent(HomeActivity.this, Profile.class);
                startActivity(moveToProfile);
            }
        });
        posts = (ImageView) findViewById(R.id.news);
        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToNews = new Intent(HomeActivity.this, PostsActivity.class);
                startActivity(moveToNews);
            }
        });
        favoriteplaces = (ImageView) findViewById(R.id.favoriteplaces);
        favoriteplaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callFavorite();

              //  Intent moveToFav = new Intent(HomeActivity.this, FavoriteActivity.class);
              //  startActivity(moveToFav);
            }
        });

        weather = (ImageView) findViewById(R.id.weather);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToWeather = new Intent(HomeActivity.this, Weather.class);
                startActivity(moveToWeather);
            }
        });
        settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSettings = new Intent(HomeActivity.this, Settings.class);
                startActivity(moveToSettings);
            }
        });
        logout = (ImageView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToLogin = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(moveToLogin);
            }
        });

    }
    private void callFavorite() {
        APIClient apiClient = new APIClient();
        Retrofit retrofit = apiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Favorite>> call = apiInterface.getFavoriteUser(currentUserId);
        call.enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    favoriteList = response.body();
                    favIndex = 0;
                    if (favoriteList.size() > 0){
                        Intent moveToNoF = new Intent(HomeActivity.this, FavoriteActivity.class);
                        startActivity(moveToNoF);

                        //        getPlaceDetail(favoriteList.get(favIndex).getPlaceid());
                        //   textv.setText("View your favorite places!");
                    }else if (favoriteList.size() == 0){

                        Intent moveToNoF = new Intent(HomeActivity.this, NoFavoritePlaces.class);
                        startActivity(moveToNoF);

                    }

                    // setupRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
