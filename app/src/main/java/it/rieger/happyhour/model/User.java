package it.rieger.happyhour.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * model class for a user
 * Created by sebastian on 26.02.17.
 */

public class User {

    private String id;

    private String uID;

    private List<String> likedLocations = new ArrayList<>();

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public List<String> getLikedLocations() {
        return likedLocations;
    }

    public void setLikedLocations(List<String> likedLocations) {
        this.likedLocations = likedLocations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("uID", uID);
        result.put("likedLocations", likedLocations);

        return result;
    }
}
