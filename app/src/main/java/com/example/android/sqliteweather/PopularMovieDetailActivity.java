package com.example.android.sqliteweather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.MovieData;
import com.example.android.sqliteweather.data.PopularResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PopularMovieDetailActivity extends AppCompatActivity {
    private final static String ICON_URL_FORMAT_STR = "https://image.tmdb.org/t/p/w500";
    public static final String EXTRA_MOVIE_DATA = "PopularMovieDetailActivity.MovieData";

    private MovieData popularMovieData = null;

    // view model if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.movietoolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_MOVIE_DATA))
        {
            this.popularMovieData = (MovieData) intent.getSerializableExtra(EXTRA_MOVIE_DATA);
            ImageView movieIconIV = findViewById(R.id.iv_detailed_movie_icon);

            if (this.popularMovieData.getIconUrl() == "null")
            {

            }

            else
            {
                String movieUrl = ICON_URL_FORMAT_STR + this.popularMovieData.getIconUrl();

                Glide.with(this)
                        .load(movieUrl)
                        .into(movieIconIV);
            }


            TextView popularMovieDetailedDataTV = findViewById(R.id.tv_detailed_movie_title);
            popularMovieDetailedDataTV.setText(this.popularMovieData.getTitle());

            TextView popularityTV = findViewById(R.id.tv_detailed_popularity);
            TextView overviewTV = findViewById(R.id.tv_detailed_overview);
            TextView voteAverageTV = findViewById(R.id.tv_detailed_vote_average);
            TextView releaseDateTV = findViewById(R.id.tv_detailed_release_date);

            System.out.println(popularMovieData.getReleaseDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");

            if (popularMovieData.getReleaseDate() != "null")
            {
                Date convertedDate = null;
                try {
                    convertedDate = sdf.parse(popularMovieData.getReleaseDate());
                    String date = sdf2.format(convertedDate);
                    releaseDateTV.setText(getString(R.string.movie_release_date, date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            int voteAverage = (int) popularMovieData.getVoteAverage();
            String voteAverageStr = String.valueOf(voteAverage) + "/10";
            voteAverageTV.setText(getString(R.string.movie_average_vote, voteAverageStr));
            popularityTV.setText(getString(R.string.movie_pop, popularMovieData.getPopularity()));
            overviewTV.setText(getString(R.string.movie_overview, popularMovieData.getOverview()));

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(this.popularMovieData.getTitle());
        }
    }
}
