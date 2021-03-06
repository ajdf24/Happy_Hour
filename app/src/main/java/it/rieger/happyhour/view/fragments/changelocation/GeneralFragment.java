package it.rieger.happyhour.view.fragments.changelocation;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.AppConstants;

public class GeneralFragment extends AbstractChangeLocationFragment implements AbstractChangeLocationFragment.OnFragmentInteractionListener {

    @Bind(R.id.fragment_general_location_name)
    EditText locationName;

    @Bind(R.id.fragment_general_location_place)
    EditText place;

    @Bind(R.id.fragment_general_locate)
    ImageButton buttonGetLocation;

    public GeneralFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GeneralFragment.
     */
    public static GeneralFragment newInstance(Location location) {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_general, container, false);

        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeGui() {
        locationName.setText(location.getName());
        place.setText(location.getAddressName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeActiveElements() {

        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SmartLocation.with(view.getContext()).location().oneFix().start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(android.location.Location location) {
                        try {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(view.getContext(), Locale.getDefault());

                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();

                            place.setText(String.format(getResources().getString(R.string.general_adress_placeholder_adrees_city), address, city));

                            //TODO: Places API Implementieren
                            locationName.setText("");


                        } catch (NullPointerException e) {
                            Log.w("Log", "Can not load current position");
                            Toast.makeText(view.getContext(), R.string.general_can_not_locate, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Log.w("Log", "Can not load current position");
                            Toast.makeText(view.getContext(), R.string.general_can_not_locate, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkReadyToSave() {
        Address addressLocation = null;

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            address = coder.getFromLocationName(place.getText().toString(), 5);
            if (address != null && address.size() > 0) {
                addressLocation = address.get(0);
            }

            double lat = addressLocation.getLatitude();
            double log = addressLocation.getLongitude();

            if (lat != 0) {
                if (log != 0) {
                    location.setAddressLongitude((float) log);
                    location.setAddressLatitude((float) lat);

                    String cityName = addressLocation.getLocality();

                    if (cityName != null) {
                        location.setName(locationName.getText().toString());
                        location.setAddressName(place.getText().toString());
                        location.setCityName(cityName);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
