package me.willeccles.weathermaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
	private SharedPreferences sharedPrefs;
	private int lastTempPref = -1;
	private int lastMapDonePref = -1;

	public static final int MAP_CURRENT = 0;
	public static final int MAP_FORECAST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		Toolbar myToolbar = findViewById(R.id.my_toolbar);
		myToolbar.setTitle("Settings");
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		sharedPrefs = getSharedPreferences(getString(R.string.prefFileKey), Context.MODE_PRIVATE);
		RadioGroup rg = findViewById(R.id.radioGroup);
		RadioGroup rg2 = findViewById(R.id.mapOptionRadioGroup);
		lastTempPref = sharedPrefs.getInt(getString(R.string.unitPrefKey), WeatherWorker.C);
		lastMapDonePref = sharedPrefs.getInt(getString(R.string.map_done_pref_name), MAP_CURRENT);
		if (lastTempPref == WeatherWorker.C) {
			rg.check(R.id.celsiusButton);
		} else {
			rg.check(R.id.fahrenheitButton);
		}
		if (lastMapDonePref == MAP_CURRENT) {
			rg2.check(R.id.mapCurrentOption);
		} else {
			rg2.check(R.id.mapForecastOption);
		}
	}

	protected void tempRadioClicked(View v) {
		RadioGroup rg = findViewById(R.id.radioGroup);
		if (rg.getCheckedRadioButtonId() == R.id.fahrenheitButton && lastTempPref == WeatherWorker.C) {
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putInt(getString(R.string.unitPrefKey), WeatherWorker.F);
			editor.apply();
		} else if (rg.getCheckedRadioButtonId() == R.id.celsiusButton && lastTempPref == WeatherWorker.F) {
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putInt(getString(R.string.unitPrefKey), WeatherWorker.C);
			editor.apply();
		} else {
			return;
		}
	}

	protected void mapDoneRadioClicked(View v) {
		RadioGroup rg = findViewById(R.id.mapOptionRadioGroup);
		if (rg.getCheckedRadioButtonId() == R.id.mapCurrentOption && lastMapDonePref == MAP_FORECAST) {
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putInt(getString(R.string.map_done_pref_name), MAP_CURRENT);
			editor.apply();
		} else if (rg.getCheckedRadioButtonId() == R.id.mapForecastOption && lastMapDonePref == MAP_CURRENT) {
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putInt(getString(R.string.map_done_pref_name), MAP_FORECAST);
			editor.apply();
		} else {
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings_toolbar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_about:
				Intent aboutIntent = new Intent(this, AboutActivity.class);
				startActivity(aboutIntent);
				return true;

			default:
				// If we got here, the user's action was not recognized.
				// Invoke the superclass to handle it.
				return super.onOptionsItemSelected(item);
		}
	}
}
