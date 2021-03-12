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
import androidx.viewpager2.widget.ViewPager2;

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
import com.example.android.sqliteweather.data.PopularTVShows;
import com.example.android.sqliteweather.data.PopularTVShowsData;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener, CityAdapter.OnNavigationItemClickListener{
//        implements MovieAdapter.OnMovieItemClickListener, TVShowsAdapter.OnTVShowClickListener,
//        SharedPreferences.OnSharedPreferenceChangeListener,
//        NavigationView.OnNavigationItemSelectedListener, CityAdapter.OnNavigationItemClickListener {
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
    private SharedPreferences sharedPreferences;
    private DrawerLayout drawerLayout;
    private ForecastCity forecastCity;
    private Toast errorToast;
    private RecyclerView movieItemsRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager2 = findViewById(R.id.pager);
        viewPager2.setAdapter(new PageAdapter(this));

        // Navigation Drawer: recycler view and drawer layout initialization
        this.drawerLayout = findViewById(R.id.drawer_layout);
        this.movieItemsRV = findViewById(R.id.nav_drawer_id);
        this.movieItemsRV.setLayoutManager(new LinearLayoutManager(this));
        this.movieItemsRV.setHasFixedSize(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_navigation_settings);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        this.drawerLayout = findViewById(R.id.drawer_layout);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Movies");
                        break;
                    }

                    case 1: {
                        tab.setText("TV Shows");
                        break;
                    }
                }
            }
        }
        );
        tabLayoutMediator.attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                {
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle("Popular Movies");
                }

                else
                {
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle("Popular TV Shows");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onNavigationItemClicked(CitiesRepo cities) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.pref_location_key), cities.city);
        editor.apply();
        drawerLayout.closeDrawers();
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void addToSavedLocations(View view){
        AlertDialog.Builder ld = new AlertDialog.Builder(MainActivity.this);
        ld.setTitle("Search For Location:");
        final EditText location = new EditText(MainActivity.this);
        final String[] input = new String[1];
        location.setInputType(InputType.TYPE_CLASS_TEXT);
        ld.setView(location);

        ld.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input[0] = location.getText().toString();
//                updateLocationsInDB(null, input[0]);
//                loadForecast(input[0]);
            }
        });

        ld.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ld.show();
    }

//    public void updateLocationsInDB(SharedPreferences sharedPreferences, String location){
//        LocationsRepository addedLocation = new LocationsRepository();
//        long timeOfUpdate = System.currentTimeMillis();
//        String newLocation = location;
//
//        if (sharedPreferences != null) {
//            newLocation = sharedPreferences.getString(
//                    getString(R.string.pref_location_key),
//                    getString(R.string.pref_default_location));
//        }
//
//        if (newLocation != null){
//            addedLocation.location = newLocation.toUpperCase();
//            addedLocation.timestamp = timeOfUpdate;
//            this.mSavedLocationsVM.insertLocation(addedLocation);
//        } else { }
//    }
//
//    /**
//     * Triggers a new forecast to be fetched based on current preference values.
//     */
//    private void loadForecast(String location) {
//        this.fiveDayForecastViewModel.loadForecast(
//                location != null ? location : this.sharedPreferences.getString(
//                        getString(R.string.pref_location_key),
//                        "Corvallis,OR,US"
//                ),
//                this.sharedPreferences.getString(
//                        getString(R.string.pref_units_key),
//                        getString(R.string.pref_units_default_value)
//                ),
//                OPENWEATHER_APPID
//        );
//    }
}