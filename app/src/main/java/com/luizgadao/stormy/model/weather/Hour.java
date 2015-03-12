package com.luizgadao.stormy.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luizcarlos on 10/03/15.
 */
public class Hour implements Parcelable {

    private long time;
    private String summary;
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

    public int getIcon() {
        return Forecast.getIconId( icon );
    }

    public void setIcon( String icon ) {
        this.icon = icon;
    }

    public int getTemperature() {
        return (int)Math.round( temperature );
    }

    public void setTemperature( double temperature ) {
        this.temperature = temperature;
    }

    public String getHour()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "h a" );
        Date date = new Date( time * 1000 );
        return dateFormat.format( date );
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

    private Hour(){}

    private Hour( Parcel parcel )
    {
        time = parcel.readLong();
        summary = parcel.readString();
        icon = parcel.readString();
        temperature = parcel.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeLong( time );
        dest.writeString( summary );
        dest.writeString( icon );
        dest.writeDouble( temperature );
    }

    public static Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel( Parcel source ) {
            return new Hour( source );
        }

        @Override
        public Hour[] newArray( int size ) {
            return new Hour[ size ];
        }
    };
}
