package com.example.sunshine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sunshine.helper.HourlyHelper;
import com.example.sunshine.R;
import com.example.sunshine.utilities.DrawableUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourViewHolder>{

    private int mNumberItems;
    private Context context;
    private ArrayList<HourlyHelper> hourlyHelperArrayList;

    public HourlyAdapter(int numberOfItems, Context context, ArrayList<HourlyHelper> hourlyHelper) {
        mNumberItems = numberOfItems;
        this.context = context;
        this.hourlyHelperArrayList = hourlyHelper;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.hourly_forecast,parent,false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class HourViewHolder extends RecyclerView.ViewHolder {

        private TextView timeHourly;
        private TextView temperatureHourly;
        private ImageView imageHourly;

        public HourViewHolder(View itemView) {
            super(itemView);

            timeHourly = (TextView) itemView.findViewById(R.id.time_hour);
            temperatureHourly = (TextView) itemView.findViewById(R.id.hourly_temperature);
            imageHourly = (ImageView) itemView.findViewById(R.id.hourly_image);

        }

        void bind(int position) {

            HourlyHelper current = hourlyHelperArrayList.get(position);

            SimpleDateFormat format = new SimpleDateFormat("HH");
            Date date = new Date(current.getTime() * 1000);
            String hour = format.format(date) + ":00";
            timeHourly.setText(hour);

            String icon = current.getIcon();
            imageHourly.setImageResource(DrawableUtils.getWeatherIcon(icon));

            temperatureHourly.setText(current.getTemperature()+"\u2103");

        }
    }
}
