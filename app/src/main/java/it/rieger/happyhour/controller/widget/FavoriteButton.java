package it.rieger.happyhour.controller.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.database.DataSource;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.database.LikedLocation;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 16.05.16.
 */
public class FavoriteButton extends ImageButton  {

    private final String LOG_TAG = getClass().getSimpleName();

    private boolean isActive = false;

    private Context context;

    private Location location;

    private DataSource db;

    private LikedLocation likedLocation;

    public FavoriteButton(Context context) {
        super(context);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
    }

    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
    }

    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
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
                if(!isActive()){
                    Toast.makeText(context,"Zu Favoriten hinzugefügt",Toast.LENGTH_LONG).show();
                    likedLocation = db.createLikedLocation(location.getId());
                    toggle();
                    new PostLocationOnFacebook().execute(location);
                }else {
                    Toast.makeText(context,"Favorit entfernt",Toast.LENGTH_LONG).show();
                    db.deleteLikedLocation(likedLocation);
                    toggle();
                }
            }
        });
    }

    private void toggle(){
        if(isActive){
            isActive = false;
            setImage();
        }else {
            isActive = true;
            setImage();
        }
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
        setImage();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setLocation(Location location){
        this.location = location;

        db = new DataSource(context);

        likedLocation = db.getLikedLocation(location.getId());

        if(likedLocation != null){
            this.setActive(true);
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        setImage();
    }

    private void setImage(){

        if(isActive){
            setImageResource(R.mipmap.ic_favorite);
        }else {
            setImageResource(R.mipmap.ic_favorite_border);
        }
    }

    /**
     * This class posts the new favorite location on facebook
     */
    private class PostLocationOnFacebook extends AsyncTask<Location, Integer, Boolean>{

        private final String LOG_TAG = getClass().getSimpleName();
        /**
         * post location as favorite on facebook
         * @param params the location
         * @return always true
         */
        @Override
        protected Boolean doInBackground(Location... params) {
            ShareContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http:xxx-platzhalter.de"))
                    .setContentTitle("... Hat hat " + location.getName() + " zu seinen Favoriten hinzugefügt")
                    .build();

            ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                }

                @Override
                public void onCancel() {
                    Log.e(LOG_TAG, "Can not post Location");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e(LOG_TAG, "Can not post Location");
                }
            });
            return true;
        }
    }
}

