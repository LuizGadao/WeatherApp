package com.luizgadao.stormy.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.luizgadao.stormy.R;
import com.luizgadao.stormy.app.App;
import com.luizgadao.stormy.model.weather.Forecast;
import com.luizgadao.stormy.model.weather.Weather;
import com.luizgadao.stormy.utils.Utils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class StormyActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = StormyActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "daily_forecast";
    public static final String HOURLY_FORECAST = "hourly_forecast";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    private GoogleApiClient googleApiClient;
    private WeatherFragment currentWeatherFragment;
    private ImageRotation imageRotation;
    @InjectView( R.id.iv_refresh ) ImageView ivRefresh;
    private double latitude = -1;
    private double longitude = -1;
    private boolean firstExe;

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

        //start google api client
        buildGoogleApiClient();

        imageRotation = new ImageRotation( ivRefresh );

        ivRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if ( latitude != -1 )
                    loadData( latitude, longitude );
            }
        } );

        if ( savedInstanceState != null )
            firstExe = savedInstanceState.getBoolean( "first_exe" );
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( googleApiClient.isConnected() )
            googleApiClient.disconnect();
    }

    private void loadData( double latitude, double longitude ) {

        String url = String.format("https://api.forecast.io/forecast/%s/%s,%s", getString( R.string.api_key ), latitude, longitude);
        imageRotation.start();

        Log.i( TAG, "load url: " + url );
        if ( Utils.isNetworkAvailable( getBaseContext() ) )
        {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url( url ).build();
            Call call = client.newCall( request );
            call.enqueue( new Callback() {
                @Override
                public void onFailure( Request request, IOException e ) {
                    imageRotation.pause();
                }

                @Override
                public void onResponse( Response response ) throws IOException {
                    imageRotation.pause();
                    try {
                        if ( response.isSuccessful() ) {
                            String jsonData = response.body().string();
                            Log.v( TAG, jsonData );
                            Forecast forecast = Forecast.fromJson( jsonData );
                            Log.i( TAG, "time: " + forecast.getCurrentWeather().getFormattedTime() );

                            currentWeatherFragment.setForecast( forecast );
                            runOnUiThread( new Runnable() {
                                @Override
                                public void run() {
                                    currentWeatherFragment.updateUi();
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

    /*--------------------------------- LOCATION PLAYSERVICE --------------------------------------*/
    @Override
    public void onConnected( Bundle bundle ) {
        Log.i( TAG, "location service connected" );
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation( googleApiClient );
        if ( lastLocation != null )
        {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            App.getApplication().saveLatAndLng( latitude, longitude );
            // load data with current location user
            loadData( latitude, longitude );
        }
        else {
            if ( ! firstExe )
                createAlertDialog();
        }
    }

    @Override
    public void onConnectionSuspended( int i ) {
        Log.i( TAG, "location service suspend." );

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult ) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST );
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /*--------------------------------- END LOCATION PLAYSERVICE --------------------------------------*/

    private void createAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( getString( R.string.attention) )
                .setMessage( getString( R.string.message_alert_attention) )
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                        startActivity( intent );
                    }
                } )
                .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        LatLng latLng = App.getApplication().getLatLng();

                        //load data with last coord save
                        if ( latLng != null )
                            loadData( latLng.latitude, latLng.longitude );
                        else
                        {
                            //load data with default coord
                            loadData( 37.8267, -122.423 );
                        }
                    }
                } )
                .setCancelable( false );
        builder.create().show();

        //Toast.makeText( this, "GEOLOCATION disabled", Toast.LENGTH_SHORT ).show();

    }

    @Override
    public void onSaveInstanceState( Bundle outState, PersistableBundle outPersistentState ) {
        super.onSaveInstanceState( outState, outPersistentState );

        outState.putBoolean( "first_exe", true );
    }

    public static class WeatherFragment extends Fragment
    {
        @InjectView( R.id.iv_icon ) ImageView ivIcon;
        @InjectView( R.id.iv_degree ) ImageView ivDegree;
        @InjectView( R.id.tv_location ) TextView tvLocation;
        @InjectView( R.id.tv_summary ) TextView tvSummary;
        @InjectView( R.id.tv_time ) TextView tvTime;
        @InjectView( R.id.tv_temperature ) TextView tvTemperature;
        @InjectView( R.id.tv_humidity_value ) TextView tvHumidity;
        @InjectView( R.id.tv_rain_value ) TextView tvRain;

        private Forecast forecast;

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

        public void updateUi()
        {
            Weather weather = forecast.getCurrentWeather();

            ivIcon.setImageResource( weather.getIconId() );
            tvTime.setText( "At " + weather.getFormattedTime() + " it will be." );
            tvLocation.setText( Weather.timeZone );
            tvSummary.setText( weather.getSummary() );
            tvTemperature.setText( weather.getTemperature() + "" );
            tvHumidity.setText( weather.getHumidity() + "%" );
            tvRain.setText( weather.getPrecipChance() + "%" );
        }

        @OnClick( R.id.bt_daily )
        public void startDailyActivity()
        {
            Intent intent = new Intent( getActivity(), DailyActivity.class );
            intent.putExtra( DAILY_FORECAST, forecast.getDays() );
            startActivity( intent );
        }

        @OnClick( R.id.bt_hourly )
        public void startHourlyActivity()
        {
            Intent intent = new Intent( getActivity(), HourlyActivity.class );
            intent.putExtra( HOURLY_FORECAST, forecast.getHours() );
            startActivity( intent );
        }

        public Forecast getForecast() {
            return forecast;
        }

        public void setForecast( Forecast forecast ) {
            this.forecast = forecast;
        }
    }
}
