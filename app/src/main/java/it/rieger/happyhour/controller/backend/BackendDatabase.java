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
 *
 * This class connects the app to the backend database.
 * Contains different Methods to load and write data to/ from the database.
 *
 * This class is a singleton and load data lazy. This means the data is only loaded when a new request is incoming.
 *
 * This class is fully thread save and can called from everywhere.
 *
 */
public enum BackendDatabase {

    INSTANCE;

    private List<Location> locationList = new ArrayList<>();

    private LatLng oldCoordinates;

    private float oldRadius;

    /**
     *
     * @return the instance of the backend database
     */
    public static BackendDatabase getInstance()
    {
        return INSTANCE;
    }

    /**
     * Load locations from the backend database.
     *
     * This method loads the specific locations to the given list.
     *
     * @param locationList the list in which the locations are loaded
     * @param context the called context
     * @param coordinates the current coordinates
     * @param radius the search radius in kilometer
     */
    public synchronized void loadLocations(@NonNull List<Location> locationList, @NonNull Context context, @NonNull LatLng coordinates, float radius){

        if(!(context instanceof LocationLoadedCallback)){
            throw new RuntimeException(context.toString()
                    + " must implement LocationLoadedCallback");
        }

        //TODO: Remove mocked Location with a real database connection.
        if(!oldCoordinates.equals(coordinates) || radius != oldRadius) {

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

            HappyHourTime happyHourTime = new HappyHourTime(times);

            HappyHour happyHour = new HappyHour("Cuba Libre Doppeldecker", "5€", happyHourTime);
            List<HappyHour> happyHours = new ArrayList<>();
            happyHours.add(happyHour);

            BitmapLRUCache.getInstance().addBitmapToMemoryCache("C1", BitmapFactory.decodeResource(context.getResources(), R.mipmap.c1));


            List<String> imageKeys = new ArrayList<>();
            imageKeys.add("C1");

            OpeningTimes openingTimes = new OpeningTimes(times);
            Location location = new Location("Clubeins", 4.3f, "Steigerstraße 18", 11.0181322f, 50.9624967f, openingTimes, happyHours, imageKeys);

            locationList.add(location);
            this.locationList.add(location);
            oldCoordinates = coordinates;

            ((LocationLoadedCallback) context).locationLoaded();
        }else {
            locationList = this.locationList;
            ((LocationLoadedCallback) context).locationLoaded();
        }

    }
}
