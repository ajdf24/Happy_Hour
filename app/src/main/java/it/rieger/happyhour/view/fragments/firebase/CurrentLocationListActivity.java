package it.rieger.happyhour.view.fragments.firebase;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import it.rieger.happyhour.R;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.view.FavoriteLocations;
import it.rieger.happyhour.view.Maps;

/**
 * List all locations for the current city
 * Created by sebastian on 11.12.16.
 */

public class CurrentLocationListActivity extends LocationList {

    String city = "";

    private BottomBar bottomBar;

    private boolean start = true;

    /**
     * {@inheritDoc}
     */
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child(AppConstants.Firebase.LOCATIONS_PATH).orderByChild("cityName").equalTo(city);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<Address> addresses = new ArrayList<>();

        SmartLocation.with(this).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(android.location.Location location) {
                try {
                    Geocoder geocoder;
                    geocoder = new Geocoder(CurrentLocationListActivity.this, Locale.getDefault());

                    addresses.addAll(geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    city = addresses.get(0).getLocality();

                    cityLoaded();

                } catch (NullPointerException e) {
                    Log.w("Log", "Can not load current position");
                    Toast.makeText(CurrentLocationListActivity.this, R.string.general_can_not_locate, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.w("Log", "Can not load current position");
                    Toast.makeText(CurrentLocationListActivity.this, R.string.general_can_not_locate, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        createBottomBar(savedInstanceState);
    }

    /**
     * create the bottom bar for this activity
     * @param savedInstanceState the last activity state
     */
    private void createBottomBar(Bundle savedInstanceState){
        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (!start) {
                    if (menuItemId == R.id.bottomBarItemTwo) {
                        startActivity(new Intent(CurrentLocationListActivity.this, FavoriteLocations.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                    if (menuItemId == R.id.bottomBarItemOne) {
                        startActivity(new Intent(CurrentLocationListActivity.this, Maps.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                }else{
                    start = false;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, 0xFF5D4037);

        bottomBar.selectTabAtPosition(2, false);

    }

    /**
     * {@inheritDoc}
     * @param outState the activity state which should be saved
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        bottomBar.onSaveInstanceState(outState);
    }

}
