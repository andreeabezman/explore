package com.example.explore.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.explore.APIClient;
import com.example.explore.Adapter.FavoriteAdapter;
import com.example.explore.ApiInterface;
import com.example.explore.Common;
import com.example.explore.Favorite;
import com.example.explore.Model.PlaceDetail;
import com.example.explore.ProgressBarAnimation;
import com.example.explore.R;
import com.example.explore.Remote.IGoogleAPIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoriteActivity extends AppCompatActivity {

    public TextView textv;
    private List<Favorite> favoriteList = new ArrayList<>();
    private int favIndex;
    private RecyclerView recyclerView;
    private FavoriteAdapter mAdapter;
    private List<PlaceDetail> favoritePlaces = new ArrayList<>();
    IGoogleAPIService mService;
    int currentUserId;

    ProgressBar progressBar;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.procent);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        progressAnimation();

        //

        
        textv = (TextView)findViewById(R.id.FavNotFound);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/YARDSALE.TTF");
        textv.setTypeface(face);

        recyclerView = findViewById(R.id.favoriteplaces_recycleView);
        SharedPreferences sharedPref = getSharedPreferences("explore", MODE_PRIVATE);
        currentUserId = sharedPref.getInt("userid",0);
        mService = Common.getGoogleAPIService();
        callFavorite();

      //  SharedPreferences sharedPreferences = getSharedPreferences("explore", MODE_PRIVATE);
  //      currentUserId = sharedPreferences.getInt("userid", 0);
//
    }

    private void progressAnimation() {
        ProgressBarAnimation amin = new ProgressBarAnimation(this, progressBar,textView, 0f, 100f);
        amin.setDuration(4000);
        progressBar.setAnimation(amin);
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
                        getPlaceDetail(favoriteList.get(favIndex).getPlaceid());
                        textv.setText("View your favorite places!");
                    }else if (favoriteList.size() == 0){

                    //    Intent moveToNoF = new Intent(FavoriteActivity.this, NoFavoritePlaces.class);
                      //  startActivity(moveToNoF);

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

    private void incercare() {
        textv.setText("Nu avem nimic aici");
    }


    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new FavoriteAdapter(favoritePlaces, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void getPlaceDetail(String id){

        mService.getDetailPlace(getPlaceDetailUrl(id))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        PlaceDetail mPlace = response.body();
                        if (mPlace != null){
                           // if(mPlace.getResult().getName()!=null) {
    //                            Log.wtf("place", "value " + mPlace.getResult().getName());
                                if(favoritePlaces==null){
                                    favoritePlaces = new ArrayList<>();
                                }
                                favoritePlaces.add(mPlace);
                        //}
                         //   }
                        }

                        //
                        favIndex++;
                        if (favIndex < favoriteList.size()){
                            getPlaceDetail(favoriteList.get(favIndex).getPlaceid());
                        }else{
                            setupRecyclerView();
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {
                        t.printStackTrace();

                    }
                });
    }

    private String getPlaceDetailUrl(String place_id) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id
                +"&key=AIzaSyB0Y8jpKrw__OxhGqNHgsKU3Sb7hXxgLDA";
    }


}
