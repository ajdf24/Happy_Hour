package it.rieger.happyhour.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.LocationLoadedCallback;

/**
 * activity which shows a list of locations
 */
public class LocationList extends AppCompatActivity implements LocationLoadedCallback{

    @Bind(R.id.activity_location_list_recycler_view)
    RecyclerView locationListView;

    @Bind(R.id.activity_location_list_searchView)
    SearchView searchView;

    @Bind(R.id.activity_location_list_progressBar)
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
        setContentView(R.layout.activity_location_list);

        ButterKnife.bind(this);

        BackendDatabase.getInstance().loadLocations(locationList, this, new LatLng(0.0, 0.0), 10);

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
                            startActivity(new Intent(LocationList.this, Maps.class).
                                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            break;
                        case R.id.bottomBarItemTwo:
                            startActivity(new Intent(LocationList.this, FavoriteLocations.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
                if (menuItemId == R.id.bottomBarItemThree) {
                    locationListView.scrollToPosition(0);
                }
            }
        });

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, "#7B1FA2");

        bottomBar.selectTabAtPosition(2, false);
    }

    @Override
    public void locationLoaded() {

        progressBar.setVisibility(View.INVISIBLE);
        locationListView.setVisibility(View.VISIBLE);
        createLocationList();
    }
}
