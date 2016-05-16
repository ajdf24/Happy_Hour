package it.rieger.happyhour.controller.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import it.rieger.happyhour.R;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 16.05.16.
 */
public class FavoriteButton extends ImageButton  {

    private boolean isActive = false;

    private Context context;

    private Location location;

    public FavoriteButton(Context context) {
        super(context);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));
        setListener();
    }

    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));
        setListener();
    }

    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));
        setListener();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));
        setListener();
    }

    private void setListener(){
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location == null){
                    throw new RuntimeException("Location not set");
                }
                toggle();
                if(isActive()){
                    Toast.makeText(context,"Zu Favoriten hinzugefügt",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"Favorit entfernt",Toast.LENGTH_LONG).show();
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

    private boolean isActive() {
        return isActive;
    }

    public void setLocation(Location location){
        this.location = location;
    }
}

