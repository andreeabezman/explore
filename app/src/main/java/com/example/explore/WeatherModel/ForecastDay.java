package com.example.explore.WeatherModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ForecastDay {
    @SerializedName("temp")
    private Temperature temperature;

    @SerializedName("pressure")
    private double pressure;

    @SerializedName("humidity")
    private double humidityPercentage;

    @SerializedName("weather")
    private WeatherCondition[] condition;

    @SerializedName("clouds")
    private double cloudsPercentage;

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidityPercentage() {
        return humidityPercentage;
    }

    public void setHumidityPercentage(double humidityPercentage) {
        this.humidityPercentage = humidityPercentage;
    }

    public WeatherCondition[] getCondition() {
        return condition;
    }

    public void setCondition(WeatherCondition[] condition) {
        this.condition = condition;
    }

    public double getCloudsPercentage() {
        return cloudsPercentage;
    }

    public void setCloudsPercentage(double cloudsPercentage) {
        this.cloudsPercentage = cloudsPercentage;
    }

}
