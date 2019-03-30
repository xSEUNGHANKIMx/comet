package io.comet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherModel {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("currently")
    @Expose
    private CurrentlyWeatherModel mCurrent;
    @SerializedName("daily")
    @Expose
    private DailyWeatherModel mDaily;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public CurrentlyWeatherModel getCurrently() {
        return mCurrent;
    }

    public void setCurrently(CurrentlyWeatherModel currently) {
        this.mCurrent = currently;
    }

    public DailyWeatherModel getDaily() {
        return mDaily;
    }

    public void setDaily(DailyWeatherModel daily) {
        this.mDaily = daily;
    }
}

