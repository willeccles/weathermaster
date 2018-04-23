package me.willeccles.weathermaster;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by willeccles on 4/23/18.
 */

public class FavArrayAdapter extends CursorAdapter {
	Context context;
	int cityID = -1;

	public FavArrayAdapter(Context _context, Cursor cursor) {
		super(_context, cursor, false);
		context = _context;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int getCount() {
		return getCursor().getCount();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.fav_row_layout, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView title = view.findViewById(R.id.cityTitle);
		title.setText(cursor.getString(cursor.getColumnIndexOrThrow(FavoritesHelper.COL2)));
		cityID = cursor.getInt(cursor.getColumnIndexOrThrow(FavoritesHelper.COL3));
	}
}
