package com.example.harry.weathertest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by harry on 10/10/17.
 */

public class WeatherData extends JSONObject{

    @SerializedName("weather")
    @Expose
    private List<JSONObject> weather;


    @SerializedName("main")
    @Expose
    private JSONObject main;

    @SerializedName("wind")
    @Expose
    private JSONObject wind;

    @SerializedName("sys")
    @Expose
    private JSONObject sys;

    public JSONObject getMain() {
        return main;
    }

    public void setMain(JSONObject main) {
        this.main = main;
    }

    public JSONObject getWind() {
        return wind;
    }

    public void setWind(JSONObject wind) {
        this.wind = wind;
    }

    public JSONObject getSys() {
        return sys;
    }

    public void setSys(JSONObject sys) {
        this.sys = sys;
    }


    public List<JSONObject> getWeather() {
        return weather;
    }

    public void setWeather(List<JSONObject> weather) {
        this.weather = weather;
    }



}
