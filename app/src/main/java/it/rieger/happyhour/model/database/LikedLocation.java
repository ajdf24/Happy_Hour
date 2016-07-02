package it.rieger.happyhour.model.database;

/**
 * Created by Admin on 02.07.2016.
 */
public class LikedLocation {

    private long id;

    private long LocationID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLocationID() {
        return LocationID;
    }

    public void setLocationID(long locationID) {
        LocationID = locationID;
    }
}
