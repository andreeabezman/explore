package com.example.explore.Remote;

import com.example.explore.Model.MyPlaces;
import com.example.explore.Model.PlaceDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface IGoogleAPIService {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);

    // new method to fetch api pt a afisa info depsre locatie --> ne ducem pe site ul -> pt a lua Photo din Googe Place  API
    // https://developers.google.com/places/web-service/photo
    //-> install picasso pt a afisa img
    @GET
    Call<PlaceDetail> getDetailPlace(@Url String url);

    @GET("maps/api/directions/json")
    Call<String> getDirections(@Query("origin") String origin, @Query("destination") String destination);
}
