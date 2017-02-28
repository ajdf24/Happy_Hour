package it.rieger.happyhour.view.fragments.changelocation;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import it.rieger.happyhour.R;
import it.rieger.happyhour.model.Day;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OpeningFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OpeningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpeningFragment extends AbstractChangeLocationFragment implements TimePickerDialog.OnTimeSetListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String LOCATION = "Location";

    @Bind(R.id.fragment_opening_monday_text)
    EditText mondayText;

    @Bind(R.id.fragment_opening_tuesday_text)
    EditText thusedayText;

    @Bind(R.id.fragment_opening_wednesday_text)
    EditText wensdayText;

    @Bind(R.id.fragment_opening_thursday_text)
    EditText thursdayText;

    @Bind(R.id.fragment_opening_friday_text)
    EditText fridayText;

    @Bind(R.id.fragment_opening_saturday_text)
    EditText saturdayText;

    @Bind(R.id.fragment_opening_sunday_text)
    EditText sundayText;

    private OnFragmentInteractionListener listener;

    private int dayClickID;

    public OpeningFragment() {
        // Required empty public constructor
    }

    public static OpeningFragment newInstance(Location location) {
        OpeningFragment fragment = new OpeningFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = (Location) getArguments().getSerializable(LOCATION);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_opening, container, false);

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    protected void initializeGui() {



        List<Time> openingTimes = location.getOpeningTimes().getTimes();

        for(int i = 0; i < location.getOpeningTimes().getTimes().size(); i++){
            if(openingTimes.get(i).getDay() == Day.SUNDAY){
                sundayText.setText(generateTimeStringForInitialize(i));
            }
            if(openingTimes.get(i).getDay() == Day.MONDAY){
                mondayText.setText(generateTimeStringForInitialize(i));
            }
            if(openingTimes.get(i).getDay() == Day.TUESDAY){
                thusedayText.setText(generateTimeStringForInitialize(i));
            }
            if(openingTimes.get(i).getDay() == Day.WEDNESDAY){
                wensdayText.setText(generateTimeStringForInitialize(i));
            }
            if(openingTimes.get(i).getDay() == Day.THURSDAY){
                thursdayText.setText(generateTimeStringForInitialize(i));
            }
            if(openingTimes.get(i).getDay() == Day.FRIDAY){
                fridayText.setText(generateTimeStringForInitialize(i));
            }
            if(openingTimes.get(i).getDay() == Day.SATURDAY){
                saturdayText.setText(generateTimeStringForInitialize(i));
            }
        }
    }

    private String generateTimeStringForInitialize(int index){

        String minuteString = "";

        if(location.getOpeningTimes().getTimes().get(index).getMinute() < 10){
            minuteString = "0" + location.getOpeningTimes().getTimes().get(index).getMinute();
        }else {
            minuteString = "" + location.getOpeningTimes().getTimes().get(index).getMinute();
        }

        String hourOfDayString = "";

        if(location.getOpeningTimes().getTimes().get(index).getHourOfDay() < 10){
            hourOfDayString = "0" + location.getOpeningTimes().getTimes().get(index).getHourOfDay();
        }else {
            hourOfDayString = "" + location.getOpeningTimes().getTimes().get(index).getHourOfDay();
        }

        String minuteEndString = "";

        if(location.getOpeningTimes().getTimes().get(index).getMinuteEnd() < 10){
            minuteEndString = "0" + location.getOpeningTimes().getTimes().get(index).getMinuteEnd();
        }else {
            minuteEndString = "" + location.getOpeningTimes().getTimes().get(index).getMinuteEnd();
        }

        String hourOfDayEndString = "";

        if (location.getOpeningTimes().getTimes().get(index).getHourOfDayEnd() < 10){
            hourOfDayEndString = "0" + location.getOpeningTimes().getTimes().get(index).getHourOfDayEnd();
        }else {
            hourOfDayEndString = "" + location.getOpeningTimes().getTimes().get(index).getHourOfDayEnd();
        }

        return hourOfDayString+":"+minuteString + CreateContextForResource.getStringFromID(R.string.general_clock_to) + hourOfDayEndString + ":" + minuteEndString + CreateContextForResource.getStringFromID(R.string.general_clock);
    }

    @Override
    protected void initializeActiveElements() {

        mondayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        OpeningFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );



                tpd.show(getFragmentManager(), AppConstants.FragmentTags.FRAGMENT_TIME_PICKER_MONDAY);
                tpd.setOnTimeSetListener(OpeningFragment.this);

                dayClickID = 1;
            }
        });
        thusedayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        OpeningFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.show(getFragmentManager(), AppConstants.FragmentTags.FRAGMENT_TIME_PICKER_TUESDAY);
                tpd.setOnTimeSetListener(OpeningFragment.this);

                dayClickID = 2;
            }
        });
        wensdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        OpeningFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.show(getFragmentManager(), AppConstants.FragmentTags.FRAGMENT_TIME_PICKER_WEDNESDAY);
                tpd.setOnTimeSetListener(OpeningFragment.this);

                dayClickID = 3;
            }
        });
        thursdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        OpeningFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.show(getFragmentManager(), AppConstants.FragmentTags.FRAGMENT_TIME_PICKER_THURSDAY);
                tpd.setOnTimeSetListener(OpeningFragment.this);

                dayClickID = 4;
            }
        });
        fridayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        OpeningFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.show(getFragmentManager(), AppConstants.FragmentTags.FRAGMENT_TIME_PICKER_FRIDAY);
                tpd.setOnTimeSetListener(OpeningFragment.this);

                dayClickID = 5;
            }
        });
        saturdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        OpeningFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.show(getFragmentManager(), AppConstants.FragmentTags.FRAGMENT_TIME_PICKER_SATURDAY);
                tpd.setOnTimeSetListener(OpeningFragment.this);

                dayClickID = 6;
            }
        });
        sundayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        OpeningFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );

                tpd.show(getFragmentManager(), AppConstants.FragmentTags.FRAGMENT_TIME_PICKER_SUNDAY);
                tpd.setOnTimeSetListener(OpeningFragment.this);

                dayClickID = 0;
            }
        });

    }

    @Override
    protected boolean checkReadyToSave() {
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

        String minuteString = "";

        if(minute < 10){
            minuteString = "0" + minute;
        }else {
            minuteString = "" + minute;
        }

        String hourOfDayString = "";

        if(hourOfDay < 10){
            hourOfDayString = "0" + hourOfDay;
        }else {
            hourOfDayString = "" + hourOfDay;
        }

        String minuteEndString = "";

        if(minuteEnd < 10){
            minuteEndString = "0" + minuteEnd;
        }else {
            minuteEndString = "" + minuteEnd;
        }

        String hourOfDayEndString = "";

        if (hourOfDayEnd < 10){
            hourOfDayEndString = "0" + hourOfDayEnd;
        }else {
            hourOfDayEndString = "" + hourOfDayEnd;
        }

        String time = hourOfDayString+":"+minuteString + CreateContextForResource.getStringFromID(R.string.general_clock_to) + hourOfDayEndString + ":" + minuteEndString + CreateContextForResource.getStringFromID(R.string.general_clock);

        switch (dayClickID){
            case 1:
                mondayText.setText(time);
                addOpeningTime(dayClickID, hourOfDay, minute, hourOfDayEnd, minuteEnd);
                break;
            case 2:
                thusedayText.setText(time);
                addOpeningTime(dayClickID, hourOfDay, minute, hourOfDayEnd, minuteEnd);
                break;
            case 3:
                wensdayText.setText(time);
                addOpeningTime(dayClickID, hourOfDay, minute, hourOfDayEnd, minuteEnd);
                break;
            case 4:
                thursdayText.setText(time);
                addOpeningTime(dayClickID, hourOfDay, minute, hourOfDayEnd, minuteEnd);
                break;
            case 5:
                fridayText.setText(time);
                addOpeningTime(dayClickID, hourOfDay, minute, hourOfDayEnd, minuteEnd);
                break;
            case 6:
                saturdayText.setText(time);
                addOpeningTime(dayClickID, hourOfDay, minute, hourOfDayEnd, minuteEnd);
                break;
            case 0:
                sundayText.setText(time);
                addOpeningTime(dayClickID, hourOfDay, minute, hourOfDayEnd, minuteEnd);
                break;
        }
    }

    private void addOpeningTime(int day, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd){
        Day enumDay = Day.intToDays(day);

        int timeListIndex = listContainsDay(enumDay);

        if(timeListIndex > -1){
            location.getOpeningTimes().getTimes().remove(timeListIndex);
        }

        Time newOpeningTime = new Time();
        newOpeningTime.setDay(enumDay);
        newOpeningTime.setHourOfDay(hourOfDay);
        newOpeningTime.setMinute(minute);
        newOpeningTime.setHourOfDayEnd(hourOfDayEnd);
        newOpeningTime.setMinuteEnd(minuteEnd);

        String startTime = "";
        if(hourOfDay < 10){
            startTime = startTime + "0" + hourOfDay + ":";
        }else {
            startTime = startTime + hourOfDay + ":";
        }

        if(minute < 10){
            startTime = startTime + "0" + minute;
        }else {
            startTime = startTime + minute;
        }

        newOpeningTime.setStartTime(startTime);

        String endTime = "";
        if(hourOfDayEnd < 10){
            endTime = endTime + "0" + hourOfDayEnd + ":";
        }else {
            endTime = endTime + hourOfDayEnd + ":";
        }

        if(minuteEnd < 10){
            endTime = endTime + "0" + minuteEnd;
        }else {
            endTime = endTime + minuteEnd;
        }

        newOpeningTime.setEndTime(endTime);

        location.getOpeningTimes().getTimes().add(newOpeningTime);
    }

    private int listContainsDay(Day day){
        List<Time> openingTimes = location.getOpeningTimes().getTimes();

        for(int i = 0; i < location.getOpeningTimes().getTimes().size(); i++){
            if(openingTimes.get(i).getDay() == day){
                return i;
            }
        }
        return -1;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
