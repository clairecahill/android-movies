package com.example.android.sqliteweather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.PopularMovieData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PopularMovieDetailActivity extends AppCompatActivity {
    private final static String ICON_URL_FORMAT_STR = "https://image.tmdb.org/t/p/w500";
    public static final String EXTRA_MOVIE_DATA = "PopularMovieDetailActivity.PopularMovieData";

    private PopularMovieData popularMovieData = null;

    // view model if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_MOVIE_DATA))
        {
            this.popularMovieData = (PopularMovieData) intent.getSerializableExtra(EXTRA_MOVIE_DATA);
            ImageView movieIconIV = findViewById(R.id.iv_detailed_movie_icon);
            String movieUrl = ICON_URL_FORMAT_STR + this.popularMovieData.getIconUrl();
            System.out.println(movieUrl);
            Glide.with(this)
                    .load(movieUrl)
                    .into(movieIconIV);

            TextView popularMovieDetailedDataTV = findViewById(R.id.tv_detailed_movie_title);
            popularMovieDetailedDataTV.setText(this.popularMovieData.getTitle());

            TextView popularityTV = findViewById(R.id.tv_detailed_popularity);
            TextView overviewTV = findViewById(R.id.tv_detailed_overview);
            TextView voteAverageTV = findViewById(R.id.tv_detailed_vote_average);
            TextView releaseDateTV = findViewById(R.id.tv_detailed_release_date);

            System.out.println(popularMovieData.getReleaseDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");
            Date convertedDate = null;
            try {
                convertedDate = sdf.parse(popularMovieData.getReleaseDate());
                String date = sdf2.format(convertedDate);
                releaseDateTV.setText(getString(R.string.movie_release_date, date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            voteAverageTV.setText(getString(R.string.movie_average_vote, popularMovieData.getVoteAverage()));
            popularityTV.setText(getString(R.string.movie_pop, popularMovieData.getPopularity()));
            overviewTV.setText(getString(R.string.movie_overview, popularMovieData.getOverview()));

        }
    }
}
