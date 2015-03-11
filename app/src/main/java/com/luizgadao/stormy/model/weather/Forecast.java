package com.luizgadao.stormy.model.weather;

import com.luizgadao.stormy.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luizcarlos on 10/03/15.
 */
public class Forecast {

    private Weather currentWeather;
    private Day[] days;
    private Hour[] hours;
    private String timeZone;

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather( Weather currentWeather ) {
        this.currentWeather = currentWeather;
    }

    public Day[] getDays() {
        return days;
    }

    public void setDays( Day[] days ) {
        this.days = days;
    }

    public Hour[] getHours() {
        return hours;
    }

    public void setHours( Hour[] hours ) {
        this.hours = hours;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone( String timeZone ) {
        this.timeZone = timeZone;
    }

    public static int getIconId(String icon)
    {
        int iconId = 0;

        if (icon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (icon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (icon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (icon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (icon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (icon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (icon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (icon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (icon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (icon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

    public static Forecast fromJson( String stringJson ) throws JSONException {
        JSONObject jsonForecast = new JSONObject( stringJson );
        Forecast forecast = new Forecast();
        forecast.setTimeZone( jsonForecast.getString( "timezone" ) );

        forecast.setCurrentWeather( Weather.fromJson( stringJson ) );
        forecast.setHours( Hour.fromJson( jsonForecast.getJSONObject( "hourly" ) ) );
        forecast.setDays( Day.fromJson( jsonForecast.getJSONObject( "daily" ) ) );

        return forecast;
    }

}
