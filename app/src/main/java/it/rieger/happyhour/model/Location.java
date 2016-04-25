package it.rieger.happyhour.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import it.rieger.happyhour.controller.BitmapLRUCache;

/**
 * Model class, which represents a Location, which all the data which are came from the the database.
 *
 * TODO: Wir müssen uns noch gedanken machen, wie wir die Bilder speichern. Ich würde einen cache vorschlagen, so das Bilder nicht immer neu geladen werden müssen.
 * Created by sebastian on 25.04.16.
 */
public class Location implements Serializable{

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
        this.name = name;
        this.rating = rating;
        this.addressName = addressName;
        this.addressLongitude = addressLongitude;
        this.addressLatitude = addressLatitude;
        this.openingTimes = openingTimes;
        this.happyHours = happyHours;
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
        this(name, rating, addressName, addressLongitude, addressLatitude, openingTimes, null);
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
        this(name, rating, addressName, addressLongitude, addressLatitude, null, null);
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
