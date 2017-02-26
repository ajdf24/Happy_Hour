package it.rieger.happyhour.view.fragments.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import it.rieger.happyhour.R;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.view.FavoriteLocations;
import it.rieger.happyhour.view.Maps;
import it.rieger.happyhour.view.StartActivity;

/**
 * Created by sebastian on 11.12.16.
 */

public class CurrentLocationListActivity extends LocationListFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        //TODO:
        return databaseReference.child(AppConstants.Firebase.LOCATIONS_PATH).orderByChild("cityName").equalTo("Erfurt");
    }

    private BottomBar bottomBar;

    private boolean start = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                if (menuItemId == R.id.bottomBarItemOne) {
//                    focusMapToCurrentPosition(mapFragment.getMap());
                }
            }
        });

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, 0xFF5D4037);

        bottomBar.selectTabAtPosition(2, false);

//        mapFragment.getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//            }
//        });
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

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//
//        intent.setClass(CurrentLocationListActivity.this, Maps.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(intent);
//    }
}
