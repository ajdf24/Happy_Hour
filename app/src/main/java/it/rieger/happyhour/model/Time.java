package it.rieger.happyhour.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Model class, which contains a time for a location or a happy hour.
 * The time contains a day, a start time and a end time.
 *
 * Created by sebastian on 25.04.16.
 */
public class Time implements Serializable {

    /**
     * day of the opening
     */
    private Day day;

    /**
     * start time for the opening
     */
    private String startTime;

    /**
     * end time for the opening
     */
    private String endTime;

    public Day getDay() {
        return day;
    }

    public void setDay(@NonNull Day day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(@NonNull String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(@NonNull String endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Time)) return false;

        Time time = (Time) o;

        if (day != time.day) return false;
        if (!startTime.equals(time.startTime)) return false;
        return endTime.equals(time.endTime);

    }

    @Override
    public int hashCode() {
        int result = day.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Time{" +
                "day=" + day +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}


