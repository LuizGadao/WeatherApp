package com.luizgadao.stormy.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by luizcarlos on 17/03/15.
 */
public class App extends Application{


    private static final String MY_PREFERENCE = "my_preference";
    static App application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static App getApplication()
    {
        return application;
    }
    
    private SharedPreferences getSharedPreferences()
    {
        return getSharedPreferences( MY_PREFERENCE, MODE_PRIVATE );
    }

    public void saveLatAndLng( double latitude, double longitude )
    {
        getSharedPreferences()
                .edit()
                .putString( "lat", Double.toString( latitude ) )
                .putString( "lng", Double.toString( longitude ) )
                .commit();
    }

    public LatLng getLatLng()
    {
        SharedPreferences sharedPreferences = getSharedPreferences();
        double lat = Double.parseDouble( sharedPreferences.getString( "lat", "-1" ) );
        double lng = Double.parseDouble( sharedPreferences.getString( "lgn", "-1" ) );

        if ( lat == -1 && lng == -1 )
            return null;

        return new LatLng( lat, lng );
    }

}
