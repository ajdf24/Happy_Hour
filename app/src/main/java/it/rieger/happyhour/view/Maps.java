package it.rieger.happyhour.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;

import it.rieger.happyhour.R;
import it.rieger.happyhour.util.AppConstants;

/**
 * This activity shows a google map, with all locations which are in the intent.
 *
 * This class can view {@link it.rieger.happyhour.model.Location} which are in a ArrayList with the key {@link AppConstants#BUNDLE_CONTEXT_LOCATIONS}
 */
public class Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private BottomBar bottomBar;

    private SupportMapFragment mapFragment;

    private ArrayList<it.rieger.happyhour.model.Location> locations;

    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadLocationsFromBundle();

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
                    if (menuItemId == R.id.bottomBarItemOne) {

                    }
                    if (menuItemId == R.id.bottomBarItemTwo) {

                    }
                    if (menuItemId == R.id.bottomBarItemThree) {
                        startActivity(new Intent(Maps.this, LocationList.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        bottomBar.selectTabAtPosition(0, false);
    }

    /**
     * Load all locations from the intent
     */
    private void loadLocationsFromBundle(){
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            locations =(ArrayList<it.rieger.happyhour.model.Location>) bundle.getSerializable(AppConstants.BUNDLE_CONTEXT_LOCATIONS);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Get Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_ACCESS_LOCATION);
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);


        if(provider != null) {
            Location myLocation = locationManager.getLastKnownLocation(provider);

            //set map type
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            // Get latitude of the current location
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        //TODO: C1 Marker entfernen Locations laden
        googleMap.addMarker(new MarkerOptions().position(new LatLng(50.962497, 11.018132)).title("Clubeins")).showInfoWindow();

        // Activate Callback
        googleMap.setOnMarkerClickListener(this);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.getTitle().toString().equals("Clubeins"))
        {
            //TODO: show Location informations
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_ACCESS_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Snackbar snackbar = Snackbar
                            .make(mapFragment.getView(), R.string.message_permission_location_denied, Snackbar.LENGTH_LONG);

                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView)snackbarView .findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                    return;
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);

    }
}
