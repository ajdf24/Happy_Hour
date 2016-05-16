package it.rieger.happyhour.util;

/**
 * Created by sebastian on 21.04.16.
 */
public interface AppConstants {

    String konst = "Das ist eine Konstante";

    String BUNDLE_CONTEXT_LOCATIONS = "Locations";
    String BUNDLE_CONTEXT_LOCATION = "Location";

    interface PermissionsIDs {
        int PERMISSION_ID_FOR_ACCESS_LOCATION = 1;
    }

    public interface SharedPreferencesKeys {
        String FIRST_START = "First Start";
    }
}
