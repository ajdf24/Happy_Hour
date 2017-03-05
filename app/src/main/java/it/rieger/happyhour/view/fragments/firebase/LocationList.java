package it.rieger.happyhour.view.fragments.firebase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.math.RoundingMode;
import java.text.DecimalFormat;

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
 * abstract class for showing locations from firebase
 * Created by sebastian on 11.12.16.
 */

public abstract class LocationList extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    private DatabaseReference database;

    private RecyclerView recycler;

    public LocationList() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        database = FirebaseDatabase.getInstance().getReference();

        recycler = (RecyclerView) findViewById(R.id.activity_location_list_recycler_view);
        recycler.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recycler.setLayoutManager(manager);


    }

    /**
     * get the current query for showing locations
     * @param databaseReference a firebase reference
     * @return the current query
     */
    public abstract Query getQuery(DatabaseReference databaseReference);

    /**
     * internal task for downloading images
     */
    private class DownloadImage extends AsyncTask<HolderContainerClass, Integer, HolderContainerClass>{

        /**
         * {@inheritDoc}
         */
        @Override
        protected HolderContainerClass doInBackground(final HolderContainerClass... params) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference images = database.getReference(AppConstants.Firebase.IMAGES_PATH);

            images.orderByKey().equalTo(params[0].getImageKey()).addListenerForSingleValueEvent(new ValueEventListener() {

                /**
                 * {@inheritDoc}
                 */
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

    /**
     * internal container for view holder a image key
     */
    private class HolderContainerClass {

        private LocationViewHolder thumbnailViewHolder = null;

        private String imageKey = null;

        HolderContainerClass(LocationViewHolder thumbnailViewHolder, String imageKey) {
            this.thumbnailViewHolder = thumbnailViewHolder;
            this.imageKey = imageKey;
        }

        LocationViewHolder getThumbnailViewHolder() {
            return thumbnailViewHolder;
        }

        String getImageKey() {
            return imageKey;
        }

    }

    /**
     * callback method which is called when the current user city is loaded
     */
    protected void cityLoaded(){

        Query locationQuery = getQuery(database);
        FirebaseRecyclerAdapter<Location, LocationViewHolder> adapter = new FirebaseRecyclerAdapter<Location, LocationViewHolder>(Location.class, R.layout.list_item_location, LocationViewHolder.class, locationQuery) {

            /**
             * {@inheritDoc}
             */
            @Override
            protected void populateViewHolder(final LocationViewHolder holder, final Location location, int position) {
                holder.getView().setOnClickListener(new View.OnClickListener() {

                    /**
                     * {@inheritDoc}
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

                    /**
                     * {@inheritDoc}
                     */
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

                if (location.getImageKeyList().size() > 0) {
                    new DownloadImage().execute(new HolderContainerClass(holder, location.getImageKeyList().get(0)));
                } else {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        };
        recycler.setAdapter(adapter);
    }

}
