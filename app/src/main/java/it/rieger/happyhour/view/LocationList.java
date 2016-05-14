package it.rieger.happyhour.view;

import android.content.Intent;
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
import it.rieger.happyhour.model.Location;

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
        List<Location> locationList = new ArrayList<>();
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
