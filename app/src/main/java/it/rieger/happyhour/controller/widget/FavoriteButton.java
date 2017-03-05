package it.rieger.happyhour.controller.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.User;
import it.rieger.happyhour.util.AppConstants;
import it.rieger.happyhour.util.listener.ValueEventListener;
import it.rieger.happyhour.util.standard.CreateContextForResource;

/**
 * Button for liking a location.
 *
 * this button writes the liked or unliked state to the database without other actions are needed
 * Created by sebastian on 16.05.16.
 */
public class FavoriteButton extends ImageButton  {

    private final String LOG_TAG = getClass().getSimpleName();

    private boolean isActive = false;

    private Context context;

    private Location location;

    /**
     * constructor
     * @param context current context
     */
    public FavoriteButton(Context context) {
        super(context);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
    }

    /**
     * constructor
     * @param context current context
     * @param attrs additional attributes
     */
    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
    }

    /**
     * constructor
     * @param context current context
     * @param attrs additional attributes
     * @param defStyleAttr style attributes
     */
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
    }

    /**
     * constructor
     * @param context current context
     * @param attrs additional attributes
     * @param defStyleAttr style attributes
     * @param defStyleRes style references
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavoriteButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setColorFilter(Color.parseColor(CreateContextForResource.getStringFromID(R.color.colorAccent)));

        setImage();

        setListener();
    }

    /**
     * set a listener to the button which writes the current state to the database
     */
    private void setListener(){
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location == null){
                    throw new RuntimeException("Location not set");
                }
                if(!isActive()){
                    Toast.makeText(context, R.string.general_added_to_favorites, Toast.LENGTH_LONG).show();

                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child(AppConstants.Firebase.USERS_PATH).orderByChild("uID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                        /**
                         * {@inheritDoc}
                         */
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()){
                                User user = dataSnapshotUser.getValue(User.class);

                                user.getLikedLocations().add(location.getId());

                                BackendDatabase.getInstance().saveUser(user);
                            }
                        }

                    });

                    toggle();
                }else {
                    Toast.makeText(context, R.string.general_removed_from_favorites, Toast.LENGTH_LONG).show();

                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child(AppConstants.Firebase.USERS_PATH).orderByChild("uID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                        /**
                         * {@inheritDoc}
                         */
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()){
                               User user = dataSnapshotUser.getValue(User.class);

                               user.getLikedLocations().remove(location.getId());

                               BackendDatabase.getInstance().saveUser(user);
                            }
                        }

                    });
                    toggle();
                }
            }
        });
    }

    /**
     * toggle the button state
     */
    private void toggle(){
        if(isActive){
            isActive = false;
            setImage();
        }else {
            isActive = true;
            setImage();
        }
    }

    /**
     * set the button active or inactive
     * @param isActive the state which should be set
     */
    public void setActive(boolean isActive){
        this.isActive = isActive;
        setImage();
    }

    /**
     * is button active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * connect the button with a specific location
     * @param location the location for the button
     */
    public void setLocation(final Location location){
        this.location = location;

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(AppConstants.Firebase.USERS_PATH).orderByChild("uID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()){
                    User user = dataSnapshotUser.getValue(User.class);

                    if(user.getLikedLocations().contains(location.getId())){
                        FavoriteButton.this.setActive(true);
                    }
                }
            }

        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        setImage();
    }

    /**
     * the the correct image for the button
     */
    private void setImage(){

        if(isActive){
            setImageResource(R.mipmap.ic_favorite);
        }else {
            setImageResource(R.mipmap.ic_favorite_border);
        }
    }

}

