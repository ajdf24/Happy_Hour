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
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.adapter.LocationAdapter;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.callbacks.LocationLoadedCallback;
import it.rieger.happyhour.util.listener.OnQueryTextListener;

/**
 * activity which shows a list of locations
 * @deprecated
 */
public class LocationList {


}
