package me.willeccles.weathermaster;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
	MapFragment mapFrag;
	GoogleMap map;
	Location userLocation = null;
	FusedLocationProviderClient mFusedLocationProviderClient;
	public static final int PERMISSION_REQUEST_CODE = 24;
	private int doneOption = SettingsActivity.MAP_CURRENT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Toolbar myToolbar = findViewById(R.id.my_toolbar);
		myToolbar.setTitle("Your Location");
		setSupportActionBar(myToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
		mapFrag.getMapAsync(this);
		mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
		SharedPreferences pref = getSharedPreferences(getString(R.string.prefFileKey), Context.MODE_PRIVATE);
		doneOption = pref.getInt(getString(R.string.map_done_pref_name), SettingsActivity.MAP_CURRENT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map_toolbar, menu);
		return true;
	}

	@Override
	public void onMapReady(GoogleMap mapView) {
		map = mapView;

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
			return;
		}

		map.setMyLocationEnabled(true);
		map.getUiSettings().setAllGesturesEnabled(false);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setIndoorLevelPickerEnabled(false);
		map.getUiSettings().setMapToolbarEnabled(false);

		final AppCompatActivity activity = this; // so that the dialog can finish this activity if needed

		mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
				if (location == null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setMessage(R.string.location_error_dialog).setTitle(R.string.location_error_dialog_title);
					builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// this is actually irrelevent, we just want to show a button
							return;
						}
					});
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							activity.finish();
						}
					});
					builder.create().show();
				} else {
					CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 15, 0, 0));
					map.moveCamera(update);
					userLocation = location;
				}
			}
		});

		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode != PERMISSION_REQUEST_CODE) return;
		final AppCompatActivity activity = this; // so that the dialog can finish this activity if needed

		for (int i = 0; i < grantResults.length; i++) {
			if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.no_permission_dialog_message).setTitle(R.string.no_permission_dialog_message);
				builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// this is actually irrelevent, we just want to show a button
						return;
					}
				});
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						activity.finish();
					}
				});
				builder.create().show();
				break;
			}
		}

		mapFrag.getMapAsync(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_done:
				if (userLocation == null) return false;
				if (doneOption == SettingsActivity.MAP_CURRENT)
					new WeatherWorker(this).execute(WeatherWorker.getParams(false, "", "", userLocation.getLatitude(), userLocation.getLongitude()));
				else
					new WeatherWorker(this).execute(WeatherWorker.getParams(true, "", "", userLocation.getLatitude(), userLocation.getLongitude()));
				return true;

			default:
				// If we got here, the user's action was not recognized.
				// Invoke the superclass to handle it.
				return super.onOptionsItemSelected(item);
		}
	}
}
