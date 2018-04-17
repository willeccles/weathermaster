package me.willeccles.weathermaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

	// used to determine which type of details to show
	public static final String TYPE = "type";
	public static final int CURRENT = 0;
	public static final int FORECAST = 1;
	public static String LOCATION = "loc";
	public static String STATUS = "status";
	public static String TEMP = "temp";
	public static String HIGH = "high_temp";
	public static String LOW = "low_temp";
	private boolean isFavorite = false;
	private String locationName = "Details";
	private String weatherStatus = "STATUS";
	private int weatherTemp = 0;
	private int weatherHigh = 0;
	private int weatherLow = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		// determine whether or not the location is a favorite
		isFavorite = getIntent().getBooleanExtra("isFav", false);

		// get the weather stuff ready
		locationName = getIntent().getStringExtra(LOCATION);
		weatherStatus = getIntent().getStringExtra(STATUS);
		weatherTemp = getIntent().getIntExtra(TEMP, -2147483648);
		weatherHigh = getIntent().getIntExtra(HIGH, -2147483648);
		weatherLow = getIntent().getIntExtra(LOW, -2147483648);

		// get what kind of thing it is (forecast or current)
		int type = getIntent().getIntExtra(TYPE, CURRENT);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		myToolbar.setTitle(locationName);
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
