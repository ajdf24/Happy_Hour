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
            DatabaseHelper.COLUMN_FACEBOOK_ID, DatabaseHelper.COLUMN_FACEBOOK_TOKEN};

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {

        dbHelper.close();
    }
    //create new FacebookLoginData instance;
   // @param FacebookLoginData
    //@return

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

    public void deleteFacebookLoginData(FacebookLoginData facebookLoginData) {
        long id = facebookLoginData.getId();
        System.out.println("FacebookLoginData deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_FACEBOOK_LOGIN, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

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

    private FacebookLoginData cursorToFacebookLoginData(Cursor cursor) {
        FacebookLoginData FacebookLoginData = new FacebookLoginData();
        FacebookLoginData.setId(cursor.getLong(0));
        FacebookLoginData.setFacebookID(cursor.getString(1));
        FacebookLoginData.setFacebookToken(cursor.getString(2));
        return FacebookLoginData;
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
