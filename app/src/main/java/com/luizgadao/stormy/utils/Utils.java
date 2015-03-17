package com.luizgadao.stormy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

}
