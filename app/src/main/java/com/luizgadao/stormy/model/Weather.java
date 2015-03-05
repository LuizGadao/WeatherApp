package com.luizgadao.stormy.model;

import com.luizgadao.stormy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by luizcarlos on 05/03/15.
 */
public class Weather {

    private String icon, summary, timeZone;
    private long time;
    private double temperature, humidity, precipChance;

    public Weather( String icon, String summary, long time, double temperature, double humidity, double precipChance ) {
        this.icon = icon;
        this.summary = summary;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipChance = precipChance;
    }

    public Weather() {

    }

    public String getIcon() {
        return icon;
    }

    public void setIcon( String icon ) {
        this.icon = icon;
    }

    public String getSummary() {
        return summary;
    }

    public int getIconId()
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

    public void setSummary( String summary ) {
        this.summary = summary;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "h:mm a" );
        formatter.setTimeZone( TimeZone.getTimeZone( timeZone ) );
        Date date = new Date( time * 1000 );
        String timeString = formatter.format( date );

        return timeString;
    }

    public void setTime( long time ) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature( double temperature ) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity( double humidity ) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance( double precipChance ) {
        this.precipChance = precipChance;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone( String timeZone ) {
        this.timeZone = timeZone;
    }

    /*
        currently: {
            time: 1425577062,
            summary: "Clear",
            icon: "clear-day",
            nearestStormDistance: 538,
            nearestStormBearing: 352,
            precipIntensity: 0,
            precipProbability: 0,
            temperature: 56.25,
            apparentTemperature: 56.25,
            dewPoint: 44.41,
            humidity: 0.64,
            windSpeed: 6.9,
            windBearing: 57,
            visibility: 9.97,
            cloudCover: 0,
            pressure: 1026.53,
            ozone: 326.06
        }
         */
    public static Weather fromJson(String stringJson) throws JSONException {
        JSONObject forecast = new JSONObject( stringJson );
        String timeZone = forecast.getString( "timezone" );

        JSONObject currently = forecast.getJSONObject( "currently" );
        Weather weather = new Weather();
        weather.setTimeZone( timeZone );
        weather.setHumidity( currently.getDouble( "humidity" ) );
        weather.setTime( currently.getLong( "time" ) );
        weather.setIcon( currently.getString( "icon" ) );
        weather.setPrecipChance( currently.getDouble( "precipIntensity" ) );
        weather.setSummary( currently.getString( "summary" ) );
        weather.setTemperature( currently.getDouble( "temperature" ) );

        return weather;
    }
}
