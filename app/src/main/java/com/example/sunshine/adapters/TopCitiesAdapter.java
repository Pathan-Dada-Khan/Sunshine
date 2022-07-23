package com.example.sunshine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sunshine.helper.CityHelper;
import com.example.sunshine.R;

import java.util.ArrayList;

public class TopCitiesAdapter extends RecyclerView.Adapter<TopCitiesAdapter.TopCitiesViewHolder> {

    Button  button;
    ArrayList<CityHelper> topCitiesList;
    ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void setOnCityClickListener(int position, ArrayList<CityHelper> topCitiesList);
    }

    public TopCitiesAdapter(ArrayList<CityHelper> topCitiesList, Context context){
        this.topCitiesList = topCitiesList;
        this.listItemClickListener = (ListItemClickListener) context;
    }

    @Override
    public TopCitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.top_cities,parent,false);
        TopCitiesViewHolder viewHolder = new TopCitiesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TopCitiesViewHolder holder, int position) {
        CityHelper current = topCitiesList.get(position);
        button.setText(current.getCityName());
    }

    @Override
    public int getItemCount() {
        return topCitiesList.size();
    }

    class TopCitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TopCitiesViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.city);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listItemClickListener.setOnCityClickListener(position,topCitiesList);
        }
    }
}
