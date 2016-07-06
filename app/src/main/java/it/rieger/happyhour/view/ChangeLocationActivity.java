package it.rieger.happyhour.view;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;

import butterknife.Bind;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.cache.BitmapLRUCache;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.view.fragments.changelocation.CameraFragment;
import it.rieger.happyhour.view.fragments.changelocation.GeneralFragment;
import it.rieger.happyhour.view.fragments.changelocation.HappyHoursFragment;
import it.rieger.happyhour.view.fragments.changelocation.OpeningFragment;

public class ChangeLocationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GeneralFragment.OnFragmentInteractionListener,
        OpeningFragment.OnFragmentInteractionListener,
        CameraFragment.OnFragmentInteractionListener,
        HappyHoursFragment.OnFragmentInteractionListener,
        GraphRequest.GraphJSONArrayCallback{

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    Fragment currentFragment;

    Location location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawe_activity);
        setSupportActionBar(toolbar);

        location = (Location) getIntent().getSerializableExtra(AppConstants.BUNDLE_CONTEXT_LOCATION);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final GeneralFragment generalFragment = GeneralFragment.newInstance(location);
        fragmentTransaction.add(R.id.fragment_container, generalFragment, "GeneralFragment");
        fragmentTransaction.commit();
        currentFragment = generalFragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final CameraFragment cameraFragment = CameraFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, cameraFragment, "CameraFragment");
            fragmentTransaction.commit();
            currentFragment = cameraFragment;
        } else if (id == R.id.nav_general) {
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final GeneralFragment generalFragment = GeneralFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, generalFragment, "GeneralFragment");
            fragmentTransaction.commit();
            currentFragment = generalFragment;
        } else if (id == R.id.nav_open) {
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final OpeningFragment openingFragment = OpeningFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, openingFragment, "OpeningFragment");
            fragmentTransaction.commit();
            currentFragment = openingFragment;
        } else if (id == R.id.nav_share) {

            ShareDialog shareDialog = new ShareDialog(this);
            if (ShareDialog.canShow(ShareLinkContent.class)) {

                ShareContent shareContent = new ShareLinkContent.Builder()
                        .setContentDescription("TEst")
                        .build();

                shareDialog.show(shareContent);  // Show facebook ShareDialog
            }
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_happy_hour){
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final HappyHoursFragment happyHoursFragment = HappyHoursFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, happyHoursFragment, "happyHoursFragment");
            fragmentTransaction.commit();
            currentFragment = happyHoursFragment;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCompleted(JSONArray objects, GraphResponse response) {
        System.out.println("TEST");
        System.out.println(objects);
    }
}
