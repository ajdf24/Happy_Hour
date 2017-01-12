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

    public static int daysToInt(Day day){
        int dayInt = -1;

        switch (day){
            case SUNDAY:
                dayInt = 6;
                break;
            case MONDAY:
                dayInt = 0;
                break;
            case TUESDAY:
                dayInt = 1;
                break;
            case WEDNESDAY:
                dayInt = 2;
                break;
            case THURSDAY:
                dayInt = 3;
                break;
            case FRIDAY:
                dayInt = 4;
                break;
            case SATURDAY:
                dayInt = 5;
                break;
        }
        return dayInt;
    }

    public static Day intToDays(int day){
        Day dayInt = null;

        switch (day){
            case 6:
                dayInt = Day.SUNDAY;
                break;
            case 0:
                dayInt = Day.MONDAY;
                break;
            case 1:
                dayInt = Day.TUESDAY;
                break;
            case 2:
                dayInt = Day.WEDNESDAY;
                break;
            case 3:
                dayInt = Day.THURSDAY;
                break;
            case 4:
                dayInt = Day.FRIDAY;
                break;
            case 5:
                dayInt = Day.SATURDAY;
                break;
        }
        return dayInt;
    }
}
