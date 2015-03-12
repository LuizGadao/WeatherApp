package com.luizgadao.stormy.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.luizgadao.stormy.R;
import com.luizgadao.stormy.adapter.HourAdapter;
import com.luizgadao.stormy.model.weather.Hour;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HourlyActivity extends ActionBarActivity {

    @InjectView( R.id.recycler )
    RecyclerView recyclerView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_hourly );
        ButterKnife.inject( this );


        Hour[] hours;
        Parcelable[] parcelables = getIntent().getParcelableArrayExtra( StormyActivity.HOURLY_FORECAST );
        hours  = Arrays.copyOf( parcelables, parcelables.length, Hour[].class );

        HourAdapter adapter = new HourAdapter( hours );
        recyclerView.setAdapter( adapter );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
    }
}
