package it.rieger.happyhour.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
            DatabaseHelper.COLUMN_XXX };

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
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
}
