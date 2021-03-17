package com.example.android.sqliteweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sqliteweather.data.CitiesRepo;
import com.example.android.sqliteweather.data.FiveDayForecast;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.LocationViewHolder>{
    private List<CitiesRepo> city;
    private OnNavigationItemClickListener onNavigationItemClickListener;

    private FiveDayForecast fiveDayForecast;
    private ForecastAdapter.OnForecastItemClickListener onForecastItemClickListener;

    public interface OnNavigationItemClickListener {
        void onNavigationItemClicked(CitiesRepo location);
    }

    public CityAdapter(OnNavigationItemClickListener clickListener) {
        onNavigationItemClickListener = clickListener;
    }

    public void updateLocations(List<CitiesRepo> cities) {
        this.city = cities;
        notifyDataSetChanged();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        holder.bind(this.city.get(position));
    }

    @Override
    public int getItemCount() {
        if(this.city != null)
            return this.city.size();
        else
            return 0;
    }

    class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv;

        public LocationViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.movie_saved_item);
            itemView.setOnClickListener(this);
        }

        public void bind(CitiesRepo cities) {
            tv.setText(cities.city);
        }

        @Override
        public void onClick(View v) {
            CitiesRepo cities = city.get(getAdapterPosition());
            onNavigationItemClickListener.onNavigationItemClicked(cities);
        }
    }
}
