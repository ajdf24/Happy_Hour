package it.rieger.happyhour.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Model class for a time on which is a happy hour.
 *
 * Created by sebastian on 25.04.16.
 */
public class HappyHourTime implements Serializable {

    /**
     * list of all times the happy hour is
     */
    public Time times;

    public HappyHourTime(@NonNull Time times) {
        this.times = times;
    }

    public Time getTime() {
        return times;
    }

    public void setTime(@NonNull Time time) {
        this.times = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HappyHourTime)) return false;

        HappyHourTime that = (HappyHourTime) o;

        return times.equals(that.times);

    }

    @Override
    public int hashCode() {
        return times.hashCode();
    }

    @Override
    public String toString() {
        return "HappyHourTime{" +
                "times=" + times +
                '}';
    }
}
