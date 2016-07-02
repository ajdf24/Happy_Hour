package it.rieger.happyhour.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.model.database.FacebookLoginData;
import it.rieger.happyhour.model.database.LikedLocation;


/**
 * Facade for the database.
 *
 * With this class you can read and write the offline data of the app.
 *
 * Created by sebastian on 25.04.16.
 */
public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumnsFacebook = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_FACEBOOK_ID, DatabaseHelper.COLUMN_FACEBOOK_TOKEN};

    private String[] allColumnsLikedLocations = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_LOCATION_ID};


    /**
     * constructor
     * @param context the context under which the db should be opened
     */
    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * open a db connection
     * @throws SQLException if a error occurs
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * close the db connection
     */
    public void close() {

        dbHelper.close();
    }

    /**
     * create a new database entry for facebook login
     * @param facebooklogindata the data for the entry
     * @return the entry which was saved in the database
     */
    public FacebookLoginData createFacebookLoginData(String facebooklogindata) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FACEBOOK_ID, facebooklogindata);
        long insertId = database.insert(DatabaseHelper.TABLE_FACEBOOK_LOGIN, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_FACEBOOK_LOGIN,
                allColumnsFacebook, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FacebookLoginData newComment = cursorToFacebookLoginData(cursor);
        cursor.close();
        close();
        return newComment;
    }

    public LikedLocation createLikedLocation(long locationID){
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LOCATION_ID, locationID);
        long insertId = database.insert(DatabaseHelper.TABLE_FAVORITE_LOCATIONS, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITE_LOCATIONS,
                allColumnsLikedLocations, DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        LikedLocation newComment = cursorToLikedLocation(cursor);
        cursor.close();
        close();
        return newComment;
    }

    /**
     * delete a db entry for facebook login
     * @param facebookLoginData the entry which should be deleted
     */
    public void deleteFacebookLoginData(FacebookLoginData facebookLoginData) {
        open();
        long id = facebookLoginData.getId();
        database.delete(DatabaseHelper.TABLE_FACEBOOK_LOGIN, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
        close();
    }

    public void deleteLikedLocation(LikedLocation likedLocation){
        open();
        long id = likedLocation.getId();
        database.delete(DatabaseHelper.TABLE_FAVORITE_LOCATIONS, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
        close();
    }

    /**
     * get all entries for facebook login
     * @return a list of all entries
     */
    public List<FacebookLoginData> getAllFacebookLoginData() {
        open();
        List<FacebookLoginData> facebookLoginDatas = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_FACEBOOK_LOGIN,
                allColumnsFacebook, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FacebookLoginData facebookLoginData = cursorToFacebookLoginData(cursor);
            facebookLoginDatas.add(facebookLoginData);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        close();
        return facebookLoginDatas;
    }

    public List<LikedLocation> getAllLikedLocations(){
        open();
        List<LikedLocation> likedLocations = new ArrayList<>();

        try {
            Cursor cursor = database.query(DatabaseHelper.TABLE_FACEBOOK_LOGIN,
                    allColumnsFacebook, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                LikedLocation likedLocation = cursorToLikedLocation(cursor);
                likedLocations.add(likedLocation);
                cursor.moveToNext();
            }
        }catch (NullPointerException e){
            Log.i("Database", "No Locations found");
        }finally {
            close();
            return likedLocations;
        }

    }

    /**
     * Cursor for the fb login data in the database
     * @param cursor
     * @return
     */
    private FacebookLoginData cursorToFacebookLoginData(Cursor cursor) {
        FacebookLoginData facebookLoginData = new FacebookLoginData();
        facebookLoginData.setId(cursor.getLong(0));
        facebookLoginData.setFacebookID(cursor.getString(1));
        facebookLoginData.setFacebookToken(cursor.getString(2));
        return facebookLoginData;
    }

    private LikedLocation cursorToLikedLocation(Cursor cursor) {
        LikedLocation likedLocation = new LikedLocation();
        likedLocation.setId(cursor.getLong(0));
        likedLocation.setLocationID(cursor.getLong(1));
        return likedLocation;
    }
}


