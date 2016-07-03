package it.rieger.happyhour.model;

import java.io.Serializable;

/**
 * A enum for the days of the week.
 *
 * Created by sebastian on 25.04.16.
 */
public enum Day implements Serializable {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY;

    public static String toString(Day day) {
        String dayName = "";
        switch (day){
            case SUNDAY:
                dayName = "Sonntag";
                break;
            case MONDAY:
                dayName = "Montag";
                break;
            case TUESDAY:
                dayName = "Dienstag";
                break;
            case WEDNESDAY:
                dayName = "Mittwoch";
                break;
            case THURSDAY:
                dayName = "Donnerstag";
                break;
            case FRIDAY:
                dayName = "Freitag";
                break;
            case SATURDAY:
                dayName = "Samstag";
                break;
        }
        return dayName;
    }
}
