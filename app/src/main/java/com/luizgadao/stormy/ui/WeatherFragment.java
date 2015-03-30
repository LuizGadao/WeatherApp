package com.luizgadao.stormy.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luizgadao.stormy.R;
import com.luizgadao.stormy.model.weather.Forecast;
import com.luizgadao.stormy.model.weather.Weather;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by luizcarlos on 30/03/15.
 */
public class WeatherFragment extends Fragment {

    public static final String DAILY_FORECAST = "daily_forecast";
    public static final String HOURLY_FORECAST = "hourly_forecast";

    @InjectView( R.id.iv_icon ) ImageView ivIcon;
    @InjectView( R.id.iv_degree ) ImageView ivDegree;
    @InjectView( R.id.tv_location ) TextView tvLocation;
    @InjectView( R.id.tv_summary ) TextView tvSummary;
    @InjectView( R.id.tv_time ) TextView tvTime;
    @InjectView( R.id.tv_temperature ) TextView tvTemperature;
    @InjectView( R.id.tv_humidity_value ) TextView tvHumidity;
    @InjectView( R.id.tv_rain_value ) TextView tvRain;

    private Forecast forecast;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance()
    {
        WeatherFragment weatherFragment = new WeatherFragment();
        return weatherFragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.fragment_weather, container, false );
        ButterKnife.inject( this, view );

        return view;
    }

    public void updateUi( Forecast forecast )
    {
        this.forecast = forecast;
        Weather weather = this.forecast.getCurrentWeather();

        ivIcon.setImageResource( weather.getIconId() );
        tvTime.setText( "At " + weather.getFormattedTime() + " it will be." );
        tvLocation.setText( Weather.timeZone );
        tvSummary.setText( weather.getSummary() );
        tvTemperature.setText( weather.getTemperature() + "" );
        tvHumidity.setText( weather.getHumidity() + "%" );
        tvRain.setText( weather.getPrecipChance() + "%" );
    }

    @OnClick( R.id.bt_daily )
    public void startDailyActivity()
    {
        Intent intent = new Intent( getActivity(), DailyActivity.class );
        intent.putExtra( DAILY_FORECAST, forecast.getDays() );
        startActivity( intent );
    }

    @OnClick( R.id.bt_hourly )
    public void startHourlyActivity()
    {
        Intent intent = new Intent( getActivity(), HourlyActivity.class );
        intent.putExtra( HOURLY_FORECAST, forecast.getHours() );
        startActivity( intent );
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast( Forecast forecast ) {
        this.forecast = forecast;
    }

}
