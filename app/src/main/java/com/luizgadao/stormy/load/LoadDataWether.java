package com.luizgadao.stormy.load;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.luizgadao.stormy.R;
import com.luizgadao.stormy.model.weather.Forecast;
import com.luizgadao.stormy.utils.Utils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by luizcarlos on 30/03/15.
 */
public class LoadDataWether {


    private static final String TAG = LoadDataWether.class.getSimpleName();

    private double latitude, longitude;
    private Context context;

    public LoadDataWether( Context context ) {
        this.context = context;
    }

    public void loadData( double latitude, double longitude, final Callback callback ) {

        this.latitude = latitude;
        this.longitude = longitude;
        String url = String.format("https://api.forecast.io/forecast/%s/%s,%s", context.getString( R.string.api_key ), latitude, longitude);

        Log.i( TAG, "load url: " + url );
        if ( Utils.isNetworkAvailable( context ) )
        {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url( url ).build();
            Call call = client.newCall( request );
            call.enqueue( new com.squareup.okhttp.Callback() {
                @Override
                public void onFailure( Request request, IOException e ) {
                    callback.onComplete();
                    callback.onFailure();
                }

                @Override
                public void onResponse( Response response ) throws IOException {
                    callback.onComplete();
                    try {
                        if ( response.isSuccessful() ) {
                            String jsonData = response.body().string();
                            Log.v( TAG, jsonData );
                            Forecast forecast = Forecast.fromJson( jsonData );
                            Log.i( TAG, "time: " + forecast.getCurrentWeather().getFormattedTime() );
                            callback.onResponse( forecast );
                        }
                        else
                            callback.onResponse( null );

                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            } );
        }
        else
            Toast.makeText( context, context.getString( R.string.network_unvailable ), Toast.LENGTH_SHORT ).show();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public interface Callback{
        public void onResponse( Forecast forecast );
        public void onFailure();
        public void onComplete();
    }

}
