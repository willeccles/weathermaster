package me.willeccles.weathermaster;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

	// Used to load the 'native-lib' library on application startup.
	static {
		System.loadLibrary("native-lib");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar bar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(bar);
	}

	/**
	 * A native method that is implemented by the 'native-lib' native library,
	 * which is packaged with this application.
	 */
	public native String stringFromJNI();
}
