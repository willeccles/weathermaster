package me.willeccles.weathermaster;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {
	FavoritesHelper favorites = new FavoritesHelper(this);
	FavArrayAdapter far;
	String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// because launch mode is singleTop, this needs to be done here too
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		Log.d("did we got here??????", "maybe");
		if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
			query = intent.getStringExtra(SearchManager.QUERY);

			// search that data somehow
			Cursor results = favorites.getSearchMatches(query);
			if (results.getCount() == 0) {
				findViewById(R.id.noResultsText).setVisibility(View.VISIBLE);
				return;
			}

			// we know there are items in the cursor, so now we should get them
			findViewById(R.id.noResultsText).setVisibility(View.INVISIBLE);

			far = new FavArrayAdapter(this, results);
			ListView lv = findViewById(R.id.resultsList);

			final ResultsActivity thisRef = this;
			SharedPreferences prefs = this.getSharedPreferences(getString(R.string.prefFileKey), Context.MODE_PRIVATE);
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
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		handleIntent(getIntent());
		TextView placeholder = findViewById(R.id.noResultsText);
		if (far.getCursor().getCount() == 0)
			placeholder.setVisibility(View.VISIBLE);
	}
}
