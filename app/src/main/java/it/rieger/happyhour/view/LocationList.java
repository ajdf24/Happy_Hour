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

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.adapter.LocationAdapter;
import it.rieger.happyhour.controller.cache.BitmapLRUCache;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.HappyHourTime;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.OpeningTimes;
import it.rieger.happyhour.model.Time;

public class LocationList extends AppCompatActivity {

    @Bind(R.id.activity_location_list_recycler_view)
    RecyclerView locationListView;

    List<Location> locationList;

    private BottomBar bottomBar;

    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        ButterKnife.bind(this);

        Log.e("Hier", "hier");

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

        createLocationList();

        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (!start) {
                    switch (menuItemId) {
                        case R.id.bottomBarItemOne:
                            startActivity(new Intent(LocationList.this, Maps.class).
                                                        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            break;
                        case R.id.bottomBarItemTwo:
                            break;
                        case R.id.bottomBarItemThree:
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
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user reselected item number one, scroll your content to top.
                }
                if (menuItemId == R.id.bottomBarItemTwo) {

                }
                if (menuItemId == R.id.bottomBarItemThree) {

                }
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, "#7B1FA2");

        //Aktiven Button setzen
        bottomBar.selectTabAtPosition(2, false);
    }

    private void createLocationList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        locationListView.setLayoutManager(linearLayoutManager);

        //TODO: Liste muss mit Serverdaten gefüllt werden
//        List<Location> locationList = new ArrayList<>();
        LocationAdapter locationAdapter = new LocationAdapter(locationList);
        locationListView.setAdapter(locationAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);

    }
}
