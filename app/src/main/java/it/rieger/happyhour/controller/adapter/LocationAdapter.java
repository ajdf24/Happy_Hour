package it.rieger.happyhour.controller.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.standard.CreateContextForResource;
import it.rieger.happyhour.view.LocationDetail;
import it.rieger.happyhour.view.viewholder.LocationViewHolder;

/**
 * Adapter for the location list.
 * Created by sebastian on 14.05.16.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder>{

    private List<Location> locationList;

    public LocationAdapter(List<Location> locations) {
        locationList = locations;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, final int position) {
        Location location = locationList.get(position);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_LOCATION, locationList.get(position));

                intent.putExtras(bundle);
                intent.setClass(CreateContextForResource.getContext(), LocationDetail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                CreateContextForResource.getContext().startActivity(intent);

            }
        });

        holder.getLocationName().setText(location.getName());
        //TODO: sre Hier muss die Zeit noch eingetragen werden
        holder.getOpeningTime().setText("");

        //TODO: sre Distanz berechnen
        holder.getDistance().setText("");

        //TODO: sre Drinks holden
        holder.getDistance().setText("");

        holder.getRating().setRating(location.getRating());

        if(location.getCachedImages().size() > 0) {
            holder.getPicture().setImageBitmap(location.getCachedImages().get(0));
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
