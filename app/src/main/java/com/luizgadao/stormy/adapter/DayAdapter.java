package com.luizgadao.stormy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luizgadao.stormy.R;
import com.luizgadao.stormy.model.weather.Day;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by luizcarlos on 11/03/15.
 */
public class DayAdapter extends BaseAdapter {

    private Context context;
    private Day[] days;

    public DayAdapter( Context context, Day[] days )
    {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem( int position ) {
        return days[position];
    }

    @Override
    public long getItemId( int position ) {
        return 0;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        ViewHolder viewHolder;

        if ( convertView == null )
        {
            convertView = LayoutInflater.from( context ).inflate( R.layout.daily_item, null );
            viewHolder = new ViewHolder( convertView );
            convertView.setTag( viewHolder );
        }
        else
            viewHolder = ( ViewHolder ) convertView.getTag();

        Day day = days[position];
        viewHolder.ivIcon.setImageResource( day.getIconId() );
        viewHolder.tvTemperature.setText( day.getTemperatureMax() + "" );

        if ( position == 0 )
            viewHolder.tvDay.setText( "Today" );
        else if ( position == 1 )
            viewHolder.tvDay.setText( "Tomorrow" );
        else
            viewHolder.tvDay.setText( day.getDayOfTheWeek() );

        return convertView;
    }

    public static class ViewHolder{

        @InjectView( R.id.iv_icon ) ImageView ivIcon;
        @InjectView( R.id.tv_temperature ) TextView tvTemperature;
        @InjectView( R.id.tv_day ) TextView tvDay;

        public ViewHolder( View view ) {
            ButterKnife.inject( this, view );
        }
    }
}
