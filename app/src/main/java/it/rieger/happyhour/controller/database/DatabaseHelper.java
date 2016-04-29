package it.rieger.happyhour.controller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is a helper class for the database.
 *
 * This class is responsible for creating the database.
 *
 * <b>Note:</b> This class is not ready to use jet.
 *
 * TODO: Hier soll später mal die Facebook id des nutzers usw. gespeichert werden.
 *
 * TODO: Zusätzlich kann man noch weitere Daten usw. zwischen speichern. z.B. Lieblings Locations etc.
 * TODO: Einfach mal abwarten was noch so kommt
 *
 * Created by sebastian on 25.04.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //TODO: anpasse
    public static final String TABLE_XXX = "XXX";
    public static final String COLUMN_ID = "_id";

    //TODO: anpassen ggf. weitere
    public static final String COLUMN_XXX = "XXX";

    private static final String DATABASE_NAME = "internal.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    // TODO: Dieses statement muss entprechend angepasst werden
    private static final String DATABASE_CREATE = "create table "
            + TABLE_XXX + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_XXX
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
        db.execSQL(DATABASE_CREATE);
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
        throw new UnsupportedOperationException("Methode nicht implementiert!!!");
    }
}