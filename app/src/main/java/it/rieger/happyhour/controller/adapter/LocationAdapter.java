package it.rieger.happyhour.controller.adapter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import it.rieger.happyhour.R;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.ValueEventListener;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.LocationDetail;
import it.rieger.happyhour.view.viewholder.LocationViewHolder;

/**
 * Adapter for the location list.
 * Created by sebastian on 14.05.16.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> implements Filterable{

    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * List of all locations
     */
    private List<Location> locationList;

    /**
     * List of all locations for filtering
     */
    private List<Location> locationListSwap;


    /**
     * constructor
     * @param locations list of the locations which should be shown
     */
    public LocationAdapter(List<Location> locations) {
        locationList = locations;
        locationListSwap = locations;
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
    public void onBindViewHolder(final LocationViewHolder holder, final int position) {

        final Location location = locationList.get(position);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            /**
             * {@inheritDoc}
             * Clicklistener which opens the detail view of the location
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION, location);

                intent.putExtras(bundle);
                intent.setClass(CreateContextForResource.getContext(), LocationDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                CreateContextForResource.getContext().startActivity(intent);

            }
        });

        holder.getLocationName().setText(location.getName());
        Time today = location.getTodaysOpeningTime();
        if (today != null) {
            holder.getOpeningTime().setText(String.format(CreateContextForResource.getContext().getString(R.string.general_adress_placeholder_adrees_city), today.getStartTime(), today.getEndTime()));
        } else {
            holder.getOpeningTime().setText(R.string.general_closed);
        }

        SmartLocation.with(holder.getView().getContext()).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(android.location.Location currentlocation) {
                android.location.Location myLocation = currentlocation;

                float[] results = new float[1];

                android.location.Location.distanceBetween(location.getAddressLatitude(), location.getAddressLongitude(), myLocation.getLatitude(), myLocation.getLongitude(), results);

                String distance;

                if (results[0] > 999.9) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.CEILING);

                    results[0] = results[0] / 1000;

                    distance = df.format(results[0]);

                    distance = distance + CreateContextForResource.getStringFromID(R.string.general_distance_kilometer);

                } else {
                    DecimalFormat df = new DecimalFormat("#");
                    df.setRoundingMode(RoundingMode.CEILING);

                    distance = df.format(results[0]);

                    distance = distance + CreateContextForResource.getStringFromID(R.string.general_distance_meter);
                }

                holder.getDistance().setText(distance);
            }
        });

        String drinks = "";
        try {
            int maxNumberOffDrinks = 3;
            int currentDrink = 0;

            for (HappyHour happyHour : location.getHappyHours()) {
                if (currentDrink < maxNumberOffDrinks) {
                    drinks = drinks + happyHour.getDrink() + ",\n";
                    currentDrink++;
                }
            }
            drinks = drinks.substring(0, drinks.length() - 2);
        } catch (StringIndexOutOfBoundsException e) {

        }

        holder.getDrinks().setText(drinks);


        holder.getRating().setRating(location.getRating());

        //TODO:Cached Images

        if (location.getImageKeyList().size() > 0) {
            new DownloadImage().execute(new HolderContainerClass(holder, location.getImageKeyList().get(0)));
        }else {
            holder.progressBar.setVisibility(View.INVISIBLE);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                locationList = (List<Location>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Location> filteredArrayNames = new ArrayList<>();

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < locationListSwap.size(); i++) {
                    Location location = locationListSwap.get(i);

                    if(location.getName().toLowerCase().contains(constraint)){
                        filteredArrayNames.add(location);
                    }else{
                        for (HappyHour happyHour : location.getHappyHours()){
                            if(happyHour.getDrink().toLowerCase().contains(constraint)){
                                filteredArrayNames.add(location);
                                break;
                            }
                        }
                    }

                }

                results.count = filteredArrayNames.size();
                results.values = filteredArrayNames;

                return results;
            }
        };

        return filter;
    }

    /**
     * private container class, which bundles a view holder and a image key
     */
    private class HolderContainerClass {

        private LocationViewHolder thumbnailViewHolder = null;

        private String imageKey = null;

        /**
         * constructor
         * @param thumbnailViewHolder current view holder
         * @param imageKey image key for the view holder
         */
        HolderContainerClass(LocationViewHolder thumbnailViewHolder, String imageKey) {
            this.thumbnailViewHolder = thumbnailViewHolder;
            this.imageKey = imageKey;
        }

        /**
         * get view holder
         */
        LocationViewHolder getThumbnailViewHolder() {
            return thumbnailViewHolder;
        }

        /**
         * get image key
         */
        String getImageKey() {
            return imageKey;
        }

    }

    /**
     * inner task for downloading images
     */
    private class DownloadImage extends AsyncTask<HolderContainerClass, Integer, HolderContainerClass> {

        /**
         * {@inheritDoc}
         */
        @Override
        protected HolderContainerClass doInBackground(final HolderContainerClass... params) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference images = database.getReference(AppConstants.Firebase.IMAGES_PATH);

            images.orderByKey().equalTo(params[0].getImageKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Image image = dataSnapshot1.getValue(Image.class);
                        params[0].getThumbnailViewHolder().progressBar.setVisibility(View.INVISIBLE);
                        params[0].getThumbnailViewHolder().getPicture().setImageBitmap(image.getImage());
                    }
                }

            });

            return null;
        }
    }
}
