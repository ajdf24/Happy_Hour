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

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.OpeningTimes;
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

    private Location location;

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

                dayClickID = 0;
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

                dayClickID = 1;
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

                dayClickID = 2;
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

                dayClickID = 3;
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

                dayClickID = 4;
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

                dayClickID = 5;
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

                dayClickID = 6;
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
    public void onDetach() {
        super.onDetach();


        BackendDatabase.getInstance().saveLocation(location);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        String time = hourOfDay+":"+minute + CreateContextForResource.getStringFromID(R.string.general_clock_to) + hourOfDayEnd + ":" + minuteEnd + CreateContextForResource.getStringFromID(R.string.general_clock);

        switch (dayClickID){
            case 0:
                mondayText.setText(time);
                break;
            case 1:
                thusedayText.setText(time);
                break;
            case 2:
                wensdayText.setText(time);
                break;
            case 3:
                thursdayText.setText(time);
                break;
            case 4:
                fridayText.setText(time);
                break;
            case 5:
                saturdayText.setText(time);
                break;
            case 6:
                sundayText.setText(time);
                break;
        }
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
