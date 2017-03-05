package it.rieger.happyhour.view;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import it.rieger.happyhour.controller.callbacks.LocationLoadedCallback;
import it.rieger.happyhour.util.listener.AnimationListener;
import it.rieger.happyhour.util.listener.ValueEventListener;
import it.rieger.happyhour.view.fragments.LocationInformation;
import it.rieger.happyhour.view.fragments.firebase.CurrentLocationListActivity;

/**
 * This activity shows a google map, with all locations which are in the intent.
 *
 * This class can view {@link it.rieger.happyhour.model.Location} which are in a ArrayList with the key {@link AppConstants#BUNDLE_CONTEXT_LOCATIONS}
 */
public class Maps extends FragmentActivity implements OnMapReadyCallback,
                                                        GoogleMap.OnMarkerClickListener,
                                                        GoogleMap.OnMapClickListener,
                                                        LocationInformation.OnFragmentInteractionListener,
                                                        LocationLoadedCallback{

    private final String LOG_TAG = getClass().getSimpleName();

    private BottomBar bottomBar;

    private SupportMapFragment mapFragment;

    private FloatingActionButton addNewLocation;

    private List<it.rieger.happyhour.model.Location> locations;

    private boolean start = true;

    GoogleMap googleMap = null;

    String city = "";

    /**
     * {@inheritDoc}
     * load the ui
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        addNewLocation = (FloatingActionButton) findViewById(R.id.activity_maps_new_location);

        loadLocations();

        createBottomBar(savedInstanceState);

        initializeActiveElements();

    }

    /**
     * initialize the active elements
     */
    private void initializeActiveElements(){
        addNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Maps.this, ChangeLocationActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
    }

    /**
     * create the bottom bar for this activity
     * @param savedInstanceState the last activity state
     */
    private void createBottomBar(Bundle savedInstanceState){
        bottomBar = BottomBar.attach(this, savedInstanceState);

        bottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (!start) {
                    if (menuItemId == R.id.bottomBarItemTwo) {
                        startActivity(new Intent(Maps.this, FavoriteLocations.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                    if (menuItemId == R.id.bottomBarItemThree) {
                        startActivity(new Intent(Maps.this, CurrentLocationListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                }else{
                    start = false;
                }
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
            }
        });

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, 0xFF5D4037);

        bottomBar.selectTabAtPosition(0, false);


    }

    /**
     * Load all locations
     */
    private void loadLocations(){
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            locations =(List<it.rieger.happyhour.model.Location>) bundle.getSerializable(AppConstants.BUNDLE_CONTEXT_LOCATIONS);
        }

        if(locations == null){
            locations = new ArrayList<>();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        AppConstants.PermissionsIDs.PERMISSION_ID_FOR_ACCESS_LOCATION);
            }else {

                final List<Address> addresses = new ArrayList<>();

                SmartLocation.with(this).location().oneFix().start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(android.location.Location location) {
                        try {
                            Geocoder geocoder;
                            geocoder = new Geocoder(Maps.this, Locale.getDefault());

                            addresses.addAll(geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1)); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            city = addresses.get(0).getLocality();

                            cityLoaded();

                        } catch (NullPointerException e) {
                            Log.w("Log", "Can not load current position");
                            Toast.makeText(Maps.this, R.string.general_can_not_locate, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.w("Log", "Can not load current position");
                            Toast.makeText(Maps.this, R.string.general_can_not_locate, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }

    /**
     * callback method which is called when the current city is loaded
     */
    private void cityLoaded(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(AppConstants.Firebase.LOCATIONS_PATH).orderByChild("cityName").equalTo(city).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot locationSnap : dataSnapshot.getChildren()){

                    it.rieger.happyhour.model.Location location = locationSnap.getValue(it.rieger.happyhour.model.Location.class);
                    locations.add(location);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getAddressLatitude(), location.getAddressLongitude())).title(location.getName())).showInfoWindow();
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     * set map to the current position and add marker
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        focusMapToCurrentPosition(googleMap);

        if(locations != null) {
            for (it.rieger.happyhour.model.Location location : locations) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getAddressLatitude(), location.getAddressLongitude())).title(location.getName())).showInfoWindow();
            }
        }

        googleMap.setOnMarkerClickListener(this);

    }

    private void focusMapToCurrentPosition(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_ACCESS_LOCATION);
        }else {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);

            try {
                Location myLocation = locationManager.getLastKnownLocation(provider);
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            } catch (NullPointerException e) {
                Log.w(LOG_TAG, "Can not load current position");
            }
        }
    }

    /**
     * show the information for the marker when it is clicked
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        if(locations != null) {
            for (final it.rieger.happyhour.model.Location location : locations) {
                if (marker.getTitle().toString().equals(location.getName())) {
                    final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.fragment_container);

                    if(relativeLayout.getVisibility() == View.VISIBLE){
                        Animation slideUp = AnimationUtils.loadAnimation(Maps.this, R.anim.slide_up);
                        relativeLayout.startAnimation(slideUp);
                        slideUp.setAnimationListener(new AnimationListener() {

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                showInfoFragment(relativeLayout, location);
                            }
                        });
                    }else{
                        showInfoFragment(relativeLayout, location);
                    }
                }
            }
        }

        return false;
    }

    /**
     * This method opens the info fragment for a specific location
     * @param relativeLayout
     * @param location the location which info are should be shown
     */
    private void showInfoFragment(RelativeLayout relativeLayout, it.rieger.happyhour.model.Location location){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final LocationInformation information = LocationInformation.newInstance(location);
        fragmentTransaction.replace(R.id.fragment_container, information, AppConstants.FragmentTags.FRAGMENT_LOCATION_INFORMATION);
        fragmentTransaction.commit();

        Animation slideDown = AnimationUtils.loadAnimation(Maps.this, R.anim.slide_down);
        relativeLayout.startAnimation(slideDown);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * This method removes the info fragment
     */
    private void removeInfoFragment(){
        final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.fragment_container);

        Animation slideUp = AnimationUtils.loadAnimation(Maps.this, R.anim.slide_up);
        relativeLayout.startAnimation(slideUp);
        relativeLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * {@inheritDoc}
     * @param requestCode the request code
     * @param permissions the permissions
     * @param grantResults the granted results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0) {


            switch (requestCode) {
                case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_ACCESS_LOCATION:
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        Snackbar snackbar = Snackbar
                                .make(mapFragment.getView(), R.string.message_permission_location_denied, Snackbar.LENGTH_LONG);

                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                        return;
                    }
                    break;
            }
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMapClick(LatLng latLng) {
        removeInfoFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void locationLoaded() {

    }
}
