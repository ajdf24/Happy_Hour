package it.rieger.happyhour.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import butterknife.Bind;
import it.rieger.happyhour.R;
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
        HappyHoursFragment.OnFragmentInteractionListener{


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

        if(location == null){
            location = new Location();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        drawer.openDrawer(Gravity.LEFT);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final GeneralFragment generalFragment = GeneralFragment.newInstance(location);
        fragmentTransaction.add(R.id.fragment_container, generalFragment, AppConstants.FragmentTags.FRAGMENT_CHANGE_LOCATION_GENERAL);
        fragmentTransaction.commit();
        currentFragment = generalFragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null ? drawer.isDrawerOpen(GravityCompat.START) : false) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_camera) {
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final CameraFragment cameraFragment = CameraFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, cameraFragment, AppConstants.FragmentTags.FRAGMENT_CHANGE_LOCATION_CAMERA);
            fragmentTransaction.commit();
            currentFragment = cameraFragment;
        } else if (id == R.id.nav_general) {
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final GeneralFragment generalFragment = GeneralFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, generalFragment, AppConstants.FragmentTags.FRAGMENT_CHANGE_LOCATION_GENERAL);
            fragmentTransaction.commit();
            currentFragment = generalFragment;
        } else if (id == R.id.nav_open) {
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final OpeningFragment openingFragment = OpeningFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, openingFragment, AppConstants.FragmentTags.FRAGMENT_CHANGE_LOCATION_OPENING);
            fragmentTransaction.commit();
            currentFragment = openingFragment;
        } else if (id == R.id.nav_share) {

            ShareDialog shareDialog = new ShareDialog(this);
            if (ShareDialog.canShow(ShareLinkContent.class)) {

                //TODO: Change with real Share Data
                ShareContent shareContent = new ShareLinkContent.Builder()
                        .setContentDescription("TEst")
                        .build();

                shareDialog.show(shareContent);  // Show facebook ShareDialog

                Toast.makeText(this, R.string.general_not_implemented,Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_send) {
                //TODO: Implement and send a real Backend Link
                Toast.makeText(this, R.string.general_not_implemented,Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_happy_hour){
            if(currentFragment != null)
                fragmentTransaction.remove(currentFragment);
            final HappyHoursFragment happyHoursFragment = HappyHoursFragment.newInstance(location);
            fragmentTransaction.add(R.id.fragment_container, happyHoursFragment, AppConstants.FragmentTags.FRAGMENT_CHANGE_LOCATION_Happy_HOURS);
            fragmentTransaction.commit();
            currentFragment = happyHoursFragment;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
