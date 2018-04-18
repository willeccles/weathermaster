package me.willeccles.weathermaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mattc on 4/18/2018.
 */

public class FavoritesHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mylist.db";
    public static final String TABLE_NAME = "mylist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";
    public FavoritesHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    //Creates the table for the dara
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ITEM1 TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    //adds the data by taking the values and putting them into the database
    public boolean saveFavorite(String item1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item1);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }
    //get the contents tp then be able to display the data
    public Cursor getFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
    //delete all the data in the db
    public void removeFavorite() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " +TABLE_NAME);
    }
}
