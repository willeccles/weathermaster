package me.willeccles.weathermaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

	// Used to load the 'native-lib' library on application startup.
	static {
		System.loadLibrary("native-lib");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar myToolbar = findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_toolbar, menu);
		return true;
	}

	// when either of the weather buttons is pressed (since they are similar enough to warrant having the same callback)
	protected void weatherButtonPressed(View v) {
		String city = ((TextView)findViewById(R.id.cityName)).getText().toString().trim();
		String zip = ((TextView)findViewById(R.id.zipCode)).getText().toString().trim();
		switch (v.getId()) {
			case R.id.buttonCurrent:
				// go to the details after getting the weather
				if (city.isEmpty() && zip.isEmpty()) {
					Toast.makeText(this, "Please enter either a city name or a zip code.", Toast.LENGTH_SHORT).show();
					break;
				}

				if (!city.isEmpty()) {
					new WeatherWorker(this).execute(WeatherWorker.getParams(false, city, "", 0.0, 0.0));
				} else {
					new WeatherWorker(this).execute(WeatherWorker.getParams(false, "", zip, 0.0, 0.0));
				}
				break;

			case R.id.buttonForecast:
				if (city.isEmpty() && zip.isEmpty()) {
					Toast.makeText(this, "Please enter either a city name or a zip code.", Toast.LENGTH_SHORT).show();
					break;
				}

				if (!city.isEmpty()) {
					new WeatherWorker(this).execute(WeatherWorker.getParams(true, city, "", 0.0, 0.0));
				} else {
					new WeatherWorker(this).execute(WeatherWorker.getParams(true, "", zip, 0.0, 0.0));
				}
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_location:
				// show the map view
				Intent mapIntent = new Intent(this, MapActivity.class);
				startActivity(mapIntent);
				return true;

			case R.id.action_favorites:
				Intent favIntent = new Intent(this, FavoritesActivity.class);
				startActivity(favIntent);
				return true;

			case R.id.action_settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				return true;

			default:
				// If we got here, the user's action was not recognized.
				// Invoke the superclass to handle it.
				return super.onOptionsItemSelected(item);
		}
	}
}
