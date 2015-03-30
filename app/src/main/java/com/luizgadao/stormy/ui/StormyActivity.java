package com.luizgadao.stormy.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.luizgadao.stormy.R;
import com.luizgadao.stormy.app.App;
import com.luizgadao.stormy.load.LoadDataWether;
import com.luizgadao.stormy.model.weather.Forecast;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class StormyActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = StormyActivity.class.getSimpleName();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleApiClient googleApiClient;
    private WeatherFragment currentWeatherFragment;
    private ImageRotation imageRotation;
    @InjectView( R.id.iv_refresh ) ImageView ivRefresh;
    private boolean firstExe;
    private LoadDataWether loadDataWether;

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
                loadData();
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

    private void loadData(){
        loadData( loadDataWether.getLatitude(), loadDataWether.getLongitude() );
    }

    private void loadData( double latitude, double longitude ) {

        if ( loadDataWether == null )
            loadDataWether = new LoadDataWether( this );

        imageRotation.start();
        loadDataWether.loadData( latitude, longitude, new LoadDataWether.Callback() {

            @Override
            public void onResponse( final Forecast forecast ) {
                if ( forecast != null )
                {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            currentWeatherFragment.updateUi( forecast );
                        }
                    } );
                }
                else
                    alertUserAboutError();
            }

            @Override
            public void onFailure() {}

            @Override
            public void onComplete() {
                imageRotation.pause();
            }
        } );
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
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            // save last location
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
}
