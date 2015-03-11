package com.luizgadao.stormy.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by luizcarlos on 10/03/15.
 */
public class Day implements Parcelable {

    private long time;
    private String summary;
    private double temperatureMax;
    private String icon;

    public Day(){ }

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

    public int getTemperatureMax() {
        return (int) Math.round( temperatureMax );
    }

    public void setTemperatureMax( double temperatureMax ) {
        this.temperatureMax = temperatureMax;
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

    public int getIconId() {
        return Forecast.getIconId( icon );
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat dataFormat = new SimpleDateFormat( "EEEE" );
        dataFormat.setTimeZone( TimeZone.getTimeZone( Weather.timeZone ) );

        Date date = new Date( time * 1000 );
        return dataFormat.format( date );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeLong( time );
        dest.writeString( summary );
        dest.writeDouble( temperatureMax );
        dest.writeString( icon );
    }

    private Day(Parcel in)
    {
        time = in.readLong();
        summary = in.readString();
        temperatureMax = in.readDouble();
        icon = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel( Parcel source ) {
            return new Day( source );
        }

        @Override
        public Day[] newArray( int size ) {
            return new Day[ size ];
        }
    };
}
