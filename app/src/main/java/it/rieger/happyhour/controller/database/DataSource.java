package it.rieger.happyhour.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



import java.util.ArrayList;
import java.util.List;

import it.rieger.happyhour.model.Locationtab;


/**
 * Facade for the database.
 *
 * With this class you can read and write the offline data of the app.
 *
 * TODO: Diese klasse muss zusammen mit dem {@link DatabaseHelper} fertig gestellt werden.
 * TODO: Hierfür wird ein Datenbank Model benötigt, welches noch abgesprochen werden muss.
 *
 * Created by sebastian on 25.04.16.
 */
public class DataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_LocationTab };

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {

        dbHelper.close();
    }
    //create new Locationtab instance;
   // @param Locationtab
    //@return

    public Locationtab createLocationtab(String locationtab) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LocationTab, locationtab);
        long insertId = database.insert(DatabaseHelper.TABLE_LocationTabs, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_LocationTabs,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Locationtab newComment = cursorToLocationtab(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteLocationtab( Locationtab locationtab ) {
        long id = locationtab.getId();
        System.out.println("Locationtab deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_LocationTabs, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Locationtab> getAllLocationtabs() {
        List<Locationtab> locationtabs = new ArrayList<Locationtab>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_LocationTabs,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Locationtab locationtab = cursorToLocationtab(cursor);
            locationtabs.add(locationtab);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return locationtabs;
    }

    private Locationtab cursorToLocationtab(Cursor cursor) {
        Locationtab Locationtab = new Locationtab();
        Locationtab.setId(cursor.getLong(0));
        Locationtab.setLocationtab(cursor.getString(1));
        return Locationtab;
    }
}


//    /**
//     * TODO: Fertig implementieren
//     * create new xxx instance
//     * @param xxx
//     * @return
//     */
//    public XXX createXXX(String xxx) {
//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_XXX, xxx);
//        long insertId = database.insert(DatabaseHelper.TABLE_XXX, null,
//                values);
//        Cursor cursor = database.query(DatabaseHelper.TABLE_XXX,
//                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//        cursor.moveToFirst();
//        XXX newComment = cursorToComment(cursor);
//        cursor.close();
//        return newComment;
//    }

//    /**
//     *
//     * @param cursor cursor for the query XXX
//     * @return instance of {@link xxx}
//     */
//    private XXX cursorToXXX(Cursor cursor) {
//        XXX XXX = new XXX();
//        XXX.setId(cursor.getLong(0));
//        XXX.setXXX(cursor.getString(1));
//        return XXX;
//    }
//}
