package com.luizgadao.stormy.model.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luizcarlos on 10/03/15.
 */
public class Day {

    private long time;
    private String summary;
    private double temperatureMax;
    private String timeZone;
    private String icon;

    public long getTime() {
        return time;
    }

    public void setTime( long time ) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary( String summary ) {
        this.summary = summary;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax( double temperatureMax ) {
        this.temperatureMax = temperatureMax;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone( String timeZone ) {
        this.timeZone = timeZone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon( String icon ) {
        this.icon = icon;
    }

    public static Day[] fromJson( JSONObject daily ) throws JSONException {

            JSONArray data = daily.getJSONArray( "data" );
        Day[] days = new Day[ data.length() ];

        int len = data.length();
        for ( int i = 0; i < len; i++ ) {
            JSONObject jsonDay = data.getJSONObject( i );
            Day day = new Day();
            day.setSummary( jsonDay.getString( "summary" ) );
            day.setTime( jsonDay.getLong( "time" ) );
            day.setTemperatureMax( jsonDay.getDouble( "temperatureMax" ) );
            day.setIcon( jsonDay.getString( "icon" ) );

            days[ i ] = day;
        }

        return days;
    }
}
