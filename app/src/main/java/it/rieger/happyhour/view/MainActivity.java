package it.rieger.happyhour.view;


import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.util.AppConstants;

/**
 * Macht bisher nix und wird denke ich nicht mehr benötigt!
 * @deprecated
 */
public class MainActivity extends AppCompatActivity {


    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //TODO implementieren der Bottombar
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                }
                if (menuItemId == R.id.bottomBarItemTwo){
                    startActivity(new Intent(MainActivity.this, LocationList.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra(AppConstants.BUNDLE_LOAD_FAVOTITE_LOCATIONS,true));
                }
                if (menuItemId == R.id.bottomBarItemThree) {

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
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);

    }

}

