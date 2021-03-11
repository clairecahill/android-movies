package com.example.android.sqliteweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sqliteweather.data.PopularTVShows;
import com.example.android.sqliteweather.data.PopularTVShowsData;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowItemViewHolder> {
    private PopularTVShows popularTVShows;
    private OnTVShowClickListener onTVShowClickListener;

    public interface OnTVShowClickListener {
        void onTVItemClick(PopularTVShowsData popularTVShows);
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
        holder.bind(this.popularTVShows.getPopularTVResults().get(position));
    }


    public void updatePopularTVShows(PopularTVShows popularTVShows) {
        this.popularTVShows = popularTVShows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.popularTVShows == null || this.popularTVShows.getPopularTVResults() == null) {
            return 0;
        } else {
            return this.popularTVShows.getPopularTVResults().size();
        }
    }

    class TVShowItemViewHolder extends RecyclerView.ViewHolder {
        final private TextView titleTV;
        final private TextView popularityTV;


        public TVShowItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_show_title);
            popularityTV = itemView.findViewById(R.id.tv_show_pop);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTVShowClickListener.onTVItemClick(
                            popularTVShows.getPopularTVResults().get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(PopularTVShowsData popularTVShows) {
            Context ctx = this.itemView.getContext();

            titleTV.setText(ctx.getString(R.string.movie_title, popularTVShows.getTitle()));
            popularityTV.setText(ctx.getString(R.string.movie_pop, popularTVShows.getPopularity()));

        }

    }
}
