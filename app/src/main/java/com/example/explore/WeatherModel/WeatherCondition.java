package com.example.explore.WeatherModel;

import com.google.gson.annotations.SerializedName;

public class WeatherCondition {
    @SerializedName("main")
    private String condition;

    @SerializedName("description")
    private String conditionDetails;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConditionDetails() {
        return conditionDetails;
    }

    public void setConditionDetails(String conditionDetails) {
        this.conditionDetails = conditionDetails;
    }
}
