package it.rieger.happyhour.util;

/**
 * Interface for app constants
 * Created by sebastian on 21.04.16.
 */
public interface AppConstants {

    String BUNDLE_CONTEXT_LOCATIONS = "Locations";
    String BUNDLE_CONTEXT_LOCATION = "Location";
    String BUNDLE_LOAD_FAVORITE_LOCATIONS = "Favorite_Locations";
    String BUNDLE_CONTEXT_POSITION = "position";

    /**
     * interface for permission constants
     */
    interface PermissionsIDs {
        int PERMISSION_ID_FOR_ACCESS_LOCATION = 1;
        int PERMISSION_ID_FOR_WRITE_STORAGE = 2;
    }

    /**
     * interface for shared pref constants
     */
    interface SharedPreferencesKeys {
        String FIRST_START = "First Start";
    }

    interface FacebookPermissions {
        String PUBLISH_ACTIONS= "publish_actions";
    }

    /**
     * interface for fragment tags
     */
    interface FragmentTags {
        String FRAGMENT_SLIDE_SHOW = "SlideShowFragment";
        String FRAGMENT_IMAGE_DIALOG = "Image Dialog";
        String FRAGMENT_LOCATION_INFORMATION = "Welcome";
        String FRAGMENT_CHANGE_LOCATION_GENERAL = "Change Location General";
        String FRAGMENT_CHANGE_LOCATION_CAMERA = "Change Location Camera";
        String FRAGMENT_CHANGE_LOCATION_OPENING = "Change Location Opening";
        String FRAGMENT_CHANGE_LOCATION_Happy_HOURS = "Change Location Happy Hours";
        String FRAGMENT_TIME_PICKER_MONDAY = "Timepickerdialog Monday";
        String FRAGMENT_TIME_PICKER_TUESDAY = "Timepickerdialog Tuesday";
        String FRAGMENT_TIME_PICKER_WEDNESDAY = "Timepickerdialog Wednesday";
        String FRAGMENT_TIME_PICKER_THURSDAY = "Timepickerdialog Thursday";
        String FRAGMENT_TIME_PICKER_FRIDAY = "Timepickerdialog Friday";
        String FRAGMENT_TIME_PICKER_SATURDAY = "Timepickerdialog Saturday";
        String FRAGMENT_TIME_PICKER_SUNDAY = "Timepickerdialog Sunday";
    }

    /**
     * interface for firebase paths
     */
    interface Firebase {
        String LOCATIONS_PATH = "locations";
        String LOCATIONS_CHILDS_PATH = "/locations/";
        String IMAGES_PATH = "images";
        String IMAGES_CHILDS_PATH = "/images/";
        String USERS_PATH = "users";
        String USERS_CHILDS_PATH = "/users/";
    }
}
