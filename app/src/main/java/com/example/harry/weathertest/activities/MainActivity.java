package com.example.harry.weathertest.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harry.weathertest.R;
import com.example.harry.weathertest.model.WeatherData;
import com.example.harry.weathertest.network.ApiClient;
import com.example.harry.weathertest.util.AppConstant;
import com.example.harry.weathertest.util.PreferenceStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.wangyuwei.loadingview.LoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.harry.weathertest.R.id.imageView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lytLoading) FrameLayout lytLoading;
    @BindView(R.id.tvLoading) TextView tvLoading;
    @BindView(R.id.vLoading) LoadingView vLoading;
    @BindView(R.id.cityName) TextView cityNameText;
    @BindView(R.id.maxMinText) TextView maxMinText;
    @BindView(R.id.humidityText) TextView humidityText;
    @BindView(R.id.windText) TextView windText;
    @BindView(R.id.sunsetSunriseText) TextView sunsetSunriseText;
    @BindView(R.id.description) TextView description;
    @BindView(imageView)
    ImageView displayIcon;

    private String cityName;
    private String minMax;
    private String humidity;
    private String wind;
    private String sunTimings;
    private String icon;
    private String desc;

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
                if(cityName == null || cityName.isEmpty()) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:"));
                intent.putExtra("sms_body", "Weather for " + cityName + " is " + desc);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        Bundle bundle = PreferenceStorage.getValues(this);
        if(bundle.getBoolean(AppConstant.DATA_PRESENT)) {
            getLocalData(bundle);
            showData();
        }
    }

    private void initContentDesc() {
        cityNameText.setContentDescription(cityNameText.getText());
        maxMinText.setContentDescription(maxMinText.getText());
        humidityText.setContentDescription(humidityText.getText());
        windText.setContentDescription(windText.getText());
        sunsetSunriseText.setContentDescription(sunsetSunriseText.getText());
        description.setContentDescription(description.getText());
    }

    private void getLocalData(Bundle bundle) {
        if(bundle != null) {
            cityName = bundle.getString(AppConstant.CITY);
            minMax = bundle.getString(AppConstant.TEMP_MAX);
            humidity = bundle.getString(AppConstant.HUMIDITY);
            wind = bundle.getString(AppConstant.SPEED);
            sunTimings = bundle.getString(AppConstant.SUNRISE);
            icon = bundle.getString(AppConstant.ICON);
            desc = bundle.getString(AppConstant.DESCRIPTION);
        }
    }

    private void getServerData(WeatherData data) {
        cityName = cityNameText.getText().toString();
        minMax = ((int) (data.getMain().get(AppConstant.TEMP_MAX) - 273)) + AppConstant.FARN + AppConstant.SLASH + ((int) (data.getMain().get(AppConstant.TEMP_MIN) - 273)) + AppConstant.FARN;
        humidity = data.getMain().get(AppConstant.HUMIDITY).toString().concat(AppConstant.PERCENT);
        wind = data.getWind().get(AppConstant.SPEED).toString().concat(AppConstant.US_UNIT);
        sunTimings = dateFormat(Math.round((double)data.getSys().get(AppConstant.SUNRISE))) + AppConstant.SLASH + dateFormat(Math.round((double)data.getSys().get(AppConstant.SUNSET)));
        icon = ApiClient.IMG_URL+data.getWeather().get(0).get(AppConstant.ICON)+".png";
        desc = data.getWeather().get(0).get(AppConstant.DESCRIPTION).toString();
    }

    private Bundle saveInfo() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AppConstant.DATA_PRESENT,true);
        bundle.putString(AppConstant.DESCRIPTION,desc);
        bundle.putString(AppConstant.ICON,icon);
        bundle.putString(AppConstant.TEMP_MAX,minMax);
        bundle.putString(AppConstant.HUMIDITY,humidity);
        bundle.putString(AppConstant.SPEED,wind);
        bundle.putString(AppConstant.SUNRISE,sunTimings);
        bundle.putString(AppConstant.CITY,cityName);
        return bundle;
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceStorage.storeValues(this,saveInfo());
    }

    @OnClick(R.id.search)
    public void search() {
        if(cityNameText.getText().toString() == null || cityNameText.getText().toString().trim().equals("")) {
            cityNameText.setError("Please put correct city name");
            return;
        }
        fetchWeatherData();
    }

    public void setVisible(boolean set) {
        if(set) {
            lytLoading.setVisibility(View.VISIBLE);
            tvLoading.setText("Loading");
            vLoading.start();
        } else {
            lytLoading.setVisibility(View.GONE);
            vLoading.stop();
        }
    }

    private void fetchWeatherData() {
        setVisible(true);
        Call<WeatherData> call =  ApiClient.apiInterface().getWeather(cityNameText.getText().toString(),ApiClient.API_KEY);
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                setVisible(false);
                if(response.body() != null) {
                    getServerData(response.body());
                    showData();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                setVisible(false);
                cityNameText.setError("Please put correct city name");
            }
        });
    }

    private String dateFormat(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    private void showData()  {
            humidityText.setText(humidity);
            maxMinText.setText(minMax);
            windText.setText(wind);
            sunsetSunriseText.setText(sunTimings);
            description.setText(desc);
            Picasso.with(getBaseContext()).load(icon).into(displayIcon);
            cityNameText.setText(cityName);
            initContentDesc();
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
