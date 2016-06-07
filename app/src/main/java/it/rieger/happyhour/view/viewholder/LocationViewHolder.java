package it.rieger.happyhour.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.widget.DynamicImageView;

/**
 * Created by sebastian on 14.05.16.
 * View holder for the items in one card
 */
public class LocationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_item_location_name)
    TextView locationName;

    @BindView(R.id.list_item_opening_time)
    TextView openingTime;

    @BindView(R.id.list_item_distance)
    TextView distance;

    @BindView(R.id.list_item_drink)
    TextView drinks;

    @BindView(R.id.ratingBar)
    RatingBar rating;

    @BindView(R.id.list_item_picture)
    DynamicImageView picture;

    View itemView;

    /**
     * constructor
     * initializes all fields for the view
     * @param itemView the item view for the holder
     */
    public LocationViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.itemView = itemView;
    }

    public TextView getLocationName() {
        return locationName;
    }

    public TextView getOpeningTime() {
        return openingTime;
    }

    public TextView getDistance() {
        return distance;
    }

    public TextView getDrinks() {
        return drinks;
    }

    public RatingBar getRating() {
        return rating;
    }

    public DynamicImageView getPicture() {
        return picture;
    }

    public View getView(){
        return itemView;
    }

}
