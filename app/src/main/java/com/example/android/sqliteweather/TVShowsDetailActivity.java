package com.example.android.sqliteweather;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.sqliteweather.data.TVShowsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TVShowsDetailActivity extends AppCompatActivity {
    private final static String ICON_URL_FORMAT_STR = "https://image.tmdb.org/t/p/w500";
    public static final String EXTRA_TV_DATA = "TVShowsDetailActivity.PopularTVShowsData";

    private TVShowsData TVShowsData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        Toolbar toolbar = findViewById(R.id.showtoolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_TV_DATA))
        {
            this.TVShowsData = (TVShowsData) intent.getSerializableExtra(EXTRA_TV_DATA);
            ImageView movieIconIV = findViewById(R.id.iv_detailed_tv_icon);
            String movieUrl = ICON_URL_FORMAT_STR + this.TVShowsData.getIconUrl();
            Glide.with(this)
                    .load(movieUrl)
                    .into(movieIconIV);

            TextView popularMovieDetailedDataTV = findViewById(R.id.tv_detailed_show_title);
            popularMovieDetailedDataTV.setText(this.TVShowsData.getTitle());

            TextView popularityTV = findViewById(R.id.tv_detailed_show_popularity);
            TextView overviewTV = findViewById(R.id.tv_detailed_show_overview);
            TextView voteAverageTV = findViewById(R.id.tv_detailed_show_vote_average);
            TextView releaseDateTV = findViewById(R.id.tv_detailed_first_aired);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");
            Date convertedDate = null;
            try {
                convertedDate = sdf.parse(TVShowsData.getFirstAirDate());
                String date = sdf2.format(convertedDate);
                releaseDateTV.setText(getString(R.string.movie_release_date, date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int voteAverage = (int) TVShowsData.getVoteAverage();
            String voteAverageStr = String.valueOf(voteAverage) + "/10";

            voteAverageTV.setText(getString(R.string.movie_average_vote, voteAverageStr));
            popularityTV.setText(getString(R.string.movie_pop, TVShowsData.getPopularity()));
            overviewTV.setText(getString(R.string.movie_overview, TVShowsData.getOverview()));

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(this.TVShowsData.getTitle());

        }


    }

}
