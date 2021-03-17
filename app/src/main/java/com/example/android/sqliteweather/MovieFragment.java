package com.example.android.sqliteweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.MovieData;
import com.example.android.sqliteweather.data.MovieRepository;
import com.example.android.sqliteweather.data.PopularMovies;
import com.example.android.sqliteweather.data.PopularResult;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment implements MovieAdapter.OnMovieItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MovieFragment.class.getSimpleName();

    private static final String MOVIEDB_APIKEY = "a9de941ba40e3e48d10c7644969d4781";
    private RecyclerView movieListRV;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private ProgressBar loadingIndicator;
    private TextView errorMessageTV;

    private ArrayList<Integer> popularMovieIds;
    private SharedPreferences sharedPreferences;

    public MovieFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        this.loadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = view.findViewById(R.id.tv_error_message);
        this.movieListRV = view.findViewById(R.id.rv_movie_list);
        this.movieListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.movieListRV.setHasFixedSize(true);

        this.movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        this.movieAdapter = new MovieAdapter(this);
        this.movieListRV.setAdapter(this.movieAdapter);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        getActivity().setTitle("Movies");

        this.popularMovieIds = new ArrayList<>();

        String sort = sharedPreferences.getString(getString(R.string.pref_sort_by), "");
        this.loadSortedMovies(sort);
        //this.loadPopularMovies();

        this.movieViewModel.getPopularMovies().observe(
                getActivity(), new Observer<PopularMovies>() {
                    @Override
                    public void onChanged(PopularMovies popularMovies) {
                        if (popularMovies != null) {
                            popularMovieIds.clear();
                            for (PopularResult result : popularMovies.getPopularMovieResults()) {
                                popularMovieIds.add(result.getId());
                            }
                            loadMovieData(MOVIEDB_APIKEY, popularMovieIds);
                        }

                        movieAdapter.notifyDataSetChanged();
                    }
                }
        );


        this.movieViewModel.getMovieData().observe(
                getActivity(), new Observer<ArrayList<MovieData>>() {
                    @Override
                    public void onChanged(ArrayList<MovieData> movieData) {
                        if (movieData != null && movieData.size() == popularMovieIds.size()) {
                            movieAdapter.updatePopularMovies(movieData);
                        }
                        movieAdapter.notifyDataSetChanged();
                    }
                }
        );

        this.movieViewModel.getLoadingStatus().observe(
                getActivity(),
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicator.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicator.setVisibility(View.INVISIBLE);
                            movieListRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicator.setVisibility(View.INVISIBLE);
                            movieListRV.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                            errorMessageTV.setText(getString(R.string.loading_error, "ヽ(。_°)ノ"));
                        }
                    }
                }
        );

        return view;
    }

//    private void loadPopularMovies() {
//        this.movieViewModel.loadPopularMovies(MOVIEDB_APIKEY);
//    }

    private void loadSortedMovies(String sort) {
        this.movieViewModel.loadPopularMovies(MOVIEDB_APIKEY, sort);
    }

    private void loadMovieData(String apiKey, ArrayList<Integer> ids) {
        this.movieViewModel.loadMovieData(apiKey, ids);
    }

    @Override
    public void onMovieItemClick(MovieData popularMovieData) {
        Intent intent = new Intent(getActivity(), PopularMovieDetailActivity.class);
        intent.putExtra(PopularMovieDetailActivity.EXTRA_MOVIE_DATA, popularMovieData);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("TAG", "loading in movie frag");
        this.loadSortedMovies(sharedPreferences.getString(key, "popularity.desc"));
    }
}