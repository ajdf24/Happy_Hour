package it.rieger.happyhour.view.viewholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.HappyHourTime;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.Time;

/**
 * Created by sebastian on 05.07.16.
 */
public class HappyHourViewHolder extends RecyclerView.ViewHolder implements TimePickerDialog.OnTimeSetListener{

    @Bind(R.id.list_item_happy_hour_drink)
    EditText drink;

    @Bind(R.id.list_item_happy_hour_price)
    EditText price;

    @Bind(R.id.list_item_happy_hour_time_day)
    Spinner daySpinner;

    @Bind(R.id.list_item_happy_hour_time_time)
    EditText timeField;

    HappyHour happyHour;

    Time time;

    public HappyHourViewHolder(final View itemView) {
        super(itemView);


        ButterKnife.bind(this, itemView);

        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        HappyHourViewHolder.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.show(((Activity) itemView.getContext()).getFragmentManager(), "Timepickerdialog Monday");
                tpd.setOnTimeSetListener(HappyHourViewHolder.this);
            }
        });

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    switch (position) {
                        case 0:
                            time.setDay(Day.MONDAY);
                            break;
                        case 1:
                            time.setDay(Day.TUESDAY);
                            break;
                        case 2:
                            time.setDay(Day.WEDNESDAY);
                            break;
                        case 3:
                            time.setDay(Day.THURSDAY);
                            break;
                        case 4:
                            time.setDay(Day.FRIDAY);
                            break;
                        case 5:
                            time.setDay(Day.SATURDAY);
                            break;
                        case 6:
                            time.setDay(Day.SUNDAY);
                            break;
                    }
                }catch (NullPointerException e){
                    Log.e("","Time not set");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public EditText getDrink() {
        return drink;
    }

    public EditText getPrice() {
        return price;
    }

    public void setHappyHour(HappyHour happyHour){
        this.happyHour = happyHour;
        time = happyHour.getHappyHourTime();
    }

    public Spinner getDaySpinner() {
        return daySpinner;
    }

    public EditText getTimeField() {
        return timeField;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String timeString = hourOfDay+":"+minute + "Uhr Bis: " + hourOfDayEnd + ":" + minuteEnd + "Uhr";
        timeField.setText(timeString);


        time.setStartTime(hourOfDay+":"+minute);
        time.setEndTime(hourOfDayEnd + ":" + minuteEnd);

        happyHour.setHappyHourTime(time);
    }
}