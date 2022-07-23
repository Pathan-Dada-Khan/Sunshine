package com.example.sunshine.adapters;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunshine.data.WeatherContract;
import com.example.sunshine.data.WeatherContract.WeatherEntry;
import com.example.sunshine.helper.WeatherHelper;
import com.example.sunshine.R;
import com.example.sunshine.loader.WeatherLoader;
import com.example.sunshine.utilities.DrawableUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

    Context context;
    HashMap<Integer, WeatherHelper> weatherHelperArrayList;
    Cursor mCursor;
    ViewHolder holder;

    UpdateBackgroundImage updateBackgroundImage;

    public interface UpdateBackgroundImage {
        void updateBackground(String icon, int position);
    }

    LoaderInterface loaderInterface;

    public interface LoaderInterface {
        LoaderManager getLoader();
    }

    public ViewPager2Adapter(Context context, HashMap<Integer, WeatherHelper> weatherHelperArrayList) {
        this.context = context;
        updateBackgroundImage = (UpdateBackgroundImage) context;
        loaderInterface = (LoaderInterface) context;
        this.weatherHelperArrayList = weatherHelperArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.main_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.holder = holder;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public boolean scrollUp() {
        return holder.scrollUp();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements LoaderManager.LoaderCallbacks<HashMap<Integer, WeatherHelper>> {

        private static final int HOURLY_LIST_ITEMS = 24;

        public HourlyAdapter mHourlyAdapter;
        public DailyAdapter mDailyAdapter;

        private static final String API_KEY = "YOUR_API_KEY";
        private static final String OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/onecall";

        RecyclerView hourlyForecastRecyclerview;
        RecyclerView dailyForecastRecyclerview;

        ScrollView scrollView;

        TextView temperature;
        TextView degree;
        TextView main;
        TextView sunrise;
        TextView sunset;
        TextView sensible;
        TextView humidity;
        TextView windSpeed;
        TextView pressure;

        TextView minMaxTemp;

        SeekBar sunSeek;

        public ViewHolder(View itemView) {
            super(itemView);

            hourlyForecastRecyclerview = itemView.findViewById(R.id.hourly_forecast_recyclerview);
            dailyForecastRecyclerview = itemView.findViewById(R.id.daily_forecast_recyclerview);

            scrollView = itemView.findViewById(R.id.scroll_view_main);

            temperature = itemView.findViewById(R.id.temperature);
            degree = itemView.findViewById(R.id.degree);
            main = itemView.findViewById(R.id.main);
            sunrise = itemView.findViewById(R.id.sunrise);
            sunset = itemView.findViewById(R.id.sunset);
            sensible = itemView.findViewById(R.id.sensible);
            humidity = itemView.findViewById(R.id.humidity);
            windSpeed = itemView.findViewById(R.id.wind_speed);
            pressure = itemView.findViewById(R.id.pressure);
            minMaxTemp = itemView.findViewById(R.id.min_max_temp);

            sunSeek = itemView.findViewById(R.id.sun_seek);

        }

        void bind(int position) {

            mCursor.moveToPosition(position);

            LinearLayoutManager layoutManagerHourly = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            hourlyForecastRecyclerview.setLayoutManager(layoutManagerHourly);

            LinearLayoutManager layoutManagerDaily = new LinearLayoutManager(context);
            dailyForecastRecyclerview.setLayoutManager(layoutManagerDaily);

            hourlyForecastRecyclerview.setHasFixedSize(true);
            dailyForecastRecyclerview.setHasFixedSize(true);

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                LoaderManager loaderManager = loaderInterface.getLoader();
                loaderManager.initLoader(position, null, this);
            }
        }

        private String formatHour(long timeStamp) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date = new Date(timeStamp * 1000);
            return format.format(date);
        }

        public boolean scrollUp() {

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            });
            return true;
        }

        @Override
        public Loader<HashMap<Integer, WeatherHelper>> onCreateLoader(int id, Bundle args) {

            mCursor.moveToPosition(id);

            int columnLatitude = mCursor.getColumnIndex(WeatherEntry.COLUMN_LATITUDE);
            int columnLongitude = mCursor.getColumnIndex(WeatherEntry.COLUMN_LONGITUDE);

            Uri baseUri = Uri.parse(OPEN_WEATHER_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon()
                    .appendQueryParameter("lat", String.valueOf(mCursor.getString(columnLatitude)))
                    .appendQueryParameter("lon", String.valueOf(mCursor.getString(columnLongitude)))
                    .appendQueryParameter("exclude", "minutely")
                    .appendQueryParameter("appid", API_KEY);

            return new WeatherLoader(context, id, uriBuilder.toString(), weatherHelperArrayList);
        }

        @Override
        public void onLoadFinished(Loader<HashMap<Integer, WeatherHelper>> loader, HashMap<Integer, WeatherHelper> data) {

            WeatherHelper current = data.get(loader.getId());

            if(current == null) return;

            mHourlyAdapter = new HourlyAdapter(HOURLY_LIST_ITEMS, context, current.getHourlyHelperArrayList());
            if (current.getHourlyHelperArrayList() != null)
                hourlyForecastRecyclerview.setAdapter(mHourlyAdapter);

            mDailyAdapter = new DailyAdapter(context, current.getDailyHelperArrayList());
            if (current.getDailyHelperArrayList() != null)
                dailyForecastRecyclerview.setAdapter(mDailyAdapter);

            setBindingData(current, loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<HashMap<Integer, WeatherHelper>> loader) {

        }

        private void setBindingData(WeatherHelper current, int id) {

            if(getAdapterPosition() != id) return;

            temperature.setText(String.valueOf(current.getCurrentTemp() - 273));
            degree.setText(context.getString(R.string.format_temperature_celsius));
            main.setText(current.getCurrentMain());
            sunrise.setText(formatHour(current.getSunrise()));
            sunset.setText(formatHour(current.getSunset()));
            sensible.setText((current.getFeelsLike() - 273) + context.getString(R.string.format_temperature_celsius));
            humidity.setText(current.getHumidity() + "%");
            windSpeed.setText(String.valueOf(current.getWindSpeed()));
            pressure.setText(current.getPressure() + "hPa");
            minMaxTemp.setText(current.getMax() + "/" + current.getMin() + "\u2103");
            sunSeek.setProgress(setSunSeek(current.getSunrise(), current.getSunset()));

            updateBackgroundImage.updateBackground(current.getIcon(), getAdapterPosition());

            long itemId = mCursor.getLong(mCursor.getColumnIndex(WeatherEntry._ID));

            ContentValues values = new ContentValues();
            values.put(WeatherContract.WeatherEntry.COLUMN_MAX, current.getMax());
            values.put(WeatherContract.WeatherEntry.COLUMN_MIN, current.getMin());
            values.put(WeatherContract.WeatherEntry.COLUMN_ICON, current.getIcon());
            Uri uri = ContentUris.withAppendedId(WeatherContract.WeatherEntry.CONTENT_URI, itemId);
            context.getContentResolver().update(uri, values, null, null);


        }

        private int setSunSeek(long sunrise, long sunset) {

            double duration = sunset - sunrise;
            double timeStamp = new Timestamp(System.currentTimeMillis()).getTime() / 1000;

            if (timeStamp > sunset) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sunSeek.setThumb(context.getDrawable(R.drawable.weather_moon));
                }
                int progress = (int) (((timeStamp - sunset) / duration) * 100);
                return progress;
            }

            int progress = (int) (((timeStamp - sunrise) / duration) * 100);

            return progress;
        }

    }

}
