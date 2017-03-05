package it.rieger.happyhour.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.rieger.happyhour.R;

/**
 * Created by sebastian on 14.05.16.
 * View holder for the items in one card
 */
public class LocationViewHolder extends RecyclerView.ViewHolder {

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.list_item_location_name)
    TextView locationName;

    @Bind(R.id.list_item_opening_time)
    TextView openingTime;

    @Bind(R.id.list_item_distance)
    TextView distance;

    @Bind(R.id.list_item_drink)
    TextView drinks;

    @Bind(R.id.ratingBar)
    RatingBar rating;

    @Bind(R.id.list_item_picture)
    ImageView picture;

    @Bind(R.id.progressBar)
    public ProgressBar progressBar;

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

    public ImageView getPicture() {
        return picture;
    }

    public View getView(){
        return itemView;
    }

}
