package it.rieger.happyhour.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.adapter.LocationAdapter;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.controller.cache.BitmapLRUCache;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.HappyHourTime;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.OpeningTimes;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.LocationLoadedCallback;

public class FavoriteLocations extends AppCompatActivity implements LocationLoadedCallback {

    @Bind(R.id.activity_favorite_location_recycler_view)
    RecyclerView locationListView;

    @Bind(R.id.activity_favorite_location_searchView)
    SearchView searchView;

    @Bind(R.id.activity_favorite_location_progressBar)
    ProgressBar progressBar;

    List<Location> locationList = new ArrayList<>();

    private BottomBar bottomBar;

    private boolean start = true;

    /**
     * {@inheritDoc}
     * create the activity
     * @param savedInstanceState saved instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_locations);

        ButterKnife.bind(this);

        BackendDatabase.getInstance().loadFavorites(locationList, this);

        createBottomBar(savedInstanceState);

    }

    /**
     * create the list of the locations
     */
    private void createLocationList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        locationListView.setLayoutManager(linearLayoutManager);

        //TODO: Liste muss mit Serverdaten gefüllt werden
        final LocationAdapter locationAdapter = new LocationAdapter(locationList);
        locationListView.setAdapter(locationAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                locationAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    /**
     * {@inheritDoc}
     * @param outState the state which should be saved
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);

    }

    /**
     * create the bottom bar for this activity
     * @param savedInstanceState
     */
    private void createBottomBar(Bundle savedInstanceState){
        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (!start) {
                    switch (menuItemId) {
                        case R.id.bottomBarItemOne:
                            startActivity(new Intent(FavoriteLocations.this, Maps.class).
                                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            break;
                        case R.id.bottomBarItemThree:
                            startActivity(new Intent(FavoriteLocations.this, LocationList.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            break;
                        default:
                            break;
                    }
                }else{
                    start = false;
                }
            }


            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemTwo) {
                    locationListView.scrollToPosition(0);
                }
            }
        });

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, "#7B1FA2");

        bottomBar.selectTabAtPosition(1, false);
    }

    /**
     * TODO: remove
     */
    private void createSampeData(){
        locationList = new ArrayList<>();
        Time timefriday = new Time();
        timefriday.setDay(Day.FRIDAY);
        timefriday.setStartTime("23:00");
        timefriday.setEndTime("05:00");
        Time timesaturday = new Time();
        timesaturday.setDay(Day.SATURDAY);
        timesaturday.setStartTime("23:00");
        timesaturday.setEndTime("05:00");

        List<Time> times = new ArrayList<>();
        times.add(timefriday);
        times.add(timesaturday);

        HappyHourTime happyHourTime = new HappyHourTime(times);

        HappyHour happyHour = new HappyHour("Cuba Libre Doppeldecker", "5€", happyHourTime);
        List<HappyHour> happyHours = new ArrayList<>();
        happyHours.add(happyHour);

        BitmapLRUCache.getInstance().addBitmapToMemoryCache("C1", BitmapFactory.decodeResource(this.getResources(), R.mipmap.c1));


        List<String> imageKeys = new ArrayList<>();
        imageKeys.add("C1");

        OpeningTimes openingTimes = new OpeningTimes(times);
        Location location = new Location("Clubeins", 4.3f, "Steigerstraße 18", 11.0181322f, 50.9624967f, openingTimes, happyHours, imageKeys);

        locationList.add(location);
    }

    @Override
    public void locationLoaded() {

        progressBar.setVisibility(View.INVISIBLE);
        locationListView.setVisibility(View.VISIBLE);
        createLocationList();
    }
}
