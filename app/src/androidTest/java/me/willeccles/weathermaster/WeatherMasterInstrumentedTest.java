package me.willeccles.weathermaster;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.Espresso.*;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WeatherMasterInstrumentedTest {
	@Before
	public void setup() {
		Context appContext = getTargetContext();
		Intent i = new Intent(appContext, MainActivity.class);
		appContext.startActivity(i);
	}

	@Test
	public void useAppContext() throws Exception {
		// Context of the app under test.
		Context appContext = getTargetContext();

		assertEquals("me.willeccles.weathermaster", appContext.getPackageName());
	}

	@Test
	public void testWeatherTitle() {
		onView(withId(R.id.zipCode)).perform(typeText("06084"));
		onView(withId(R.id.buttonCurrent)).perform(click());
		onView(withId(R.id.my_toolbar)).check(matches(withToolbarTitle(is("Hartford"))));
	}

	@Test
	public void testSettings() {
		Intent i = new Intent(getTargetContext(), SettingsActivity.class);
		getTargetContext().startActivity(i);
		SharedPreferences prefs = getTargetContext().getSharedPreferences(getTargetContext().getString(R.string.prefFileKey), Context.MODE_PRIVATE);
		int prefTemp = prefs.getInt(getTargetContext().getString(R.string.unitPrefKey), WeatherWorker.C);
		int prefForecast = prefs.getInt(getTargetContext().getString(R.string.map_done_pref_name), SettingsActivity.MAP_FORECAST);

		if (prefTemp == WeatherWorker.C) {
			onView(withId(R.id.celsiusButton)).check(matches(isChecked()));
			onView(withId(R.id.fahrenheitButton)).check(matches(isNotChecked()));
		} else {
			onView(withId(R.id.celsiusButton)).check(matches(isNotChecked()));
			onView(withId(R.id.fahrenheitButton)).check(matches(isChecked()));
		}

		if (prefForecast == SettingsActivity.MAP_FORECAST) {
			onView(withId(R.id.mapCurrentOption)).check(matches(isNotChecked()));
			onView(withId(R.id.mapForecastOption)).check(matches(isChecked()));
		} else {
			onView(withId(R.id.mapCurrentOption)).check(matches(isChecked()));
			onView(withId(R.id.mapForecastOption)).check(matches(isNotChecked()));
		}

		onView(withId(R.id.mapCurrentOption)).perform(click());
		assertEquals(prefs.getInt(getTargetContext().getString(R.string.map_done_pref_name), SettingsActivity.MAP_CURRENT), SettingsActivity.MAP_CURRENT);

		onView(withId(R.id.mapForecastOption)).perform(click());
		assertEquals(prefs.getInt(getTargetContext().getString(R.string.map_done_pref_name), SettingsActivity.MAP_FORECAST), SettingsActivity.MAP_FORECAST);

		onView(withId(R.id.celsiusButton)).perform(click());
		assertEquals(prefs.getInt(getTargetContext().getString(R.string.unitPrefKey), WeatherWorker.C), WeatherWorker.C);

		onView(withId(R.id.fahrenheitButton)).perform(click());
		assertEquals(prefs.getInt(getTargetContext().getString(R.string.unitPrefKey), WeatherWorker.F), WeatherWorker.F);
	}

	// for matching the text in a toolbar
	private static Matcher<Object> withToolbarTitle(final Matcher<String> textMatcher) {
		return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
			@Override public boolean matchesSafely(Toolbar toolbar) {
				return textMatcher.matches(toolbar.getTitle());
			}
			@Override public void describeTo(Description description) {
				description.appendText("with toolbar title: ");
				textMatcher.describeTo(description);
			}
		};
	}

}
