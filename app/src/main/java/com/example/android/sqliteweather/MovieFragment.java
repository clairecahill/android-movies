package com.example.android.sqliteweather;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sqliteweather.data.PopularMovieData;
import com.example.android.sqliteweather.data.PopularMovies;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieFragment} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment implements MovieAdapter.OnMovieItemClickListener {
    private static final String MOVIEDB_APIKEY = "a9de941ba40e3e48d10c7644969d4781";
    private RecyclerView movieListRV;
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    public MovieFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        this.movieListRV = view.findViewById(R.id.rv_movie_list);
        this.movieListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.movieListRV.setHasFixedSize(true);

        this.movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        this.movieAdapter = new MovieAdapter(this);
        this.movieListRV.setAdapter(this.movieAdapter);

        getActivity().setTitle("Movies");

        this.loadPopularMovies();

        this.movieViewModel.getPopularMovies().observe(
                getActivity(), new Observer<PopularMovies>() {
                    @Override
                    public void onChanged(PopularMovies popularMovies) {
                        movieAdapter.updatePopularMovies(popularMovies);
                    }
                }
        );

        return view;
    }

    private void loadPopularMovies() {
        this.movieViewModel.loadPopularMovies(MOVIEDB_APIKEY);
    }

    @Override
    public void onMovieItemClick(PopularMovieData popularMovieData) {
        Intent intent = new Intent(getActivity(), PopularMovieDetailActivity.class);
        intent.putExtra(PopularMovieDetailActivity.EXTRA_MOVIE_DATA, popularMovieData);
        startActivity(intent);
    }

}