package it.rieger.happyhour.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.controller.cache.BitmapLRUCache;

/**
 * Model class, which represents a Location, which all the data which are came from the the database.
 *
 * Created by sebastian on 25.04.16.
 */
public class Location implements Serializable{

    /**
     * database id of this location
     */
    public int id;

    /**
     * name of the locaton
     */
    public String name;

    /**
     * rating for the location
     */
    public float rating;

    /**
     * name of the address
     */
    public String addressName;

    /**
     * longitude of the location
     */
    public float addressLongitude;

    /**
     * latitude of the location
     */
    public float addressLatitude;

    /**
     * times when the location is open
     */
    public OpeningTimes openingTimes;

    /**
     * happy hours of the location
     */
    public List<HappyHour> happyHours;

    /**
     * list of image keys for the location
     */
    public List<String> imageKeyList = new ArrayList<>();

    /**
     * list of image keys which are not in the cache
     */
    public List<String> notCachedImages = new ArrayList<>();

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
    public Location(@NonNull String name, @NonNull float rating, @NonNull String addressName, @NonNull float addressLongitude, @NonNull float addressLatitude, OpeningTimes openingTimes, List<HappyHour> happyHours, List<String> imageKeyList) {
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
    public Location(@NonNull String name, @NonNull float rating, @NonNull String addressName, @NonNull float addressLongitude, @NonNull float addressLatitude, OpeningTimes openingTimes, List<HappyHour> happyHours) {
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
    public Location(@NonNull String name, @NonNull float rating, @NonNull String addressName, @NonNull float addressLongitude, @NonNull float addressLatitude, OpeningTimes openingTimes) {
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
    public Location(@NonNull String name, @NonNull float rating, @NonNull String addressName, @NonNull float addressLongitude, @NonNull float addressLatitude) {
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

    public void setRating(@NonNull float rating) {
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

    public void setAddressLongitude(@NonNull float addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public float getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(@NonNull float addressLatitude) {
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

    /**
     * Return all cached images for a specific location.
     * @return a list of {@link Bitmap} with the images which are in the cache
     */
    public List<Bitmap> getCachedImages(){
        List<Bitmap> imageList = new ArrayList<>();

        for(String key : imageKeyList){
            Bitmap image = BitmapLRUCache.getInstance().getBitmapFromMemCache(key);
            if(image != null) {
                imageList.add(image);
            }else {
                notCachedImages.add(key);
            }
        }

        return imageList;
    }

    /**
     * Get a list of all keys of images which are not in the cache
     * @return
     */
    public List<String> getNotCachedImages(){
        List<Bitmap> imageList = new ArrayList<>();

        for(String key : imageKeyList){
            Bitmap image = BitmapLRUCache.getInstance().getBitmapFromMemCache(key);
            if(image != null) {
                imageList.add(image);
            }else {
                notCachedImages.add(key);
            }
        }

        return notCachedImages;
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
}
