package me.willeccles.weathermaster;

import android.content.Intent;
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

	public WeatherWorker() {
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

		String queryParams = "";

		if (!strings[1].trim().isEmpty()) {
			queryParams = "q=" + strings[1].trim();
		} else if (strings[2].trim().matches("\\d{5}")) {
			queryParams = "zip=" + strings[2].trim() + ",us";
		} else {
			queryParams = "lat=" + strings[3] + "&lon=" + strings[4];
		}

		try {
			URL url = new URL(String.format(queryUrl, URLEncoder.encode(queryParams, "UTF-8"), KEY));
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
				Log.i("current weather", "weather: " + resultBundle.getString("location") + " " + resultBundle.getString("status") + " " + resultBundle.getDouble("temp") + " " + resultBundle.getDouble("temp_min") + " " + resultBundle.getDouble("temp_max"));
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
	}
}
