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
import com.example.android.sqliteweather.data.PopularResult;
import com.example.android.sqliteweather.data.PopularTVShows;
import com.example.android.sqliteweather.data.TVShowsData;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TVShowFragment#} factory method to
 * create an instance of this fragment.
 */
public class TVShowFragment extends Fragment implements TVShowsAdapter.OnTVShowClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String MOVIEDB_APIKEY = "a9de941ba40e3e48d10c7644969d4781";
    private RecyclerView tvListRV;
    private TVShowsAdapter tvShowsAdapter;
    private TVShowsViewModel tvShowsViewModel;
    private ProgressBar loadingIndicator;
    private TextView errorMessageTV;

    private ArrayList<Integer> popularTVShowIds;
    private SharedPreferences sharedPreferences;


    public TVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_t_v_show, container, false);
        this.loadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = view.findViewById(R.id.tv_error_message);
        this.tvListRV = view.findViewById(R.id.rv_tv_list);
        this.tvListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.tvListRV.setHasFixedSize(true);

        this.tvShowsViewModel = new ViewModelProvider(this).get(TVShowsViewModel.class);
        this.tvShowsAdapter = new TVShowsAdapter(this);
        this.tvListRV.setAdapter(this.tvShowsAdapter);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        getActivity().setTitle("TV Shows");
        setHasOptionsMenu(true);

        this.popularTVShowIds = new ArrayList<>();
        //this.loadPopularTVShows();

        String sort = sharedPreferences.getString(getString(R.string.pref_sort_by), "");
        this.loadSortedTVShows(sort);

        this.tvShowsViewModel.getPopularTVShows().observe(
                getActivity(), new Observer<PopularTVShows>() {
                    @Override
                    public void onChanged(PopularTVShows popularTVShows) {
                        if (popularTVShows != null) {
                            for (PopularResult result : popularTVShows.getPopularTVResults()) {
                                popularTVShowIds.add(result.getId());
                            }
                            loadTVShowData();
                        }
                    }
                }
        );

        this.tvShowsViewModel.getTvShowsData().observe(
                getActivity(),
                new Observer<ArrayList<TVShowsData>>() {
                    @Override
                    public void onChanged(ArrayList<TVShowsData> tvShowsData) {
                        if (tvShowsData != null && tvShowsData.size() == popularTVShowIds.size()) {
                            tvShowsAdapter.updatePopularTVShows(sort, tvShowsData);
                        }
                    }
                }
        );

        this.tvShowsViewModel.getLoadingStatus().observe(
                getActivity(),
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicator.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicator.setVisibility(View.INVISIBLE);
                            tvListRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicator.setVisibility(View.INVISIBLE);
                            tvListRV.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                            errorMessageTV.setText(getString(R.string.loading_error, "ヽ(。_°)ノ"));
                        }
                    }
                }
        );

        return view;
    }

//    private void loadPopularTVShows() {
//        this.tvShowsViewModel.loadPopularTVShows(MOVIEDB_APIKEY);
//    }

    private void loadSortedTVShows(String sort) {
        this.tvShowsViewModel.loadPopularTVShows(MOVIEDB_APIKEY, sort);
    }

    private void loadTVShowData() { this.tvShowsViewModel.loadTVShowsData(MOVIEDB_APIKEY, this.popularTVShowIds);}

    @Override
    public void onTVItemClick(TVShowsData popularTVShows) {
        Intent intent = new Intent(getActivity(), TVShowsDetailActivity.class);
        intent.putExtra(TVShowsDetailActivity.EXTRA_TV_DATA, popularTVShows);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("TAG", "loading in movie frag");
        this.loadSortedTVShows(sharedPreferences.getString(key, "popularity.desc"));
    }
}