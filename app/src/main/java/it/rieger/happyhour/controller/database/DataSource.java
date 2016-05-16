package it.rieger.happyhour.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.model.FacebookLoginData;


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
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_FACEBOOK_ID, DatabaseHelper.COLUMN_FACEBOOK_TOKEN};

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
     * create a new database entry
     * @param facebooklogindata the data for the entry
     * @return the entry which was saved in the database
     */
    public FacebookLoginData createFacebookLoginData(String facebooklogindata) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FACEBOOK_ID, facebooklogindata);
        long insertId = database.insert(DatabaseHelper.TABLE_FACEBOOK_LOGIN, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_FACEBOOK_LOGIN,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        FacebookLoginData newComment = cursorToFacebookLoginData(cursor);
        cursor.close();
        return newComment;
    }

    /**
     * delete a db entry
     * @param facebookLoginData the entry which should be deleted
     */
    public void deleteFacebookLoginData(FacebookLoginData facebookLoginData) {
        long id = facebookLoginData.getId();
        System.out.println("FacebookLoginData deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_FACEBOOK_LOGIN, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    /**
     * get all entries from the db
     * @return a list of all entries
     */
    public List<FacebookLoginData> getAllFacebookLoginData() {
        List<FacebookLoginData> facebookLoginDatas = new ArrayList<FacebookLoginData>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_FACEBOOK_LOGIN,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            FacebookLoginData facebookLoginData = cursorToFacebookLoginData(cursor);
            facebookLoginDatas.add(facebookLoginData);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return facebookLoginDatas;
    }

    /**
     * Cursor for the fb login data in the database
     * @param cursor
     * @return
     */
    private FacebookLoginData cursorToFacebookLoginData(Cursor cursor) {
        FacebookLoginData FacebookLoginData = new FacebookLoginData();
        FacebookLoginData.setId(cursor.getLong(0));
        FacebookLoginData.setFacebookID(cursor.getString(1));
        FacebookLoginData.setFacebookToken(cursor.getString(2));
        return FacebookLoginData;
    }
}


