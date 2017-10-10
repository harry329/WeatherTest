package com.example.harry.weathertest.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harry.weathertest.R;
import com.example.harry.weathertest.model.WeatherData;
import com.example.harry.weathertest.network.ApiClient;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.wangyuwei.loadingview.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lytLoading) FrameLayout lytLoading;
    @BindView(R.id.tvLoading) TextView tvLoading;
    @BindView(R.id.vLoading) LoadingView vLoading;
    @BindView(R.id.cityName) TextView cityName;
    @BindView(R.id.maxMinText) TextView maxMinText;
    @BindView(R.id.humidityText) TextView humidityText;
    @BindView(R.id.windText) TextView windText;
    @BindView(R.id.sunsetSunriseText) TextView sunsetSunriseText;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.imageView)
    ImageView displayIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick(R.id.search)
    public void search() {
        if(cityName.getText().toString() == null || cityName.getText().toString().trim().equals("")) {
            cityName.setError("Please put correct city name");
            return;
        }
        fetchWeatherData();
    }

    public void setVisible(boolean set) {
        if(set) {
            lytLoading.setVisibility(View.VISIBLE);
            tvLoading.setText("Loading");
            vLoading.start();
            Log.d("set","sset");
        } else {
            lytLoading.setVisibility(View.GONE);
            vLoading.stop();
        }
    }

    private void fetchWeatherData() {
        setVisible(true);
        Call<WeatherData> call =  ApiClient.apiInterface().getWeather(cityName.getText().toString(),ApiClient.API_KEY);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                setVisible(false);
                if(response.body() != null) {
                    showData(response.body());
                    Log.d("response" , " response is "+ new Gson().toJson(response));
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                setVisible(false);
            }
        });
    }

    private void showData(WeatherData data)  {
        try {
            //maxMinTkansasext.setText(data.getMain().get("temp_min") + "/" + data.getMain().get("temp_max"));
            humidityText.setText(data.getMain().toString());
            Log.d("no exception", "nod " + data.getMain().toString());
            Log.d("no exception", "nod " + data.getSys().toString());
            Log.d("no exception", "nod " + data.getWind().toString());
            Log.d("no exception", "nod " + data.getWeather().get(0));
            Log.d("no exception", "nod " + data.toString());
        }catch (Exception e) {
            Log.d("exception", " ");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
