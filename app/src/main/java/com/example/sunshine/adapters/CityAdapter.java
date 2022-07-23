package com.example.sunshine.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sunshine.data.WeatherContract;
import com.example.sunshine.R;
import com.example.sunshine.utilities.DrawableUtils;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder>{

    private ItemClickListener clickListener;
    private ItemLongClickListener longClickListener;

    private Cursor mCursor;

    public interface ItemLongClickListener {
        boolean setOnItemLongClickListener(int position, long id);
    }

    public interface ItemClickListener {
        void setOnItemClickListener(int position);
    }

    public CityAdapter(Context context) {
        this.clickListener = (ItemClickListener) context;
        this.longClickListener = (ItemLongClickListener) context;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.cities_list,parent,false);
        CityViewHolder viewHolder = new CityViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView cityName, countryName, cityMaxTemp, cityMinTemp;
        RelativeLayout cityBackground;

        public CityViewHolder(View itemView) {
            super(itemView);

            cityBackground = (RelativeLayout) itemView.findViewById(R.id.city_background);
            cityName = (TextView) itemView.findViewById(R.id.city);
            countryName = (TextView) itemView.findViewById(R.id.city_country);
            cityMaxTemp = (TextView) itemView.findViewById(R.id.city_max_temperature);
            cityMinTemp = (TextView) itemView.findViewById(R.id.city_min_temperature);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        void bind(int position) {

            mCursor.moveToPosition(position);

            Integer localityColumn = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_LOCALITY);
            Integer countryColumn = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_COUNTRY);
            Integer maxColumn = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX);
            Integer minColumn = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN);
            Integer iconColumn = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_ICON);

            String locality = mCursor.getString(localityColumn);
            String country = mCursor.getString(countryColumn);
            int max = mCursor.getInt(maxColumn);
            int min = mCursor.getInt(minColumn);
            String icon = mCursor.getString(iconColumn);

            cityName.setText(locality);
            countryName.setText(country);
            cityBackground.setBackgroundResource(DrawableUtils.getCityBackgroundDrawable(icon));
            cityMaxTemp.setText( max + "\u2103");
            cityMinTemp.setText( min +"\u2103");
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.setOnItemClickListener(position);
        }

        @Override
        public boolean onLongClick(View v) {

            int position = getAdapterPosition();



            mCursor.moveToPosition(position);
            Integer idColumn = mCursor.getColumnIndex(WeatherContract.WeatherEntry._ID);
            long id = mCursor.getLong(idColumn);

            longClickListener.setOnItemLongClickListener(position, id);
            return true;
        }
    }
}
