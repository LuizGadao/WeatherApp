package com.luizgadao.stormy.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luizgadao.stormy.R;
import com.luizgadao.stormy.model.weather.Hour;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by luizcarlos on 11/03/15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] hours;

    public HourAdapter( Hour[] hours ) {
        this.hours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.hourly_item, parent, false );

        HourViewHolder viewHolder = new HourViewHolder( view );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( HourViewHolder holder, int position ) {
        Hour hour = hours[position];
        holder.bind( hour );
    }

    @Override
    public int getItemCount() {
        return hours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView( R.id.tv_time )
        TextView tvTime;
        @InjectView( R.id.tv_temperature )
        TextView tvTemperature;
        @InjectView( R.id.tv_summary )
        TextView tvSummary;
        @InjectView( R.id.iv_icon )
        ImageView ivIcon;

        public HourViewHolder( View view ) {
            super( view );
            ButterKnife.inject( this, view );

            view.setOnClickListener( this );
        }

        public void bind( Hour hour )
        {
            tvTime.setText( hour.getHour() );
            tvTemperature.setText( hour.getTemperature() + "" );
            tvSummary.setText( hour.getSummary() );
            ivIcon.setImageResource( hour.getIcon() );
        }

        @Override
        public void onClick( View v ) {
            Log.i( "ViewHolder", "click" );
        }
    }
}
