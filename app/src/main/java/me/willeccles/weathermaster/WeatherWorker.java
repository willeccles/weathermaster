package me.willeccles.weathermaster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by willeccles on 4/17/18.
 */

public class WeatherWorker extends AsyncTask<String, String, String> {
	static String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast?%s&APPID=%s";
	static String CURRENT_URL = "https://api.openweathermap.org/data/2.5?weather?%s&APPID=%s";
	static String KEY = "0f0d5c7b1e715210f113b114b66acfb3";
	private MainActivity main;

	public WeatherWorker(MainActivity m) {
		main = m;
	}

	/**
	 * Get the 5-day forecast. Parameters will be used in the order of precedence they are written in. If the location is "" and zip code is 0, the latitude and longitude will be used.
	 * If location is supplied and everything else is 0 or 0.0, the location will be used.
	 * @param location The location name.
	 * @param zip The zip code.
	 * @param lat The latitude.
	 * @param lon The longitude.
	 * @return The bundle of returned values.
	 */
	public Bundle getForecast(String location, int zip, double lat, double lon) {
		return doInBackground("forecast", location, String.valueOf(zip), String.valueOf(lat), String.valueOf(lon));
	}

	/**
	 * Get the current weather. Parameters will be used in the order of precedence they are written in. If the location is "" and zip code is 0, the latitude and longitude will be used.
	 * If location is supplied and everything else is 0 or 0.0, the location will be used.
	 * @param location The location name.
	 * @param zip The zip code.
	 * @param lat The latitude.
	 * @param lon The longitude.
	 * @return The bundle of returned values.
	 */
	public Bundle getCurrent(String location, int zip, double lat, double lon) {
		return doInBackground("current", location, String.valueOf(zip), String.valueOf(lat), String.valueOf(lon));
	}

	@Override
	protected Bundle doInBackground(String... strings) {
		String result;
		BufferedReader reader = null;
		HttpURLConnection urlConnection = null;

		if (strings.length == 0) return null;

		String queryUrl = "";

		if (strings[0].equals("forecast")) {
			queryUrl = FORECAST_URL;
		} else {
			queryUrl = CURRENT_URL;
		}

		try {
			URL url = new URL(String.format(queryUrl, URLEncoder.encode(strings[0], "UTF-8"), KEY));
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			InputStream in = urlConnection.getInputStream();
			if (in == null) return null;
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			String msg = "";
			while ((line = reader.readLine()) != null) {
				msg += line + '\n';
			}
			if (msg.length() == 0) return null;
			return msg;
		} catch (Exception e) {
			Log.e("Exception e: ", e.getMessage());
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (urlConnection != null)
					urlConnection.disconnect();
			} catch (Exception e2) {
				Log.e("Exception e2: ", e2.getMessage());
			}
		}

		return null; // error
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
	}
}
