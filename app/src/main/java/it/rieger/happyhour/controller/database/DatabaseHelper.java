package it.rieger.happyhour.controller.database;

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

    public static final String TABLE_FACEBOOK_LOGIN = "facebooklogin";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FACEBOOK_ID = "facebookid";
    public static final String COLUMN_FACEBOOK_TOKEN = "facebooktoken";

    public static final String TABLE_FAVORITE_LOCATIONS = "favoritelocations";
    public static final String COLUMN_LOCATION_ID = "locationid";

    public static final String TABLE_LOCATION_IMAGES = "locationimages";
    public static final String COLUMN_IMAGE_KEY = "imagekey";

    private static final String DATABASE_NAME = "internal.db";
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

    private static final String CREATE_TABLE_FAVORITE_LOCATIONS = "create table "
            + TABLE_FAVORITE_LOCATIONS + "("
            + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_LOCATION_ID
            + " integer not null);";

    private static final String CREATE_TABLE_LOCATION_IMAGES = "create table "
            + TABLE_LOCATION_IMAGES + "("
            + COLUMN_LOCATION_ID
            + " integer primary key, "
            + COLUMN_IMAGE_KEY
            + " text not null);";

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
        //db.execSQL(CREATE_TABLE_LOCATION_IMAGES);
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
        //throw new UnsupportedOperationException("Methode nicht implementiert!!!");
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACEBOOK_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_FAVORITE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LOCATION_IMAGES);
        onCreate(db);
    }
}
