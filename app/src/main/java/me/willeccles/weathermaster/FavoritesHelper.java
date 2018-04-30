package me.willeccles.weathermaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

/**
 * Created by mattc on 4/18/2018.
 */

public class FavoritesHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "mylist.db";
	public static final String TABLE_NAME = "mylist_data";
	public static final String COL1 = "ID";
	public static final String COL2 = "TITLE";
	public static final String COL3 = "CITYID";

	public FavoritesHelper(Context context) {
		super(context,DATABASE_NAME,null,1);
	}

	//Creates the table for the dara
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"TITLE TEXT, CITYID INT)";
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	}

	//adds the data by taking the values and putting them into the database
	public boolean saveFavorite(String name, int cityID) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor prevdata = db.rawQuery(String.format("SELECT ID _id,* FROM %s WHERE %s=%s OR %s=%d;", TABLE_NAME, COL2, DatabaseUtils.sqlEscapeString(name), COL3, cityID), null);
		if (prevdata.getCount() != 0) return false;
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL2, name);
		contentValues.put(COL3, cityID);

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
		Cursor data = db.rawQuery("SELECT ID _id,* FROM " + TABLE_NAME, null);
		return data;
	}

	public Cursor getFavoritesByName(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.rawQuery(String.format("SELECT ID _id,* FROM %s WHERE %s=%s;", TABLE_NAME, COL2, DatabaseUtils.sqlEscapeString(name)), null);
	}

	public void removeFavorite(String name, int cityID) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(String.format("DELETE FROM %s WHERE %s=%s AND %s=%d;", TABLE_NAME, COL2, DatabaseUtils.sqlEscapeString(name), COL3, cityID));
	}

	//delete all the data in the db
	public void removeAllFavorites() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_NAME);
	}

	public boolean isFavorite(String name, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor data = db.rawQuery(String.format("SELECT ID _id,* FROM %s WHERE %s=%s OR %s=%d;", TABLE_NAME, COL2, DatabaseUtils.sqlEscapeString(name), COL3, id), null);
		return data.getCount() != 0;
	}
}
