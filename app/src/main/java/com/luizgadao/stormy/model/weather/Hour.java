package com.luizgadao.stormy.model.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luizcarlos on 10/03/15.
 */
public class Hour {

    private long time;
    private String summary;
    private String timeZone;
    private String icon;
    private double temperature;

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

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature( double temperature ) {
        this.temperature = temperature;
    }

    public static Hour[] fromJson( JSONObject hourly ) throws JSONException {

        JSONArray data = hourly.getJSONArray( "data" );
        Hour[] hours = new Hour[ data.length() ];

        int len = data.length();
        for ( int i = 0; i < len; i++ )
        {
            JSONObject jsonHour = data.getJSONObject( i );
            //set data for hour
            Hour hour = new Hour();
            hour.setTime( jsonHour.getLong( "time" ) );
            hour.setSummary( jsonHour.getString( "summary" ) );
            hour.setIcon( jsonHour.getString( "icon" ) );
            hour.setTemperature( jsonHour.getDouble( "temperature" ) );

            hours[i] = hour;
        }

        return hours;
    }
}
