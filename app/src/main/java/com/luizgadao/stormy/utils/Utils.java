package com.luizgadao.stormy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by luizcarlos on 17/03/15.
 */
public class Utils {

    public static boolean isNetworkAvailable( Context context )
    {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ( info != null & info.isConnected() )
            return true;

        return false;
    }

    public static boolean isGooglePlayServiceAvailable( Context context )
    {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable( context );
        if ( status != ConnectionResult.SUCCESS )
            return false;

        return true;
    }

    public static int degreeFahrenheitToCelsius( int degree )
    {
        int temp = ( int ) Math.round( (degree - 32) * 0.55 );
        return temp;
    }

}
