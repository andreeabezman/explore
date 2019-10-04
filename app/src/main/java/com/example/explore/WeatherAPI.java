package com.example.explore;

import com.example.explore.WeatherModel.ForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    String BASE_URL = "https://api.openweathermap.org/";
    String API_KEY = "cca7752c8ab83ff10bc1b877fbefe9d1";
///data/2.5/forecast/daily?lat=35&lon=139&cnt=10&appid=
    @GET("data/2.5/forecast/daily")
    Call<ForecastResponse> getForecastsForLocation(@Query("lat") double latitude,
                                                   @Query("lon") double longitude,
                                                   @Query("cnt") int count,
                                                   @Query("appid") String apiKey);
}
