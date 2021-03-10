package com.example.android.sqliteweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.PopularMovieData;
import com.example.android.sqliteweather.data.PopularMovies;

import java.util.Calendar;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder> {
    private PopularMovies popularMovies;
    private OnMovieItemClickListener onMovieItemClickListener;

    public interface OnMovieItemClickListener {
        void onMovieItemClick(PopularMovieData popularMovieDataData);
    }

    public MovieAdapter(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    @NonNull
    @Override
    public MovieItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_list_item, parent, false);
        return new MovieItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemViewHolder holder, int position) {
        holder.bind(this.popularMovies.getPopularMovieResults().get(position));
    }

    public void updatePopularMovies(PopularMovies popularMovies) {
        this.popularMovies = popularMovies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.popularMovies == null || this.popularMovies.getPopularMovieResults() == null) {
            return 0;
        } else {
            return this.popularMovies.getPopularMovieResults().size();
        }
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {
        final private TextView titleTV;
        final private TextView popularityTV;


        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_movie_title);
            popularityTV = itemView.findViewById(R.id.tv_movie_pop);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMovieItemClickListener.onMovieItemClick(
                            popularMovies.getPopularMovieResults().get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(PopularMovieData popularMovieData) {
            Context ctx = this.itemView.getContext();

            titleTV.setText(ctx.getString(R.string.movie_title, popularMovieData.getTitle()));
            popularityTV.setText(ctx.getString(R.string.movie_pop, popularMovieData.getPopularity()));

        }

    }
}
