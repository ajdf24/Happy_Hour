package it.rieger.happyhour.controller.backend;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.User;
import it.rieger.happyhour.util.AppConstants;

/**
 * Created by sebastian on 28.06.16.
 * <p>
 * This class connects the app to the backend database.
 * Contains different Methods to write data to the database.
 * <p>
 * This class is a singleton.
 * <p>
 * This class is fully thread save and can called from everywhere.
 */
public enum BackendDatabase {

    INSTANCE;

    /**
     * @return the instance of the backend database
     */
    public static BackendDatabase getInstance() {
        return INSTANCE;
    }

    /**
     * remove image from location
     * @param location current location
     * @param imageKey the imagekey
     */
    public void removeImage(Location location, String imageKey){
        location.getImageKeyList().remove(imageKey);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("images/" + imageKey).removeValue();

        saveLocation(location);
    }

    /**
     * Save a location in the backend database
     *
     * @param location the location which should be saved
     */
    public void saveLocation(Location location) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(location.getId() == null || location.getId().isEmpty()){
            String key = mDatabase.child(AppConstants.Firebase.LOCATIONS_PATH).push().getKey();
            location.setId(key);
        }

        Map<String, Object> postValues = location.toMap();

        Map<String, Object> childUpdates = new HashMap<String, Object>();

        childUpdates.put(AppConstants.Firebase.LOCATIONS_CHILDS_PATH + location.getId(), postValues);
        mDatabase.updateChildren(childUpdates);
    }

    /**
     * save a user in the backend database
     * @param user the user which should be saved
     */
    public void saveUser(User user){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        if(user.getId() == null || user.getId().isEmpty()){
            String key = database.child(AppConstants.Firebase.USERS_PATH).push().getKey();
            user.setId(key);
        }

        Map<String, Object> postValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<String, Object>();

        childUpdates.put(AppConstants.Firebase.USERS_CHILDS_PATH + user.getId(), postValues);
        database.updateChildren(childUpdates);
    }

    /**
     * save a image for a location in the database
     * @param image the image
     * @param location the location on which the image should be saved
     * @return the database key for the new image
     */
    public String saveImage(Image image, Location location){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = null;

        if (location != null){
            key =mDatabase.child(AppConstants.Firebase.IMAGES_PATH).push().getKey();
        }

        Map<String, Object> postValues = image.toMap();
        Map<String, Object> childUpdates = new HashMap<String, Object>();

        childUpdates.put(AppConstants.Firebase.IMAGES_CHILDS_PATH + key, postValues);

        location.setImageKey(key);

        Map<String, Object> locationValues = location.toMap();
        Map<String, Object> childLocationUpdates = new HashMap<String, Object>();

        childLocationUpdates.put(AppConstants.Firebase.LOCATIONS_CHILDS_PATH + location.getId(), locationValues);

        mDatabase.updateChildren(childUpdates);
        mDatabase.updateChildren(childLocationUpdates);

        return key;
    }

}
