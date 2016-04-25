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
    public List<Time> times;

    public HappyHourTime(@NonNull List<Time> times) {
        this.times = times;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(@NonNull List<Time> times) {
        this.times = times;
    }

    public void setTime(@NonNull Time time){
        this.times.add(time);
    }

    public void removeTime(@NonNull Time time){
        this.times.remove(time);
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
