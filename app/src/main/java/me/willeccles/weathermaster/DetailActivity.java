package me.willeccles.weathermaster;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {

	// used to determine which type of details to show
	public static final String TYPE = "type";
	public static final int CURRENT = 0;
	public static final int FORECAST = 1;
	private boolean isFavorite = false;
	private Bundle weatherBundle;
	private Intent shareIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		// determine whether or not the location is a favorite
		isFavorite = getIntent().getBooleanExtra("isFav", false);

		// get the weather stuff ready
		weatherBundle = getIntent().getBundleExtra("weatherBundle");

		// get what kind of thing it is (forecast or current)
		int type = weatherBundle.getInt(TYPE);

		Toolbar myToolbar = findViewById(R.id.my_toolbar);
		myToolbar.setTitle(weatherBundle.getString("location"));
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		if (type == CURRENT) {
			DayFragment df = DayFragment.newInstance(weatherBundle);
			transaction.add(R.id.fragmentFrame, df);
			transaction.commit();
		} else {
			for (int i = 0; i < 5; i++) {
				DayFragment df = DayFragment.newInstance(weatherBundle.getBundle("day" + String.valueOf(i)));
				transaction.add(R.id.fragmentFrame, df);
			}
			transaction.commit();
		}

		// the intent to share from
		shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		String shareStr;
		if (weatherBundle.getInt(TYPE) == CURRENT) {
			shareStr = String.format("It's %.1fº in %s right now!", weatherBundle.getDouble("temp"), weatherBundle.getString("location"));
		} else {
			shareStr = String.format("In %s today, the low will be %.1fº and the high will be %.1fº!", weatherBundle.getBundle("day0").getString("location"), weatherBundle.getBundle("day0").getDouble("temp_min"), weatherBundle.getBundle("day0").getDouble("temp_max"));
		}
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareStr);
		shareIntent.setType("text/plain");
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.detail_toolbar, menu);

		if (isFavorite)
			menu.findItem(R.id.action_favorite).setIcon(getDrawable(R.drawable.ic_favorite_white_24dp));

		return true;
	}

	// used to toggle whether or not the current location is a favorite
	private void toggleFavorite() {
		if (isFavorite) {
			new FavoritesHelper(this).removeFavorite(weatherBundle.getString("location"), weatherBundle.getInt("id"));
			((MenuView.ItemView)findViewById(R.id.action_favorite)).setIcon(getDrawable(R.drawable.ic_favorite_border_white_24dp));
			isFavorite = false;
		} else {
			new FavoritesHelper(this).saveFavorite(weatherBundle.getString("location"), weatherBundle.getInt("id"));
			((MenuView.ItemView)findViewById(R.id.action_favorite)).setIcon(getDrawable(R.drawable.ic_favorite_white_24dp));
			isFavorite = true;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favorite:
				toggleFavorite();
				return true;

			case android.R.id.home:
				// this circumvents the parent activity so that you can always go back to the previous display
				finish();
				return true;

			case R.id.action_share:
				startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
				return true;

			default:
				// If we got here, the user's action was not recognized.
				// Invoke the superclass to handle it.
				return super.onOptionsItemSelected(item);

		}
	}
}
