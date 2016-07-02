package it.rieger.happyhour.controller.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.database.DataSource;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.database.LikedLocation;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 16.05.16.
 */
public class FavoriteButton extends ImageButton  {

    private boolean isActive = false;

    private Context context;

    private Location location;

    private DataSource db;

    private LikedLocation likedLocation;

    public FavoriteButton(Context context) {
        super(context);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        db = new DataSource(context);

        List<LikedLocation> likedLocations = db.getAllLikedLocations();
        for (LikedLocation likedLocation : likedLocations){
            if(location.getId() == likedLocation.getLocationID()){
                this.setActive(true);
                this.likedLocation = likedLocation;
                break;
            }
        }

        setListener();
        toggle();
    }

    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        db = new DataSource(context);

        List<LikedLocation> likedLocations = db.getAllLikedLocations();
        for (LikedLocation likedLocation : likedLocations){
            if(location.getId() == likedLocation.getLocationID()){
                this.setActive(true);
                this.likedLocation = likedLocation;
                break;
            }
        }

        setListener();
        toggle();
    }

    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        db = new DataSource(context);

        List<LikedLocation> likedLocations = db.getAllLikedLocations();
        for (LikedLocation likedLocation : likedLocations){
            if(location.getId() == likedLocation.getLocationID()){
                this.setActive(true);
                this.likedLocation = likedLocation;
                break;
            }
        }

        setListener();
        toggle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        db = new DataSource(context);

        List<LikedLocation> likedLocations = db.getAllLikedLocations();
        for (LikedLocation likedLocation : likedLocations){
            if(location.getId() == likedLocation.getLocationID()){
                this.setActive(true);
                this.likedLocation = likedLocation;
                break;
            }
        }

        setListener();
        toggle();
    }

    private void setListener(){
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location == null){
                    throw new RuntimeException("Location not set");
                }
                if(db == null){
                    throw new RuntimeException("Database not set");
                }
                toggle();
                if(isActive()){
                    Toast.makeText(context,"Zu Favoriten hinzugefügt",Toast.LENGTH_LONG).show();
                    likedLocation = db.createLikedLocation(location.getId());
                }else {
                    Toast.makeText(context,"Favorit entfernt",Toast.LENGTH_LONG).show();
                    db.deleteLikedLocation(likedLocation);
                }
            }
        });
    }

    private void toggle(){
        if(isActive){
            isActive = false;
            setImageResource(R.mipmap.ic_favorite_border);
        }else {
            isActive = true;
            setImageResource(R.mipmap.ic_favorite);
        }
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
        toggle();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setLocation(Location location){
        this.location = location;
    }

}

