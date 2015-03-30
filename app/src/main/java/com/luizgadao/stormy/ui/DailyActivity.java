package com.luizgadao.stormy.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Parcelable;

import com.luizgadao.stormy.R;
import com.luizgadao.stormy.adapter.DayAdapter;
import com.luizgadao.stormy.model.weather.Day;

import java.util.Arrays;

public class DailyActivity extends ListActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_daily );

        Day[] days;
        Parcelable[] parcelable = getIntent().getParcelableArrayExtra( WeatherFragment.DAILY_FORECAST );
        days = Arrays.copyOf( parcelable, parcelable.length, Day[].class );

        DayAdapter adapter = new DayAdapter( this, days );
        setListAdapter( adapter );
    }
}
