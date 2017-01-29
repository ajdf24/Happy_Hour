package it.rieger.happyhour.view.fragments.firebase;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import it.rieger.happyhour.R;
import it.rieger.happyhour.model.HappyHour;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.ThumbnailHolderClass;
import it.rieger.happyhour.model.Time;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.LocationDetail;
import it.rieger.happyhour.view.viewholder.LocationViewHolder;
import it.rieger.happyhour.view.viewholder.ThumbnailViewHolder;

/**
 * Created by sebastian on 11.12.16.
 */

public abstract class LocationListFragment extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Location, LocationViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public LocationListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) findViewById(R.id.activity_location_list_recycler_view);
        mRecycler.setHasFixedSize(true);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query locationQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<it.rieger.happyhour.model.Location, LocationViewHolder>(it.rieger.happyhour.model.Location.class, R.layout.list_item_location, LocationViewHolder.class, locationQuery) {
            @Override
            protected void populateViewHolder(final LocationViewHolder holder, final it.rieger.happyhour.model.Location location, int position) {
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
        };
        mRecycler.setAdapter(mAdapter);
    }

    public abstract Query getQuery(DatabaseReference databaseReference);


    private class DownloadImage extends AsyncTask<HolderContainerClass, Integer, HolderContainerClass>{

        @Override
        protected HolderContainerClass doInBackground(final HolderContainerClass... params) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference images = database.getReference("images");

            images.orderByKey().equalTo(params[0].getImageKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Image image = dataSnapshot1.getValue(Image.class);
                        params[0].getThumbnailViewHolder().progressBar.setVisibility(View.INVISIBLE);
                        params[0].getThumbnailViewHolder().getPicture().setImageBitmap(image.getImage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return null;
        }
    }

    private class HolderContainerClass {

        private LocationViewHolder thumbnailViewHolder = null;

        private String imageKey = null;

        public HolderContainerClass(LocationViewHolder thumbnailViewHolder, String imageKey) {
            this.thumbnailViewHolder = thumbnailViewHolder;
            this.imageKey = imageKey;
        }

        public LocationViewHolder getThumbnailViewHolder() {
            return thumbnailViewHolder;
        }

        public void setThumbnailViewHolder(LocationViewHolder thumbnailViewHolder) {
            this.thumbnailViewHolder = thumbnailViewHolder;
        }

        public String getImageKey() {
            return imageKey;
        }

        public void setImageKey(String imageKey) {
            this.imageKey = imageKey;
        }
    }
}
