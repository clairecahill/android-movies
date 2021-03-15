package com.example.android.sqliteweather;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sqliteweather.data.PopularResult;
import com.example.android.sqliteweather.data.PopularTVShows;
import com.example.android.sqliteweather.data.TVShowsData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TVShowFragment#} factory method to
 * create an instance of this fragment.
 */
public class TVShowFragment extends Fragment implements TVShowsAdapter.OnTVShowClickListener {
    private static final String MOVIEDB_APIKEY = "a9de941ba40e3e48d10c7644969d4781";
    private RecyclerView tvListRV;
    private TVShowsAdapter tvShowsAdapter;
    private TVShowsViewModel tvShowsViewModel;

    private ArrayList<Integer> popularTVShowIds;

    public TVShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_t_v_show, container, false);
        this.tvListRV = view.findViewById(R.id.rv_tv_list);
        this.tvListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.tvListRV.setHasFixedSize(true);

        this.tvShowsViewModel = new ViewModelProvider(this).get(TVShowsViewModel.class);
        this.tvShowsAdapter = new TVShowsAdapter(this);
        this.tvListRV.setAdapter(this.tvShowsAdapter);

        getActivity().setTitle("TV Shows");
        setHasOptionsMenu(true);

        this.popularTVShowIds = new ArrayList<>();
        this.loadPopularTVShows();

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
                            tvShowsAdapter.updatePopularTVShows(tvShowsData);
                        }
                    }
                }
        );

        return view;
    }

    private void loadPopularTVShows() {
        this.tvShowsViewModel.loadPopularTVShows(MOVIEDB_APIKEY);
    }

    private void loadTVShowData() { this.tvShowsViewModel.loadTVShowsData(MOVIEDB_APIKEY, this.popularTVShowIds);}

    @Override
    public void onTVItemClick(TVShowsData popularTVShows) {
        Intent intent = new Intent(getActivity(), TVShowsDetailActivity.class);
        intent.putExtra(TVShowsDetailActivity.EXTRA_TV_DATA, popularTVShows);
        startActivity(intent);
    }
}