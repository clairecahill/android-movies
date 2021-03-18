package com.example.android.sqliteweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sqliteweather.data.MovieData;
import com.example.android.sqliteweather.data.TVShowsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TVShowsAdapter extends RecyclerView.Adapter<TVShowsAdapter.TVShowItemViewHolder> {
    private ArrayList<TVShowsData> TVShows;
    private ArrayList<String> test = new ArrayList <String> ();
    private ArrayList<Float> testF = new ArrayList<Float>();
    private ArrayList<TVShowsData> tempTVShows = new ArrayList <TVShowsData> ();
    private OnTVShowClickListener onTVShowClickListener;
    private SharedPreferences sharedPreferences;

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


    public void updatePopularTVShows(String sort, ArrayList<TVShowsData> TVShows) {
        this.TVShows = TVShows;

        test.clear();
        testF.clear();
        tempTVShows.clear();
        String[] parts = sort.split("\\.");

        //Decide which thing to sort on
        for (int i = 0; i < TVShows.size(); i++)
        {
            switch(parts[0]) {
//                case "release_date":
//                    test.add(String.valueOf(TVShows.get(i).getReleaseDate()));
//                    break;
                case "original_title":
                    test.add(String.valueOf(TVShows.get(i).getTitle()));
                    break;
                case "vote_average":
                    testF.add(TVShows.get(i).getVoteAverage());
                    break;
                default:
                    testF.add(TVShows.get(i).getPopularity());
                    break;
            }
        }

        //Order it ascending or descending
        if(parts[1] == "asc") {
            if(!test.isEmpty()) {
                Collections.sort(test);
            } else {
                Collections.sort(testF);
            }
        } else {
            if(!test.isEmpty()) {
                Collections.sort(test, Collections.reverseOrder());
            } else {
                Collections.sort(testF, Collections.reverseOrder());
            }
        }

        for (int i = 0; i < TVShows.size(); i++)
        {
            for (int j = 0; j < TVShows.size(); j++)
            {
                switch(parts[0]) {
//                    case "release_date":
//                        if (test.get(i) == String.valueOf(TVShows.get(j).getReleaseDate())) {
//                            tempTVShows.add(TVShows.get(j));
//                        }
//                        break;
                    case "original_title":
                        if (test.get(i) == String.valueOf(TVShows.get(j).getTitle())) {
                            tempTVShows.add(TVShows.get(j));
                        }
                        break;
                    case "vote_average":
                        if (testF.get(i) == TVShows.get(j).getVoteAverage()) {
                            tempTVShows.add(TVShows.get(j));
                        }
                        break;
                    default:
                        if (testF.get(i) == TVShows.get(j).getPopularity()) {
                            tempTVShows.add(TVShows.get(j));
                        }
                        break;
                }
            }
        }


        this.TVShows = tempTVShows;
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
                releaseDateTV.setText(ctx.getString(R.string.movie_release_date, "Unavailable Date"));
            }

            genreTV.setText(ctx.getString(R.string.genre, popularTVShows.getGenre()));

        }

    }
}
