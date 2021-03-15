package com.example.android.sqliteweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sqliteweather.data.TVShowsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowItemViewHolder> {
    private ArrayList<TVShowsData> TVShows;
    private OnTVShowClickListener onTVShowClickListener;

    public interface OnTVShowClickListener {
        void onTVItemClick(TVShowsData TVShows);
    }

    public TVShowsAdapter(TVShowsAdapter.OnTVShowClickListener onTVShowClickListener) {
        this.onTVShowClickListener = onTVShowClickListener;
    }

    @NonNull
    @Override
    public TVShowsAdapter.TVShowItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.tv_list_item, parent, false);
        return new TVShowItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowItemViewHolder holder, int position) {
        holder.bind(this.TVShows.get(position));
    }


    public void updatePopularTVShows(ArrayList<TVShowsData> TVShows) {
        this.TVShows = TVShows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.TVShows == null) {
            return 0;
        } else {
            return this.TVShows.size();
        }
    }

    class TVShowItemViewHolder extends RecyclerView.ViewHolder {
        final private TextView titleTV;
        final private TextView popularityTV;
        final private TextView voteAveTV;
        final private TextView releaseDateTV;
        final private TextView genreTV;


        public TVShowItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_tv_title);
            popularityTV = itemView.findViewById(R.id.tv_popularity);
            voteAveTV = itemView.findViewById(R.id.tv_vote_average);
            releaseDateTV = itemView.findViewById(R.id.tv_release_date);
            genreTV = itemView.findViewById(R.id.tv_tv_genre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTVShowClickListener.onTVItemClick(
                            TVShows.get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(TVShowsData popularTVShows) {
            Context ctx = this.itemView.getContext();

            titleTV.setText(ctx.getString(R.string.movie_title, popularTVShows.getTitle()));
            popularityTV.setText(ctx.getString(R.string.movie_pop, popularTVShows.getPopularity()));
            int voteAverage = (int) popularTVShows.getVoteAverage();
            String voteAverageStr = String.valueOf(voteAverage) + "/10";
            voteAveTV.setText(ctx.getString(R.string.movie_average_vote, voteAverageStr));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");
            Date convertedDate = null;
            try {
                convertedDate = sdf.parse(popularTVShows.getFirstAirDate());
                String date = sdf2.format(convertedDate);
                releaseDateTV.setText(ctx.getString(R.string.movie_release_date, date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            genreTV.setText(ctx.getString(R.string.genre, popularTVShows.getGenre()));

        }

    }
}
