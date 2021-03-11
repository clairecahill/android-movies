package com.example.android.sqliteweather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sqliteweather.data.CitiesRepo;
import com.example.android.sqliteweather.data.ForecastCity;
import com.example.android.sqliteweather.data.LoadingStatus;
import com.example.android.sqliteweather.data.PopularMovieData;
import com.example.android.sqliteweather.data.PopularMovies;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.OnMovieItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener, CityAdapter.OnNavigationItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    /*
     * To use your own OpenWeather API key, create a file called `gradle.properties` in your
     * GRADLE_USER_HOME directory (this will usually be `$HOME/.gradle/` in MacOS/Linux and
     * `$USER_HOME/.gradle/` in Windows), and add the following line:
     *
     *   OPENWEATHER_API_KEY="<put_your_own_OpenWeather_API_key_here>"
     *
     * The Gradle build for this project is configured to automatically grab that value and store
     * it in the field `BuildConfig.OPENWEATHER_API_KEY` that's used below.  You can read more
     * about this setup on the following pages:
     *
     *   https://developer.android.com/studio/build/gradle-tips#share-custom-fields-and-resource-values-with-your-app-code
     *
     *   https://docs.gradle.org/current/userguide/build_environment.html#sec:gradle_configuration_properties
     *
     * Alternatively, you can just hard-code your API key below 🤷‍.  If you do hard code your API
     * key below, make sure to get rid of the following line (line 18) in build.gradle:
     *
     *   buildConfigField("String", "OPENWEATHER_API_KEY", OPENWEATHER_API_KEY)
     */
//    private static final String OPENWEATHER_APPID = BuildConfig.OPENWEATHER_API_KEY;
    private static final String OPENWEATHER_APPID = "2048a626b634d6fc16289af021a195e6";
    private static final String MOVIEDB_APIKEY = "a9de941ba40e3e48d10c7644969d4781";

    private ForecastAdapter forecastAdapter;
    private FiveDayForecastViewModel fiveDayForecastViewModel;

    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    private SharedPreferences sharedPreferences;

    private ForecastCity forecastCity;
    private CityAdapter cityAdapter;

    private RecyclerView movieListRV;
    private RecyclerView forecastListRV;
    private RecyclerView cityItemsRV;
    private ProgressBar loadingIndicatorPB;
    private TextView errorMessageTV;
    private TextView addLocationTV;
    private String userText;

    private Toast errorToast;

    private DrawerLayout drawerLayout;
    private CityViewModel cityViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.loadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        this.errorMessageTV = findViewById(R.id.tv_error_message);
//        this.forecastListRV = findViewById(R.id.rv_forecast_list);
        this.movieListRV = findViewById(R.id.rv_movie_list);
        this.cityItemsRV = findViewById(R.id.rv_city);
//        this.forecastListRV.setLayoutManager(new LinearLayoutManager(this));
//        this.forecastListRV.setHasFixedSize(true);
        this.movieListRV.setLayoutManager(new LinearLayoutManager(this));
        this.movieListRV.setHasFixedSize(true);
        this.cityItemsRV.setLayoutManager(new LinearLayoutManager(this));
        this.cityItemsRV.setHasFixedSize(true);
        this.addLocationTV = findViewById(R.id.tv_add_location);


        this.movieAdapter = new MovieAdapter(this);
        this.movieListRV.setAdapter(this.movieAdapter);

//        this.forecastAdapter = new ForecastAdapter(this);
//        this.forecastListRV.setAdapter(this.forecastAdapter);
        this.cityAdapter = new CityAdapter(this);
        this.cityItemsRV.setAdapter(this.cityAdapter);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);

//        this.fiveDayForecastViewModel = new ViewModelProvider(this)
//                .get(FiveDayForecastViewModel.class);
//        this.loadForecast();

        this.movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        this.loadPopularMovies();

        this.cityViewModel = new ViewModelProvider(this)
                .get(CityViewModel.class);


//        NavigationView navigationView = findViewById(R.id.nv_nav_drawer);
//        navigationView.setNavigationItemSelectedListener(this);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_navigation_settings);

        this.movieViewModel.getPopularMovies().observe(
                this, new Observer<PopularMovies>() {
                    @Override
                    public void onChanged(PopularMovies popularMovies) {
                        movieAdapter.updatePopularMovies(popularMovies);
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setTitle("Popular Movies");
                    }
                }
        );

        /*
         * Update UI to reflect newly fetched forecast data.
         */
//        this.fiveDayForecastViewModel.getFiveDayForecast().observe(
//                this,
//                new Observer<FiveDayForecast>() {
//                    @Override
//                    public void onChanged(FiveDayForecast fiveDayForecast) {
//                        forecastAdapter.updateForecastData(fiveDayForecast);
//                        if (fiveDayForecast != null) {
//                            forecastCity = fiveDayForecast.getForecastCity();
//                            ActionBar actionBar = getSupportActionBar();
//                            actionBar.setTitle(forecastCity.getName());
//                        }
//                    }
//                }
//        );


        /*
         * Update UI to reflect changes in loading status.
         */
        this.movieViewModel.getLoadingStatus().observe(
                this,
                new Observer<LoadingStatus>() {
                    @Override
                    public void onChanged(LoadingStatus loadingStatus) {
                        if (loadingStatus == LoadingStatus.LOADING) {
                            loadingIndicatorPB.setVisibility(View.VISIBLE);
                        } else if (loadingStatus == LoadingStatus.SUCCESS) {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            movieListRV.setVisibility(View.VISIBLE);
                            errorMessageTV.setVisibility(View.INVISIBLE);
                        } else {
                            loadingIndicatorPB.setVisibility(View.INVISIBLE);
                            movieListRV.setVisibility(View.INVISIBLE);
                            errorMessageTV.setVisibility(View.VISIBLE);
                            errorMessageTV.setText(getString(R.string.loading_error, "ヽ(。_°)ノ"));
                        }
                    }
                }
        );

        this.cityViewModel.getAllCities().observe(this, new Observer<List<CitiesRepo>>() {
            @Override
            public void onChanged(@Nullable List<CitiesRepo> cityItems) {
                cityAdapter.updateLocations(cityItems);
            }
        });
    }

    public void add_location(View v)
    {
        AlertDialog.Builder locationDialog = new AlertDialog.Builder(MainActivity.this);
        locationDialog.setTitle("Enter a location");

        final EditText locationInput = new EditText(MainActivity.this);
        locationInput.setInputType(InputType.TYPE_CLASS_TEXT);
        locationDialog.setView(locationInput);

        locationDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userText = locationInput.getText().toString();
                System.out.println("user entered in: " + userText);
                updateLocationManually(userText, sharedPreferences);
            }
        });

        locationDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        locationDialog.show();
    }

    @Override
    public void onMovieItemClick(PopularMovieData popularMovieData) {
        Intent intent = new Intent(this, PopularMovieDetailActivity.class);
        intent.putExtra(PopularMovieDetailActivity.EXTRA_MOVIE_DATA, popularMovieData);
        startActivity(intent);
//        Log.d(TAG, popularMovieData.getTitle() + " " + popularMovieData.getPopularity() + " " + popularMovieData.getIconUrl());
    }



//    @Override
//    public void onForecastItemClick(ForecastData forecastData) {
//        Intent intent = new Intent(this, ForecastDetailActivity.class);
//        intent.putExtra(ForecastDetailActivity.EXTRA_FORECAST_DATA, forecastData);
//        intent.putExtra(ForecastDetailActivity.EXTRA_FORECAST_CITY, this.forecastCity);
//        startActivity(intent);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                viewForecastCityInMap();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    public void updateLocationManually(String location, SharedPreferences sharedPreferences)
    {
        databaseLocation2(location);
        this.loadForecast2(location, sharedPreferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        databaseLocation(sharedPreferences);
        this.loadForecast();
    }

    public void databaseLocation2(String userCity) {
        CitiesRepo newCity = new CitiesRepo();
        newCity.city = null;
        newCity.timestamp = 0;
        String city = userCity;
        long timeSearched = System.currentTimeMillis();
        if(city != null) {
            newCity.city = city;
            newCity.timestamp = timeSearched;
            System.out.println("city: " + newCity.city + " timestamp: " + newCity.timestamp);
            this.cityViewModel.insertCity(newCity);
            drawerLayout.closeDrawers();
        }

        else {

        }
    }

    public void databaseLocation(SharedPreferences sharedPreferences) {
        CitiesRepo newCity = new CitiesRepo();
        newCity.city = null;
        newCity.timestamp = 0;
        String city = sharedPreferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_default_location));
        long timeSearched = System.currentTimeMillis();
        if(city != null) {
            newCity.city = city;
            newCity.timestamp = timeSearched;
            this.cityViewModel.insertCity(newCity);
        }

        else {

        }
    }

    private void loadForecast2(String location, SharedPreferences sharedPreferences) {
        System.out.println("location: " + location);
        this.fiveDayForecastViewModel.loadForecast(
                location,
                this.sharedPreferences.getString(
                        getString(R.string.pref_units_key),
                        getString(R.string.pref_units_default_value)
                ),
                OPENWEATHER_APPID
        );
    }

    /**
     * Triggers a new forecast to be fetched based on current preference values.
     */
    private void loadForecast() {
        this.fiveDayForecastViewModel.loadForecast(
                this.sharedPreferences.getString(
                        getString(R.string.pref_location_key),
                        "Corvallis,OR,US"
                ),
                this.sharedPreferences.getString(
                        getString(R.string.pref_units_key),
                        getString(R.string.pref_units_default_value)
                ),
                OPENWEATHER_APPID
        );
    }

    private void loadPopularMovies() {
        this.movieViewModel.loadPopularMovies(MOVIEDB_APIKEY);
    }

    /**
     * This function uses an implicit intent to view the forecast city in a map.
     */
    private void viewForecastCityInMap() {
        if (this.forecastCity != null) {
            Uri forecastCityGeoUri = Uri.parse(getString(
                    R.string.geo_uri,
                    this.forecastCity.getLatitude(),
                    this.forecastCity.getLongitude(),
                    12
            ));
            Intent intent = new Intent(Intent.ACTION_VIEW, forecastCityGeoUri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                if (this.errorToast != null) {
                    this.errorToast.cancel();
                }
                this.errorToast = Toast.makeText(
                        this,
                        getString(R.string.action_map_error),
                        Toast.LENGTH_LONG
                );
                this.errorToast.show();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();
        switch(item.getItemId())
        {
            case R.id.navigation_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onNavigationItemClicked(CitiesRepo cities) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_location_key), cities.city);
        editor.apply();
        drawerLayout.closeDrawers();
    }

//    @Override
//    public void onMovieItemClick(PopularMovieData popularMovieDataData) {
//        Log.d(TAG, popularMovieDataData.getTitle() + " " + popularMovieDataData.getPopularity());
//    }
}