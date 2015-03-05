package com.luizgadao.stormy;

import android.app.Fragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luizgadao.stormy.model.Weather;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class StormyActivity extends ActionBarActivity {

    private static final String TAG = StormyActivity.class.getSimpleName();

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_stormy );

        if ( savedInstanceState == null ) {
            getFragmentManager().beginTransaction()
                    .add( R.id.container, WeatherFragment.newInstance() )
                    .commit();
        }

        double latitude = 37.8267;
        double longitude = -122.423;

        String url = String.format("https://api.forecast.io/forecast/%s/%s,%s", getString( R.string.api_key ), latitude, longitude);

        loadData( url );
    }

    private void loadData( String url ) {

        Log.i( TAG, "load url: " + url );
        if ( isNetworkAvailable() )
        {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url( url ).build();
            Call call = client.newCall( request );
            call.enqueue( new Callback() {
                @Override
                public void onFailure( Request request, IOException e ) {

                }

                @Override
                public void onResponse( Response response ) throws IOException {
                    try {
                        if ( response.isSuccessful() ) {
                            String jsonData = response.body().string();
                            Log.v( TAG, jsonData );

                            Weather weather = Weather.fromJson( jsonData );
                            Log.i( TAG, "time: " + weather.getFormattedTime() );
                        }
                        else
                            alertUserAboutError();

                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            } );
        }
        else
            Toast.makeText( this, getString( R.string.network_unvailable ), Toast.LENGTH_SHORT ).show();
    }

    private void alertUserAboutError() {
        AlertDialogFragment alert = new AlertDialogFragment();
        alert.show( getFragmentManager(), "error_dialog" );
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ( info != null & info.isConnected() )
            return true;

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_stormy, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings ) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    public static class WeatherFragment extends Fragment
    {

        public WeatherFragment() {
        }

        public static Fragment newInstance()
        {
            WeatherFragment weatherFragment = new WeatherFragment();
            return weatherFragment;
        }

        @Override
        public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

            View view = inflater.inflate( R.layout.fragment_weather, container, false );

            return view;
        }
    }
}
