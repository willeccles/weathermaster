package me.willeccles.weathermaster;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DetailActivity extends AppCompatActivity implements CurrentWeatherFragment.OnFragmentInteractionListener, ForecastFragment.OnFragmentInteractionListener {

	// used to determine which type of details to show
	public static final String TYPE = "type";
	public static final int CURRENT = 0;
	public static final int FORECAST = 1;
	private boolean isFavorite = false;
	private Bundle weatherBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		// determine whether or not the location is a favorite
		isFavorite = getIntent().getBooleanExtra("isFav", false);

		// get the weather stuff ready
		weatherBundle = getIntent().getBundleExtra("weatherBundle");

		// get what kind of thing it is (forecast or current)
		int type = getIntent().getIntExtra(TYPE, CURRENT);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		myToolbar.setTitle(weatherBundle.getString("location"));
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		if (type == CURRENT) {
			CurrentWeatherFragment cwf = CurrentWeatherFragment.newInstance(weatherBundle);
			transaction.add(R.id.currentfragment, cwf);
			transaction.commit();
			View forecast = (View) findViewById(R.id.forecastfragment);
			forecast.setEnabled(false);
			forecast.setVisibility(View.INVISIBLE);
		} else {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail_toolbar, menu);
		if (isFavorite)
			((MenuView.ItemView)findViewById(R.id.action_favorite)).setIcon(getDrawable(R.drawable.ic_favorite_black_24dp));
		return true;
	}

	// used to toggle whether or not the current location is a favorite
	private void toggleFavorite() {
		if (isFavorite) {
			// TODO set to not be a favorite in SQL here

			((MenuView.ItemView)findViewById(R.id.action_favorite)).setIcon(getDrawable(R.drawable.ic_favorite_border_black_24dp));
		} else {
			// TODO set it to be a favorite in SQL

			((MenuView.ItemView)findViewById(R.id.action_favorite)).setIcon(getDrawable(R.drawable.ic_favorite_black_24dp));
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favorite:
				toggleFavorite();
				return true;

			default:
				// If we got here, the user's action was not recognized.
				// Invoke the superclass to handle it.
				return super.onOptionsItemSelected(item);

		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
