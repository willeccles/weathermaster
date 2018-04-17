package me.willeccles.weathermaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by willeccles on 4/17/18.
 */

public class WeatherWorker extends AsyncTask<String, String, Bundle> {
	static String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast/daily?%s&APPID=%s&cnt=5";
	static String CURRENT_URL = "https://api.openweathermap.org/data/2.5/weather?%s&APPID=%s";
	static String KEY = "0f0d5c7b1e715210f113b114b66acfb3";
	static int C = 0;
	static int F = 1;
	private MainActivity main;

	public WeatherWorker(MainActivity m) {
		main = m;
	}

	/**
	 * A helper method to get the params to give to doInBackground(). If location is "", it will use zip; if that's "" it will use lat/long.
	 * @param forecast If true, gets the 5-day forecast. Otherwise, gets current weather.
	 * @param location The location name.
	 * @param zip The zip code.
	 * @param lat Latitude.
	 * @param lon Longitude.
	 * @return
	 */
	public static String[] getParams(boolean forecast, String location, String zip, double lat, double lon) {
		return new String[]{forecast?"forecast":"current", location, zip, String.valueOf(lat), String.valueOf(lon)};
	}

	/**
	 * Convert a Kelvin value to Celsius.
	 * @param k The value in Kelvin.
	 * @return The value in Celsius.
	 */
	public static native double kelvinToC(double k);

	/**
	 * Convert a Kelvin value to Fahrenheit.
	 * @param k The value in Kelvin.
	 * @return The value in Fahrenheit.
	 */
	public static native double kelvinToF(double k);

	/**
	 * Convert a temperature from Kelvin to the unit the user has selected.
	 * @param c The context to use.
	 * @param K The temperature in Kelvin.
	 * @return The temperature in either Celsius or Fahrenheit.
	 */
	public static double convertTemp(Context c, double K) {
		SharedPreferences sharedpref = c.getSharedPreferences(c.getString(R.string.prefFileKey), Context.MODE_PRIVATE);
		// default to C if none is found
		if (sharedpref.getInt(c.getString(R.string.unitPrefKey), C) == C) {
			return kelvinToC(K);
		} else {
			return kelvinToF(K);
		}
	}

	@Override
	protected Bundle doInBackground(String... strings) {
		String result;
		BufferedReader reader = null;
		HttpURLConnection urlConnection = null;
		Bundle resultBundle = null;

		if (strings.length == 0) return null;

		String queryUrl = "";

		if (strings[0].equals("forecast")) {
			queryUrl = FORECAST_URL;
		} else {
			queryUrl = CURRENT_URL;
		}

		String queryFlag = "";
		String queryParams = "";

		if (!strings[1].trim().isEmpty()) {
			queryFlag = "q=%s";
			queryParams = strings[1].trim();
		} else if (strings[2].trim().matches("\\d{5}")) {
			queryFlag = "zip=%s,us";
			queryParams = strings[2].trim();
		} else {
			queryFlag = "lat=%s&lon=%s";
		}

		try {
			URL url;
			if (queryParams.isEmpty()) {
				url = new URL(String.format(queryUrl, String.format(queryFlag, strings[3], strings[4]), KEY));
			} else {
				url = new URL(String.format(queryUrl, String.format(queryFlag, URLEncoder.encode(queryParams, "UTF-8")), KEY));
			}
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

			resultBundle = new Bundle();

			// parse the JSON
			JSONObject jobject = new JSONObject(msg);

			if (strings[0].equals("current")) {
				JSONObject weatherObj = jobject.getJSONArray("weather").getJSONObject(0);
				JSONObject mainObj = jobject.getJSONObject("main");
				// fill stuff from the current weather
				resultBundle.putString("location", jobject.getString("name"));
				resultBundle.putString("status", weatherObj.getString("main"));
				resultBundle.putDouble("temp", mainObj.getDouble("temp"));
				resultBundle.putDouble("temp_min", mainObj.getDouble("temp_min"));
				resultBundle.putDouble("temp_max", mainObj.getDouble("temp_max"));
			} else {
				JSONArray daysList = jobject.getJSONArray("list");
				SimpleDateFormat sdf = new SimpleDateFormat("E");
				sdf.setTimeZone(TimeZone.getDefault());
				String day;
				resultBundle.putString("location", jobject.getJSONObject("city").getString("name"));
				// fill stuff from the 5-day forecast
				for (int i = 0; i < 5; i++) {
					Bundle dayBundle = new Bundle();
					JSONObject dayObj = daysList.getJSONObject(i);
					Date date = new Date(dayObj.getLong("dt")*1000L);
					day = sdf.format(date);
					dayBundle.putString("day", day);
					dayBundle.putString("status", dayObj.getJSONArray("weather").getJSONObject(0).getString("main"));
					double temps[] = {dayObj.getJSONObject("temp").getDouble("min"), dayObj.getJSONObject("temp").getDouble("day"), dayObj.getJSONObject("temp").getDouble("max")};
					dayBundle.putDoubleArray("temps", temps);
					resultBundle.putBundle("day" + String.valueOf(i), dayBundle);
				}
			}

			return resultBundle;
		} catch (Exception e) {
			Log.e("Exception e: ", Log.getStackTraceString(e));
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (urlConnection != null)
					urlConnection.disconnect();
			} catch (Exception e2) {
				Log.e("Exception e2: ", Log.getStackTraceString(e2));
			}
		}

		return null; // error
	}

	@Override
	protected void onPostExecute(Bundle b) {
		super.onPostExecute(b);
		Intent i = new Intent(main, DetailActivity.class);
		i.putExtra("isFav", false);
		i.putExtra(DetailActivity.TYPE, DetailActivity.CURRENT);
		i.putExtra("weatherBundle", b);
		main.startActivity(i);
	}
}
