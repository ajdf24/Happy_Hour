package it.rieger.happyhour.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.adapter.LocationAdapter;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.User;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.controller.callbacks.LocationLoadedCallback;
import it.rieger.happyhour.util.listener.ValueEventListener;
import it.rieger.happyhour.view.fragments.firebase.CurrentLocationListActivity;

/**
 * activity class which shows favorite locations of the user
 */
public class FavoriteLocations extends AppCompatActivity implements LocationLoadedCallback {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.activity_favorite_location_recycler_view)
    RecyclerView locationListView;

    @Bind(R.id.activity_favorite_location_progressBar)
    ProgressBar progressBar;

    @Bind(R.id.activtiy_favorite_no_favorites_textView)
    TextView noLocations;

    List<Location> locationList = new ArrayList<>();

    List<Location> searchBackupLocationList = new ArrayList<>();

    private BottomBar bottomBar;

    LocationAdapter locationAdapter = null;

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


        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child(AppConstants.Firebase.USERS_PATH).orderByChild("uID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnap : dataSnapshot.getChildren()){
                    User user = userSnap.getValue(User.class);

                    if(user.getLikedLocations().size() == 0){
                        progressBar.setVisibility(View.INVISIBLE);
                        noLocations.setVisibility(View.VISIBLE);
                    }

                    for (String locationKey : user.getLikedLocations()){
                        database.child(AppConstants.Firebase.LOCATIONS_PATH).orderByChild("id").equalTo(locationKey).addListenerForSingleValueEvent(new ValueEventListener() {

                            /**
                             * {@inheritDoc}
                             */
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    createLocationList();

                                    locationLoaded();

                                    locationList.add(dataSnapshot1.getValue(Location.class));

                                    locationAdapter.notifyDataSetChanged();
                                }
                            }

                        });

                    }

                }
            }

        });

        createBottomBar(savedInstanceState);


    }

    /**
     * create the list of the locations
     */
    private void createLocationList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        locationListView.setLayoutManager(linearLayoutManager);

        locationAdapter = new LocationAdapter(locationList);
        locationListView.setAdapter(locationAdapter);

        searchBackupLocationList.clear();

        for (Location location : locationList){
            searchBackupLocationList.add(location);
        }

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
                            startActivity(new Intent(FavoriteLocations.this, Maps.class).
                                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            break;
                        case R.id.bottomBarItemThree:
                            startActivity(new Intent(FavoriteLocations.this, CurrentLocationListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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

    @Override
    public void locationLoaded() {

        progressBar.setVisibility(View.INVISIBLE);
        locationListView.setVisibility(View.VISIBLE);
        createLocationList();
    }
}
