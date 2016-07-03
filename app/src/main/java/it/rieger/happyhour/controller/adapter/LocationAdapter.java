package it.rieger.happyhour.controller.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.LocationDetail;
import it.rieger.happyhour.view.viewholder.LocationViewHolder;

/**
 * Adapter for the location list.
 * Created by sebastian on 14.05.16.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> implements Filterable{

    /**
     * List of all locations
     */
    private List<Location> locationList;

    /**
     * List of all locations for filtering
     */
    private List<Location> locationListswap;


    /**
     * constructor
     * @param locations list of the locations which should be shown
     */
    public LocationAdapter(List<Location> locations) {
        locationList = locations;
        locationListswap = locations;
    }

    /**
     * {@inheritDoc}
     * create a {@link LocationViewHolder}
     * @param parent parent view
     * @param viewType type of the view
     * @return a view holder
     */
    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
        return new LocationViewHolder(itemView);
    }

    /**
     * bind a view holder with the data
     * @param holder holder
     * @param position position of the viewholder
     */
    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        Location location = locationList.get(position);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             * Clicklistener which opens the detail view of the location
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION, locationList.get(position));

                intent.putExtras(bundle);
                intent.setClass(new CreateContextForResource().getContext(), LocationDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                new CreateContextForResource().getContext().startActivity(intent);

            }
        });

        holder.getLocationName().setText(location.getName());
        Time today = location.getTodysOpeningTime();
        if(today != null){
            holder.getOpeningTime().setText(today.getStartTime() + " bis " + today.getEndTime());
        }else{
            holder.getOpeningTime().setText("geschlossen");
        }

        if (ActivityCompat.checkSelfPermission(holder.getView().getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(holder.getView().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
            LocationManager locationManager = (LocationManager) holder.getView().getContext().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();

            String provider = locationManager.getBestProvider(criteria, true);
            try {
                android.location.Location myLocation = locationManager.getLastKnownLocation(provider);

                float[] results = new float[1];

                android.location.Location.distanceBetween(location.getAddressLatitude(), location.getAddressLongitude(), myLocation.getLatitude(), myLocation.getLongitude(), results);

                String distance = "";

                if(results[0] > 999.9){
                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.CEILING);

                    results[0] = results[0]/1000;

                    distance = df.format(results[0]);

                    distance = distance + " km";

                }else {
                    DecimalFormat df = new DecimalFormat("#");
                    df.setRoundingMode(RoundingMode.CEILING);

                    distance = df.format(results[0]);

                    distance = distance + " m";
                }


                holder.getDistance().setText(distance);
                System.out.println(distance + " Distanz");
            } catch (NullPointerException e) {
                Log.w("Log", "Can not load current position");

        }

        String drinks = "";
        int maxNumberOffDrinks = 3;
        int currentDrink = 0;

        for(HappyHour happyHour : location.getHappyHours()){
            if(currentDrink < maxNumberOffDrinks) {
                drinks = drinks + happyHour.getDrink() + ",\n";
                currentDrink++;
            }
        }
        drinks = drinks.substring(0, drinks.length()-2);

        holder.getDrinks().setText(drinks);


        holder.getRating().setRating(location.getRating());

        if(location.getCachedImages().size() > 0) {
            holder.getPicture().setImageBitmap(location.getCachedImages().get(0));
        }
    }

    /**
     * {@inheritDoc}
     * @return the count of the items
     */
    @Override
    public int getItemCount() {
        return locationList.size();
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                locationList = (List<Location>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Location> FilteredArrayNames = new ArrayList<>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < locationListswap.size(); i++) {
                    Location dataNames = locationListswap.get(i);

                    if(dataNames.getName().contains(constraint)){
                        FilteredArrayNames.add(dataNames);
                    }else{
                        for (HappyHour happyHour : dataNames.getHappyHours()){
                            if(happyHour.drink.contains(constraint)){
                                FilteredArrayNames.add(dataNames);
                                break;
                            }
                        }
                    }

                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;
            }
        };

        return filter;
    }
}
