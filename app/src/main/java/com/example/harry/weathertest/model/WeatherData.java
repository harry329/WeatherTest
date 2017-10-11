package com.example.harry.weathertest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by harry on 10/10/17.
 */

public class WeatherData extends JSONObject{

    @SerializedName("weather")
    @Expose
    private ArrayList<HashMap<String,Object>> weather;

    @SerializedName("main")
    @Expose
    private HashMap<String,Double> main;

    @SerializedName("wind")
    @Expose
    private HashMap<String,Double> wind;

    @SerializedName("sys")
    @Expose
    private HashMap<String,Object> sys;


    public ArrayList<HashMap<String,Object>> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<HashMap<String,Object>> weather) {
        this.weather = weather;
    }

    public HashMap<String, Double> getMain() {
        return main;
    }

    public void setMain(HashMap<String, Double> main) {
        this.main = main;
    }


    public HashMap<String, Double> getWind() {
        return wind;
    }

    public void setWind(HashMap<String, Double> wind) {
        this.wind = wind;
    }

    public HashMap<String, Object> getSys() {
        return sys;
    }

    public void setSys(HashMap<String, Object> sys) {
        this.sys = sys;
    }

}
