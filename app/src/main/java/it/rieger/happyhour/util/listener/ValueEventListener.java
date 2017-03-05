package it.rieger.happyhour.util.listener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Only a helper class, which implements {@link com.google.firebase.database.ValueEventListener}.
 * In the code there are now only the methods which are needed.
 * Created by sebastian on 05.03.17.
 */

public class ValueEventListener implements com.google.firebase.database.ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
