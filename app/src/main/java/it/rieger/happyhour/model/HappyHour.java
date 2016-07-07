package it.rieger.happyhour.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Model class for a specific happy hour.
 * The data for this class came from the database.
 *
 * Created by sebastian on 25.04.16.
 */
public class HappyHour implements Serializable {

    public String drink;

    public String price;

    public Time happyHourTime;

    public HappyHour() {
    }

    public HappyHour(@NonNull String drink, @NonNull String price, @NonNull Time happyHourTime) {
        this.drink = drink;
        this.price = price;
        this.happyHourTime = happyHourTime;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(@NonNull String drink) {
        this.drink = drink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(@NonNull String price) {
        this.price = price;
    }

    public Time getHappyHourTime() {
        return happyHourTime;
    }

    public void setHappyHourTime(@NonNull Time happyHourTime) {
        this.happyHourTime = happyHourTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HappyHour)) return false;

        HappyHour happyHour = (HappyHour) o;

        if (!drink.equals(happyHour.drink)) return false;
        if (!price.equals(happyHour.price)) return false;
        return happyHourTime.equals(happyHour.happyHourTime);

    }

    @Override
    public int hashCode() {
        int result = drink.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + happyHourTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HappyHour{" +
                "drink='" + drink + '\'' +
                ", price='" + price + '\'' +
                ", happyHourTime=" + happyHourTime +
                '}';
    }
}
