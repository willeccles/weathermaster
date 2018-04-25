package me.willeccles.weathermaster;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class FavoritesActivity extends AppCompatActivity {
	FavoritesHelper fHelper = new FavoritesHelper(this);
	FavArrayAdapter far;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);

		Toolbar myToolbar = findViewById(R.id.fav_toolbar);
		myToolbar.setTitle("Favorite Locations");
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final ListView lv = findViewById(R.id.fav_list);
		lv.setLongClickable(true);
		final TextView placeholder = findViewById(R.id.placeholder);

		Cursor favs = fHelper.getFavorites();
		far = new FavArrayAdapter(this, favs);

		if (favs.getCount() == 0) {
			placeholder.setVisibility(View.VISIBLE);
		} else {
			placeholder.setVisibility(View.INVISIBLE);
			lv.setAdapter(far);
		}

		final FavoritesActivity thisRef = this;
		SharedPreferences prefs = getSharedPreferences(getString(R.string.prefFileKey), Context.MODE_PRIVATE);
		final boolean _forecast = (prefs.getInt(getString(R.string.map_done_pref_name), SettingsActivity.MAP_FORECAST) == SettingsActivity.MAP_FORECAST);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(thisRef, DetailActivity.class);
				far.getCursor().moveToPosition(position);
				int cid = far.getCursor().getInt(far.getCursor().getColumnIndexOrThrow(FavoritesHelper.COL3));
				new WeatherWorker(thisRef).execute(WeatherWorker.getParams(_forecast, cid));
			}
		});
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(thisRef);
				builder.setMessage(R.string.confirm_delete_title).setMessage(R.string.confirm_delete_message);
				builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing, this is just to dismiss the alert
					}
				});
				builder.setPositiveButton("Yep", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// this is where we actually delete that favorite item
						far.getCursor().moveToPosition(position);
						String name = far.getCursor().getString(far.getCursor().getColumnIndexOrThrow(FavoritesHelper.COL2));
						int id = far.getCursor().getInt(far.getCursor().getColumnIndexOrThrow(FavoritesHelper.COL3));
						new FavoritesHelper(thisRef).removeFavorite(name, id);
						far.removeItem(position);
						far.changeCursor(fHelper.getFavorites());
						if (far.getCursor().getCount() == 0)
							placeholder.setVisibility(View.VISIBLE);
					}
				});
				builder.create().show();
				return true;
			}
		});
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		far.changeCursor(fHelper.getFavorites());
		far.notifyDataSetChanged(); // just in case
		TextView placeholder = findViewById(R.id.placeholder);
		if (far.getCursor().getCount() == 0)
			placeholder.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fav_toolbar, menu);

		MenuItem searchItem = menu.findItem(R.id.action_search);

		SearchManager manager = (SearchManager) FavoritesActivity.this.getSystemService(Context.SEARCH_SERVICE);

		SearchView sview = null;
		if (searchItem != null) {
			sview = (SearchView) searchItem.getActionView();
		}
		if (sview != null) {
			sview.setSearchableInfo(manager.getSearchableInfo(FavoritesActivity.this.getComponentName()));
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onSearchRequested() {
		// just in case we need to do something before searching,
		// otherwise this method is just redundant
		return super.onSearchRequested();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_search:
				// search the favorite locations
				onSearchRequested();
				return true;

			case R.id.clearAll:
				// this should show a popup to confirm whether or not the user wants to clear all favorites
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.confirm_delete_all_message).setTitle(R.string.confirm_delete_all_title);
				builder.setPositiveButton("Yep", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// delete all favorites
						fHelper.removeAllFavorites();
						far.changeCursor(fHelper.getFavorites());
						far.notifyDataSetChanged();
						TextView placeholder = findViewById(R.id.placeholder);
						if (far.getCursor().getCount() == 0)
							placeholder.setVisibility(View.VISIBLE);
					}
				});
				builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				});
				builder.create().show();
				return true;

			default:
				// If we got here, the user's action was not recognized.
				// Invoke the superclass to handle it.
				return super.onOptionsItemSelected(item);

		}
	}
}
