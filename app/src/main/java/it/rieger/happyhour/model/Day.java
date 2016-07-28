package it.rieger.happyhour.model;

import java.io.Serializable;

import it.rieger.happyhour.R;
import it.rieger.happyhour.util.standard.CreateContextForResource;

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
                dayName = CreateContextForResource.getStringFromID(R.string.general_day_sunday);
                break;
            case MONDAY:
                dayName = CreateContextForResource.getStringFromID(R.string.general_day_monday);
                break;
            case TUESDAY:
                dayName = CreateContextForResource.getStringFromID(R.string.general_day_tuesday);
                break;
            case WEDNESDAY:
                dayName = CreateContextForResource.getStringFromID(R.string.general_day_wednesday);
                break;
            case THURSDAY:
                dayName = CreateContextForResource.getStringFromID(R.string.general_day_thursday);
                break;
            case FRIDAY:
                dayName = CreateContextForResource.getStringFromID(R.string.general_day_friday);
                break;
            case SATURDAY:
                dayName = CreateContextForResource.getStringFromID(R.string.general_day_saturday);
                break;
        }
        return dayName;
    }
}
