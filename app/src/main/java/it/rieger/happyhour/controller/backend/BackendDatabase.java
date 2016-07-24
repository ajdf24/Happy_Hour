package it.rieger.happyhour.controller.backend;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.cache.BitmapLRUCache;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.HappyHourTime;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.OpeningTimes;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.LocationLoadedCallback;

/**
 * Created by sebastian on 28.06.16.
 * <p>
 * This class connects the app to the backend database.
 * Contains different Methods to load and write data to/ from the database.
 * <p>
 * This class is a singleton and load data lazy. This means the data is only loaded when a new request is incoming.
 * <p>
 * This class is fully thread save and can called from everywhere.
 */
public enum BackendDatabase {

    INSTANCE;

    private List<Location> locationList = new ArrayList<>();

    private LatLng oldCoordinates = new LatLng(0.0, 0.0);

    private float oldRadius;

    /**
     * @return the instance of the backend database
     */
    public static BackendDatabase getInstance() {
        return INSTANCE;
    }

    /**
     * Load locations from the backend database.
     * <p>
     * This method loads the specific locations to the given list.
     *
     * @param locationList the list in which the locations are loaded
     * @param context      the called context
     * @param coordinates  the current coordinates
     * @param radius       the search radius in kilometer
     */
    public synchronized void loadLocations(@NonNull List<Location> locationList, @NonNull Context context, @NonNull LatLng coordinates, float radius) {

        if (!(context instanceof LocationLoadedCallback)) {
            throw new RuntimeException(context.toString()
                    + " must implement LocationLoadedCallback");
        }

        //TODO: Remove mocked Location with a real database connection.
        if (!oldCoordinates.equals(coordinates) || radius != oldRadius) {

            locationList.clear();
            Time timefriday = new Time();
            timefriday.setDay(Day.FRIDAY);
            timefriday.setStartTime("23:00");
            timefriday.setEndTime("05:00");
            Time timesaturday = new Time();
            timesaturday.setDay(Day.SATURDAY);
            timesaturday.setStartTime("23:00");
            timesaturday.setEndTime("05:00");

            List<Time> times = new ArrayList<>();
            times.add(timefriday);
            times.add(timesaturday);

            HappyHour happyHour = new HappyHour("Cuba Libre Doppeldecker", "5€", timefriday);
            List<HappyHour> happyHours = new ArrayList<>();
            happyHours.add(happyHour);

            BitmapLRUCache.getInstance().addBitmapToMemoryCache("C1", BitmapFactory.decodeResource(context.getResources(), R.mipmap.c1));


            List<String> imageKeys = new ArrayList<>();
            imageKeys.add("http://www.eventsofa.de/venue-images/534/ef0/534ef027b7605368076c4eeb-7262.jpg");
            imageKeys.add("http://www.afterworkclub-erfurt.de/wp-content/uploads/2014/11/IMG_6385-705x476.jpg");
            imageKeys.add("https://www.blitz-world.de/magazin/archiv/2014/1405/pix/t-club-clubeins2.jpg");

            OpeningTimes openingTimes = new OpeningTimes(times);
            Location location = new Location("Clubeins", 4.3f, "Steigerstraße 18", 11.0181322f, 50.9624967f, openingTimes, happyHours, imageKeys);
            location.setId(1);

            locationList.add(location);
            this.locationList.add(location);
            oldCoordinates = coordinates;

            ((LocationLoadedCallback) context).locationLoaded();
        } else {
            locationList = this.locationList;
            ((LocationLoadedCallback) context).locationLoaded();
        }

    }

    public synchronized void loadFavorites(@NonNull List<Location> locationList, @NonNull Context context) {
        if (!(context instanceof LocationLoadedCallback)) {
            throw new RuntimeException(context.toString()
                    + " must implement LocationLoadedCallback");
        }

        //TODO: Remove mocked Location with a real database connection.

        locationList.clear();
        Time timefriday = new Time();
        timefriday.setDay(Day.FRIDAY);
        timefriday.setStartTime("23:00");
        timefriday.setEndTime("05:00");
        Time timesaturday = new Time();
        timesaturday.setDay(Day.SATURDAY);
        timesaturday.setStartTime("23:00");
        timesaturday.setEndTime("05:00");

        List<Time> times = new ArrayList<>();
        times.add(timefriday);
        times.add(timesaturday);

        HappyHour happyHour = new HappyHour("Cuba Libre Doppeldecker", "5€", timefriday);
        HappyHour happyHour4 = new HappyHour("Cuba Libre Doppeldecker", "5€", timefriday);
        HappyHour happyHour2 = new HappyHour("Rum Cola Doppeldecker", "5€", timefriday);
        HappyHour happyHour3 = new HappyHour("Bier Doppeldecker", "5€", timefriday);
        List<HappyHour> happyHours = new ArrayList<>();
        happyHours.add(happyHour);
        happyHours.add(happyHour2);
        happyHours.add(happyHour3);
        happyHours.add(happyHour4);

        BitmapLRUCache.getInstance().addBitmapToMemoryCache("C1", BitmapFactory.decodeResource(context.getResources(), R.mipmap.c1));


        List<String> imageKeys = new ArrayList<>();
        imageKeys.add("C1");

        OpeningTimes openingTimes = new OpeningTimes(times);
        Location location = new Location("Clubeins", 4.3f, "Steigerstraße 18", 11.0181322f, 50.9624967f, openingTimes, happyHours, imageKeys);
        location.setId(1);

        locationList.add(location);
        this.locationList.add(location);

        ((LocationLoadedCallback) context).locationLoaded();

    }

    /**
     * Get a new id from the backend database
     *
     * @return the new id
     */
    public int getNewBackendId() {
        //TODO: Add database connection
        return 0;
    }

    /**
     * Save a location in the backend database
     *
     * @param location the location which should be saved
     */
    public void saveLocation(Location location) {
        //TODO: implement
    }
}
