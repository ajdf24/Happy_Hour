package it.rieger.happyhour.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Model class for the opening times for locations.
 *
 * Created by sebastian on 25.04.16.
 */
public class OpeningTimes implements Serializable{

    /**
     * list of opening times
     */
    public List<Time> times;

    public OpeningTimes(@NonNull List<Time> times) {
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
        if (!(o instanceof OpeningTimes)) return false;

        OpeningTimes that = (OpeningTimes) o;

        return times.equals(that.times);

    }

    @Override
    public int hashCode() {
        return times.hashCode();
    }

    @Override
    public String toString() {
        return "OpeningTimes{" +
                "times=" + times +
                '}';
    }
}
