package com.example.sunshine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sunshine.helper.DailyHelper;
import com.example.sunshine.R;
import com.example.sunshine.utilities.DrawableUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder>{

    private Context context;
    private ArrayList<DailyHelper> dailyHelperArrayList;

    public DailyAdapter(Context context, ArrayList<DailyHelper> dailyHelperArrayList) {
        this.context = context;
        this.dailyHelperArrayList = dailyHelperArrayList;
    }

    @Override
    public DailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.daily_forecast,parent,false);
        DailyViewHolder viewHolder = new DailyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DailyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return dailyHelperArrayList.size();
    }

    class DailyViewHolder extends RecyclerView.ViewHolder {

        TextView date,day,max,min;
        ImageView dailyImage;

        public DailyViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.daily_date);
            day = (TextView) itemView.findViewById(R.id.daily_day);
            max = (TextView) itemView.findViewById(R.id.daily_max);
            min = (TextView) itemView.findViewById(R.id.daily_min);

            dailyImage = (ImageView) itemView.findViewById(R.id.daily_image);

        }

        void bind(int position) {

            DailyHelper current = dailyHelperArrayList.get(position);

            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM");
            SimpleDateFormat formatDay = new SimpleDateFormat("EEEE");
            Date timeStamp = new Date(current.getDate() * 1000);

            date.setText(formatDate.format(timeStamp));
            if(position == 0){
                day.setText(R.string.tomorrow);
            } else {
                day.setText(formatDay.format(timeStamp));
            }

            String icon = current.getIcon();
            dailyImage.setImageResource(DrawableUtils.getWeatherIcon(icon));

            max.setText(current.getMaxTemperature()+"\u2103");
            min.setText(current.getMinTemperature()+"\u2103");
        }
    }
}
