package it.rieger.happyhour.view.fragments.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import it.rieger.happyhour.util.AppConstants;

/**
 * Created by sebastian on 11.12.16.
 */

public class CurrentLocationListActivity extends LocationListFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child(AppConstants.Firebase.LOCATIONS_PATH).orderByChild("cityName").equalTo("Erfurt");
    }
}
