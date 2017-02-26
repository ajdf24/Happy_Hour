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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.R;
import it.rieger.happyhour.controller.backend.BackendDatabase;
import it.rieger.happyhour.controller.database.DataSource;
import it.rieger.happyhour.model.Image;
import it.rieger.happyhour.model.Location;
import it.rieger.happyhour.model.User;
import it.rieger.happyhour.model.database.LikedLocation;
import it.rieger.happyhour.util.AppConstants;
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
                    Toast.makeText(context, R.string.general_added_to_favorites, Toast.LENGTH_LONG).show();
                    //TODO: write to Firebase

                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child(AppConstants.Firebase.USERS_PATH).orderByChild("uID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()){
                                User user = dataSnapshotUser.getValue(User.class);

                                user.getLikedLocations().add(location.getId());

                                BackendDatabase.getInstance().saveUser(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    toggle();
                }else {
                    Toast.makeText(context, R.string.general_removed_from_favorites, Toast.LENGTH_LONG).show();

                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child(AppConstants.Firebase.USERS_PATH).orderByChild("uID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()){
                               User user = dataSnapshotUser.getValue(User.class);

                               user.getLikedLocations().remove(location.getId());

                               BackendDatabase.getInstance().saveUser(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
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

    public void setLocation(final Location location){
        this.location = location;

        db = new DataSource(context);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child(AppConstants.Firebase.USERS_PATH).orderByChild("uID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshotUser : dataSnapshot.getChildren()){
                    User user = dataSnapshotUser.getValue(User.class);

                    if(user.getLikedLocations().contains(location.getId())){
                        FavoriteButton.this.setActive(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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

}

