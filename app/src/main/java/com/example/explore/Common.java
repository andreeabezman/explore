package com.example.explore;

import com.example.explore.Model.Results;
import com.example.explore.Remote.IGoogleAPIService;
import com.example.explore.Remote.RetrofitClient;
import com.example.explore.Remote.RetrofitScalarsClient;

public class Common {

    public static Results currentResult;

    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService(){
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }

    public static IGoogleAPIService getGoogleAPIServiceScalars(){
        return RetrofitScalarsClient.getScalarsClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
