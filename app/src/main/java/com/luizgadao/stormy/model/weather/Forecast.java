package com.luizgadao.stormy.model.weather;

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

    public static Forecast fromJson( String stringJson ) throws JSONException {
        JSONObject jsonForecast = new JSONObject( stringJson );
        Forecast forecast = new Forecast();
        forecast.setTimeZone( jsonForecast.getString( "timezone" ) );

        forecast.setCurrentWeather( Weather.fromJson( stringJson )  );
        forecast.setHours( Hour.fromJson( jsonForecast.getJSONObject( "hourly" ) ) );
        forecast.setDays( Day.fromJson( jsonForecast.getJSONObject( "daily" ) ) );

        return forecast;
    }

}
