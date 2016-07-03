package it.rieger.happyhour.util;

/**
 * Interface for app constants
 * Created by sebastian on 21.04.16.
 */
public interface AppConstants {

    String BUNDLE_CONTEXT_LOCATIONS = "Locations";
    String BUNDLE_CONTEXT_LOCATION = "Location";
    String BUNDLE_LOAD_FAVOTITE_LOCATIONS = "Favorite_Locations";

    /**
     * interface for permission constants
     */
    interface PermissionsIDs {
        int PERMISSION_ID_FOR_ACCESS_LOCATION = 1;
    }

    /**
     * interface for shared pref constants
     */
    interface SharedPreferencesKeys {
        String FIRST_START = "First Start";
    }
}
