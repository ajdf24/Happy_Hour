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
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.Location;

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

        ButterKnife.bind(this, view);

        locationName.setText(location.getName());
        place.setText(location.getAddressName());

        buttonGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    LocationManager locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);

                    Criteria criteria = new Criteria();

                    String provider = locationManager.getBestProvider(criteria, true);
                    try {
                        android.location.Location myLocation = locationManager.getLastKnownLocation(provider);

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(view.getContext(), Locale.getDefault());

                        addresses = geocoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();

                        place.setText(address + ", " + city);

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
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
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
}
