package it.rieger.happyhour.controller.database.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.model.Location;

/**
 * Created by sebastian on 24.11.16.
 */

public enum  Firebase {

    FIREBASE;

    static int connections = 0;

    public static void getLocations(List<Integer> ids, final LocationsLoaded callback){

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final List<Location> locations = new ArrayList<>();

        for(Integer id : ids){
            connections++;
            Query postsRef = mDatabase.child("posts").orderByChild("id").equalTo((double) id);
            postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    locations.add(dataSnapshot.getChildren().iterator().next().getValue(Location.class));
                    connections--;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        
        while (true){
            if(connections == 0){
                callback.locationsLoaded(locations);
                break;
            }
        }
    }
}