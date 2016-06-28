package it.rieger.happyhour.view;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import it.rieger.happyhour.R;

public class BottombarActivity extends AppCompatActivity {

    private BottomBar bottomBar;

    private boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);

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
                        startActivity(new Intent(BottombarActivity.this, Maps.class).
                                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                    if (menuItemId == R.id.bottomBarItemTwo) {

                    }
                    if (menuItemId == R.id.bottomBarItemThree) {
                        startActivity(new Intent(BottombarActivity.this, LocationList.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        bottomBar.mapColorForTab(2, 0xFF5D4037);

        //Aktiven Button setzen
        bottomBar.selectTabAtPosition(0, false);

    }

    /**
     * {@inheritDoc}
     * @param outState the activity state which should be saved
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);

    }
}
