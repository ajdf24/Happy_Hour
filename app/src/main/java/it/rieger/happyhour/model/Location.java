package it.rieger.happyhour.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.rieger.happyhour.controller.cache.BitmapLRUCache;
import it.rieger.happyhour.view.viewholder.ThumbnailViewHolder;

/**
 * Model class, which represents a Location, which all the data which are came from the the database.
 *
 * Created by sebastian on 25.04.16.
 */
public class Location implements Serializable{

    /**
     * database id of this location
     */
    private String id;

    /**
     * name of the locaton
     */
    private String name;

    /**
     * rating for the location
     */
    private float rating;

    /**
     * name of the address
     */
    private String addressName;

    private String countryName;

    private String cityName;

    /**
     * longitude of the location
     */
    private float addressLongitude;

    /**
     * latitude of the location
     */
    private float addressLatitude;

    /**
     * times when the location is open
     */
    private OpeningTimes openingTimes = new OpeningTimes();

    /**
     * happy hours of the location
     */
    private List<HappyHour> happyHours = new ArrayList<>();

    /**
     * list of image keys for the location
     */
    private List<String> imageKeyList = new ArrayList<>();

    private List<String> ratedUser = new ArrayList<>();

    public Location() {
    }

    /**
     * constructor
     * @param name name of the location
     * @param rating rating of the location
     * @param addressName address name of the location
     * @param addressLongitude longitude of the address
     * @param addressLatitude latitude of the address
     * @param openingTimes opening times of the location
     * @param happyHours happy hours
     * @param imageKeyList image keys of this location
     */
    public Location(@NonNull String name, float rating, String addressName, float addressLongitude, float addressLatitude, OpeningTimes openingTimes, List<HappyHour> happyHours, List<String> imageKeyList) {
        this.name = name;
        this.rating = rating;
        this.addressName = addressName;
        this.addressLongitude = addressLongitude;
        this.addressLatitude = addressLatitude;
        this.openingTimes = openingTimes;
        this.happyHours = happyHours;
        this.imageKeyList = imageKeyList;
    }

    /**
     * constructor
     * @param name name of the location
     * @param rating rating of the location
     * @param addressName address name of the location
     * @param addressLongitude longitude of the address
     * @param addressLatitude latitude of the address
     * @param openingTimes opening times of the location
     * @param happyHours happy hours
     */
    public Location(@NonNull String name, float rating, @NonNull String addressName, float addressLongitude,  float addressLatitude, OpeningTimes openingTimes, List<HappyHour> happyHours) {
        this(name, rating, addressName, addressLongitude, addressLatitude, openingTimes, null, null);
    }

    /**
     * constructor
     * @param name name of the location
     * @param rating rating of the location
     * @param addressName address name of the location
     * @param addressLongitude longitude of the address
     * @param addressLatitude latitude of the address
     * @param openingTimes opening times of the location
     */
    public Location(@NonNull String name, float rating, @NonNull String addressName, float addressLongitude, float addressLatitude, OpeningTimes openingTimes) {
        this(name, rating, addressName, addressLongitude, addressLatitude, openingTimes, null, null);
    }

    /**
     * constructor
     * @param name name of the location
     * @param rating rating of the location
     * @param addressName address name of the location
     * @param addressLongitude longitude of the address
     * @param addressLatitude latitude of the address
     */
    public Location(@NonNull String name, float rating, String addressName, float addressLongitude, float addressLatitude) {
        this(name, rating, addressName, addressLongitude, addressLatitude, null, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(@NonNull String addressName) {
        this.addressName = addressName;
    }

    public float getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(float addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public float getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(float addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public OpeningTimes getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(OpeningTimes openingTimes) {
        this.openingTimes = openingTimes;
    }

    public List<HappyHour> getHappyHours() {
        return happyHours;
    }

    public void setHappyHours(List<HappyHour> happyHours) {
        this.happyHours = happyHours;
    }

    public List<String> getImageKeyList() {
        return imageKeyList;
    }

    public void setImageKeyList(List<String> imageKeyList) {
        this.imageKeyList = imageKeyList;
    }

    public void setImageKey(String key){
        imageKeyList.add(key);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Time getTodaysOpeningTime(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Time today = null;

        switch (day) {
            case Calendar.SUNDAY:
                today = getTimeForDay(Day.SUNDAY);
                break;
            case Calendar.MONDAY:
                today = getTimeForDay(Day.MONDAY);
                break;
            case Calendar.TUESDAY:
                today = getTimeForDay(Day.TUESDAY);
                break;
            case Calendar.WEDNESDAY:
                today = getTimeForDay(Day.WEDNESDAY);
                break;
            case Calendar.THURSDAY:
                today = getTimeForDay(Day.THURSDAY);
                break;
            case Calendar.FRIDAY:
                today = getTimeForDay(Day.FRIDAY);
                break;
            case Calendar.SATURDAY:
                today = getTimeForDay(Day.SATURDAY);
                break;
        }
        return today;
    }

    private Time getTimeForDay(Day day){
        Time today = null;
        for(Time time : this.getOpeningTimes().getTimes()){
            if(time.getDay() == day){
                today = time;
                break;
            }
        }
        return today;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        if (Float.compare(location.rating, rating) != 0) return false;
        if (Float.compare(location.addressLongitude, addressLongitude) != 0) return false;
        if (Float.compare(location.addressLatitude, addressLatitude) != 0) return false;
        if (!name.equals(location.name)) return false;
        if (!addressName.equals(location.addressName)) return false;
        if (openingTimes != null ? !openingTimes.equals(location.openingTimes) : location.openingTimes != null)
            return false;
        return happyHours != null ? happyHours.equals(location.happyHours) : location.happyHours == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        result = 31 * result + addressName.hashCode();
        result = 31 * result + (addressLongitude != +0.0f ? Float.floatToIntBits(addressLongitude) : 0);
        result = 31 * result + (addressLatitude != +0.0f ? Float.floatToIntBits(addressLatitude) : 0);
        result = 31 * result + (openingTimes != null ? openingTimes.hashCode() : 0);
        result = 31 * result + (happyHours != null ? happyHours.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", addressName='" + addressName + '\'' +
                ", addressLongitude=" + addressLongitude +
                ", addressLatitude=" + addressLatitude +
                ", openingTimes=" + openingTimes +
                ", happyHours=" + happyHours +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("rating", rating);
        result.put("addressName", addressName);
        result.put("countryName", countryName);
        result.put("cityName", cityName);
        result.put("addressLongitude", addressLongitude);
        result.put("addressLatitude", addressLatitude);
        result.put("openingTimes", openingTimes);
        result.put("happyHours", happyHours);
        result.put("imageKeyList", imageKeyList);
        result.put("ratedUser", ratedUser);

        return result;
    }

    public List<String> getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(List<String> ratedUser) {
        this.ratedUser = ratedUser;
    }
}
