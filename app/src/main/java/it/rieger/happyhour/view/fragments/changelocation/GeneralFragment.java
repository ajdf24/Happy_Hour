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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class GeneralFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String LOCATION = "Location";

    private Location location;

    private OnFragmentInteractionListener listener;

    @Bind(R.id.fragment_general_location_name)
    EditText locationName;

    @Bind(R.id.fragment_general_location_place)
    EditText place;

    @Bind(R.id.fragment_general_locate)
    ImageButton buttonGetLocation;

    @Bind(R.id.activity_change_location_save)
    FloatingActionButton save;

    Context context;

    public GeneralFragment() {
        // Required empty public constructor
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = (Location) getArguments().getSerializable(LOCATION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_general, container, false);

        context = view.getContext();

        ButterKnife.bind(this, view);

//        initializeActiveElements();

        locationName.setText(location.getName());
        place.setText(location.getAddressName());

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

                            place.setText(String.format(getResources().getString(R.string.general_adress_placeholder_adrees_city),address,city));

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

                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                }
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    private void initializeActiveElements(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                if(location.getId() == null || location.getId().isEmpty()){
                    String key = mDatabase.child(AppConstants.Firebase.LOCATIONS_PATH).push().getKey();
                    location.setId(key);
                }

                location.setName(locationName.getText().toString());
                location.setAddressName(place.getText().toString());

                Map<String, Object> postValues = location.toMap();

                Map<String, Object> childUpdates = new HashMap<String, Object>();

                childUpdates.put(AppConstants.Firebase.LOCATIONS_CHILDS_PATH + location.getId(), postValues);
                mDatabase.updateChildren(childUpdates);

            }
        });
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

        if(!place.getText().toString().isEmpty()){

            location.setAddressName(place.getText().toString());
            Geocoder coder = new Geocoder(context);
            try {
                ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(place.getText().toString(), 50);
                if(adresses.size() > 0) {
                    location.setAddressLongitude((float) adresses.get(0).getLongitude());
                    location.setAddressLatitude((float) adresses.get(0).getLatitude());

                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(adresses.get(0).getLatitude(), adresses.get(0).getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        location.setCityName(addresses.get(0).getLocality());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            android.location.Location currentLocation = getLastBestLocation(context);
            if(currentLocation != null) {
                location.setAddressLongitude((float) currentLocation.getLongitude());
                location.setAddressLatitude((float) currentLocation.getLatitude());
                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    location.setCityName(addresses.get(0).getLocality());
                }
            }
        }

        location.setName(locationName.getText().toString());

        BackendDatabase.getInstance().saveLocation(location);

        listener = null;
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

    /**
     * @return the last know best location
     */
    private android.location.Location getLastBestLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)
                    context.getSystemService(Context.LOCATION_SERVICE);

            android.location.Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            android.location.Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if ( 0 < GPSLocationTime - NetLocationTime ) {
                return locationGPS;
            }
            else {
                return locationNet;
            }
        }
        return null;
    }
}
