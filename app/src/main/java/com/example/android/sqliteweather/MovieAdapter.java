package com.example.android.sqliteweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sqliteweather.data.MovieData;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder>  {
    private ArrayList<MovieData> movies;
    private ArrayList<String> test = new ArrayList <String> ();
    private ArrayList<Float> testF = new ArrayList<Float>();
    private ArrayList<MovieData> tempMovies = new ArrayList <MovieData> ();
    private OnMovieItemClickListener onMovieItemClickListener;
    private SharedPreferences sharedPreferences;

    public interface OnMovieItemClickListener {
        void onMovieItemClick(MovieData movieData);
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
        holder.bind(this.movies.get(position));
    }

    public void updatePopularMovies(String sort, ArrayList<MovieData> movies) {
        this.movies = movies;

        test.clear();
        testF.clear();
        tempMovies.clear();
        String[] parts = sort.split("\\.");

        //Decide which thing to sort on
        for (int i = 0; i < movies.size(); i++)
        {
            switch(parts[0]) {
//                case "release_date":
//                    test.add(String.valueOf(movies.get(i).getReleaseDate()));
//                    break;
                case "original_title":
                    test.add(String.valueOf(movies.get(i).getTitle()));
                    break;
                case "vote_average":
                    testF.add(movies.get(i).getVoteAverage());
                    break;
                default:
                    testF.add(movies.get(i).getPopularity());
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

        for (int i = 0; i < movies.size(); i++)
        {
            for (int j = 0; j < movies.size(); j++)
            {
                switch(parts[0]) {
//                    case "release_date":
//                        if (test.get(i) == String.valueOf(movies.get(j).getReleaseDate())) {
//                            tempMovies.add(movies.get(j));
//                        }
//                        break;
                    case "original_title":
                        if (test.get(i) == String.valueOf(movies.get(j).getTitle())) {
                            tempMovies.add(movies.get(j));
                        }
                        break;
                    case "vote_average":
                        if (testF.get(i) == movies.get(j).getVoteAverage()) {
                            tempMovies.add(movies.get(j));
                        }
                        break;
                    default:
                        if (testF.get(i) == movies.get(j).getPopularity()) {
                            tempMovies.add(movies.get(j));
                        }
                        break;
                }
            }
        }

        this.movies = tempMovies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.movies == null) {
            return 0;
        } else {
            return this.movies.size();
        }
    }

    class MovieItemViewHolder extends RecyclerView.ViewHolder {
        final private TextView titleTV;
        final private TextView popularityTV;
        final private TextView voteAveTV;
        final private TextView releaseDateTV;
        final private TextView genreTV;


        public MovieItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_movie_title);
            popularityTV = itemView.findViewById(R.id.tv_popularity);
            voteAveTV = itemView.findViewById(R.id.tv_vote_average);
            releaseDateTV = itemView.findViewById(R.id.tv_release_date);
            genreTV = itemView.findViewById(R.id.tv_movie_genre);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMovieItemClickListener.onMovieItemClick(
                            movies.get(getAdapterPosition())
                    );
                }
            });
        }

        public void bind(MovieData movieData) {
            Context ctx = this.itemView.getContext();

            titleTV.setText(ctx.getString(R.string.movie_title, movieData.getTitle()));
            popularityTV.setText(ctx.getString(R.string.movie_pop, movieData.getPopularity()));
            int voteAverage = (int) movieData.getVoteAverage();
            String voteAverageStr = String.valueOf(voteAverage) + "/10";
            voteAveTV.setText(ctx.getString(R.string.movie_average_vote, voteAverageStr));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");
            Date convertedDate = null;
            try {
                convertedDate = sdf.parse(movieData.getReleaseDate());
                String date = sdf2.format(convertedDate);
                releaseDateTV.setText(ctx.getString(R.string.movie_release_date, movieData.getReleaseDate()));
            } catch (ParseException e) {
                releaseDateTV.setText(ctx.getString(R.string.movie_release_date, "Unavailable Date"));
            }

            genreTV.setText(ctx.getString(R.string.genre, movieData.getGenre()));

        }

    }
}
