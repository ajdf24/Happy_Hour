package it.rieger.happyhour.controller.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This is a helper class for the database.
 *
 * This class is responsible for creating the database.
 *
 * <b>Note:</b> This class is not ready to use jet.
 *
 * Created by sebastian on 25.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = getClass().getSimpleName();

    public static final String TABLE_FACEBOOK_LOGIN = "facebooklogin";
    @SuppressLint(TABLE_FACEBOOK_LOGIN)
    public static final String COLUMN_ID = "_id";
    @SuppressLint(COLUMN_ID)
    public static final String COLUMN_FACEBOOK_ID = "facebookid";
    @SuppressLint(COLUMN_FACEBOOK_ID)
    public static final String COLUMN_FACEBOOK_TOKEN = "facebooktoken";
    @SuppressLint(COLUMN_FACEBOOK_TOKEN)

    public static final String TABLE_FAVORITE_LOCATIONS = "favoritelocations";
    @SuppressLint(TABLE_FAVORITE_LOCATIONS)
    public static final String COLUMN_LOCATION_ID = "locationid";
    @SuppressLint(COLUMN_LOCATION_ID)

    public static final String TABLE_LOCATION_IMAGES = "locationimages";
    @SuppressLint(TABLE_LOCATION_IMAGES)
    public static final String COLUMN_IMAGE_KEY = "imagekey";
    @SuppressLint(COLUMN_IMAGE_KEY)

    private static final String DATABASE_NAME = "internal.db";
    @SuppressLint(DATABASE_NAME)
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE_FACEBOOK_LOGIN = "create table "
            + TABLE_FACEBOOK_LOGIN + "("
            + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_FACEBOOK_ID
            + " text not null,"
            + COLUMN_FACEBOOK_TOKEN
            + " text not null);";
    @SuppressLint(CREATE_TABLE_FACEBOOK_LOGIN)

    private static final String CREATE_TABLE_FAVORITE_LOCATIONS = "create table "
            + TABLE_FAVORITE_LOCATIONS + "("
            + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_LOCATION_ID
            + " integer not null);";
    @SuppressLint(CREATE_TABLE_FAVORITE_LOCATIONS)

    private static final String CREATE_TABLE_LOCATION_IMAGES = "create table "
            + TABLE_LOCATION_IMAGES + "("
            + COLUMN_LOCATION_ID
            + " integer primary key, "
            + COLUMN_IMAGE_KEY
            + " text not null);";
    @SuppressLint(CREATE_TABLE_LOCATION_IMAGES)

    /**
     * constructor
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * create a database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_FACEBOOK_LOGIN);
        db.execSQL(CREATE_TABLE_FAVORITE_LOCATIONS);
    }

    /**
     * update database
     *
     * @param db database
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: implementieren sollte ein Uodate fällig sein
        Log.w(LOG_TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACEBOOK_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_FAVORITE_LOCATIONS);
        onCreate(db);
    }
}
