package me.willeccles.weathermaster;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoritesActivity extends AppCompatActivity {
	FavoritesHelper fHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		Toolbar myToolbar = findViewById(R.id.fav_toolbar);
		myToolbar.setTitle("Favorite Locations");
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ListView lv = findViewById(R.id.fav_list);
		TextView placeholder = findViewById(R.id.placeholder);
		fHelper = new FavoritesHelper(this);
		Cursor favs = fHelper.getFavorites();
		if (favs.getCount() == 0) {
			placeholder.setVisibility(View.VISIBLE);
			lv.setVisibility(View.INVISIBLE);
		} else {
			placeholder.setVisibility(View.INVISIBLE);
			lv.setVisibility(View.VISIBLE);
			lv.setAdapter(new FavArrayAdapter(this, favs));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fav_toolbar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.clearAll:
				// this should show a popup to confirm whether or not the user wants to clear all favorites
				return true;

			default:
				// If we got here, the user's action was not recognized.
				// Invoke the superclass to handle it.
				return super.onOptionsItemSelected(item);

		}
	}

	public void listItemClicked(View view) {
		Toast.makeText(this, "laskdfjlaskjdf", Toast.LENGTH_SHORT).show();
	}
}
