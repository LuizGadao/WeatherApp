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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luizgadao.stormy.model.Weather;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class StormyActivity extends ActionBarActivity {

    private static final String TAG = StormyActivity.class.getSimpleName();
    private WeatherFragment currentWeatherFragment;
    @InjectView( R.id.iv_refresh ) ImageView ivRefresh;
    @InjectView( R.id.progress_bar ) ProgressBar progressBar;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_stormy );
        ButterKnife.inject( this );

        if ( savedInstanceState == null ) {
            currentWeatherFragment = new WeatherFragment().newInstance();
            getFragmentManager().beginTransaction()
                    .add( R.id.container, currentWeatherFragment )
                    .commit();
        }

        final double latitude = 37.8267;
        final double longitude = -122.423;
        loadData( latitude, longitude );

        ivRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                loadData( latitude, longitude );
            }
        } );
    }

    private void togleProgresBar()
    {
        if ( progressBar.getVisibility() == View.INVISIBLE )
        {
            progressBar.setVisibility( View.VISIBLE );
            ivRefresh.setVisibility( View.INVISIBLE );
        }
        else
        {
            progressBar.setVisibility( View.INVISIBLE );
            ivRefresh.setVisibility( View.VISIBLE );
        }
    }

    private void togleProgressBarUIThread()
    {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                togleProgresBar();
            }
        } );
    }

    private void loadData( double latitude, double longitude ) {
        String url = String.format("https://api.forecast.io/forecast/%s/%s,%s", getString( R.string.api_key ), latitude, longitude);

        Log.i( TAG, "load url: " + url );
        if ( isNetworkAvailable() )
        {
            togleProgresBar();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url( url ).build();
            Call call = client.newCall( request );
            call.enqueue( new Callback() {
                @Override
                public void onFailure( Request request, IOException e ) {
                    togleProgressBarUIThread();
                }

                @Override
                public void onResponse( Response response ) throws IOException {
                    togleProgressBarUIThread();
                    try {
                        if ( response.isSuccessful() ) {
                            String jsonData = response.body().string();
                            Log.v( TAG, jsonData );
                            final Weather weather = Weather.fromJson( jsonData );
                            Log.i( TAG, "time: " + weather.getFormattedTime() );

                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    currentWeatherFragment.updateUi( weather );
                                }
                            } );

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
        @InjectView( R.id.iv_icon ) ImageView ivIcon;
        @InjectView( R.id.tv_location ) TextView tvLocation;
        @InjectView( R.id.tv_summary ) TextView tvSummary;
        @InjectView( R.id.tv_time ) TextView tvTime;
        @InjectView( R.id.tv_temperature ) TextView tvTemperature;
        @InjectView( R.id.tv_humidity_value ) TextView tvHumidity;
        @InjectView( R.id.tv_rain_value ) TextView tvRain;

        public WeatherFragment() {
        }

        public static WeatherFragment newInstance()
        {
            WeatherFragment weatherFragment = new WeatherFragment();
            return weatherFragment;
        }

        @Override
        public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

            View view = inflater.inflate( R.layout.fragment_weather, container, false );
            ButterKnife.inject( this, view );

            return view;
        }

        public void updateUi( Weather weather )
        {
            ivIcon.setImageResource( weather.getIconId() );
            tvTime.setText( "At " + weather.getFormattedTime() + " it will be." );
            tvLocation.setText( weather.getTimeZone() );
            tvSummary.setText( weather.getSummary() );
            tvTemperature.setText( weather.getTemperature() + "" );
            tvHumidity.setText( weather.getHumidity() + "%" );
            tvRain.setText( weather.getPrecipChance() + "%" );
        }
    }
}
