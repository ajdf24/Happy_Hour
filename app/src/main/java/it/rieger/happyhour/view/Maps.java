package it.rieger.happyhour.view;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.callbacks.LocationLoadedCallback;
import it.rieger.happyhour.util.listener.AnimationListener;
import it.rieger.happyhour.view.fragments.LocationInformation;

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

    private List<it.rieger.happyhour.model.Location> locations;

    private boolean start = true;

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
        mapFragment.getMap().setOnMapClickListener(this);

        loadLocations();

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
                        startActivity(new Intent(Maps.this, FavoriteLocations.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                    if (menuItemId == R.id.bottomBarItemThree) {
                        startActivity(new Intent(Maps.this, LocationList.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                }else{
                    start = false;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    focusMapToCurrentPosition(mapFragment.getMap());
                }
            }
        });

        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        bottomBar.mapColorForTab(1, 0xFF5D4037);
        bottomBar.mapColorForTab(2, 0xFF5D4037);

        bottomBar.selectTabAtPosition(0, false);

        mapFragment.getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }
        });
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
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                Criteria criteria = new Criteria();

                String provider = locationManager.getBestProvider(criteria, true);
                try {
                    Location myLocation = locationManager.getLastKnownLocation(provider);

                    LatLng currentPosition = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    BackendDatabase.getInstance().loadLocations(locations, this, currentPosition, 10);

                } catch (NullPointerException e) {
                    Log.w(LOG_TAG, "Can not load current position");
                    BackendDatabase.getInstance().loadLocations(locations, this, new LatLng(0.0, 0.0), 10);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * set map to the current position and add marker
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

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
        fragmentTransaction.add(R.id.fragment_container, information, AppConstants.FragmentTags.FRAGMENT_LOCATION_INFORMATION);
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
     * @param uri the uri for interaction
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onMapClick(LatLng latLng) {
        removeInfoFragment();
    }

    @Override
    public void locationLoaded() {
        onMapReady(mapFragment.getMap());
    }
}
