package me.willeccles.weathermaster;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment {
	private String w_dayTitle;
	private String w_dayStatus;
	private String w_temp;
	private String w_tempLowHigh;
	private int backgroundId;

	public DayFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param dayBundle The bundle of data for the day.
	 * @return A new instance of fragment DayFragment.
	 */
	public static DayFragment newInstance(Bundle dayBundle) {
		DayFragment fragment = new DayFragment();
		fragment.setArguments(dayBundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			Bundle day = getArguments();
			w_dayStatus = day.getString("status");
			if (w_dayStatus.equals("Clouds"))
				backgroundId = R.drawable.clouds;
			else if (w_dayStatus.equals("Rain"))
				backgroundId = R.drawable.rain;
			else if (w_dayStatus.equals("Clear"))
				backgroundId = R.drawable.clear;
			else if (w_dayStatus.equals("Snow"))
				backgroundId = R.drawable.snow;
			else backgroundId =  0;
			// TODO: add Mist
			w_dayTitle = day.getString("day");
			w_temp = String.format("%.1fº", WeatherWorker.convertTemp(getContext(), day.getDouble("temp")));
			w_tempLowHigh = String.format("Low: %.1fº, High: %.1f",
					WeatherWorker.convertTemp(getContext(), day.getDouble("temp_min")),
					WeatherWorker.convertTemp(getContext(), day.getDouble("temp_max"))
			);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_day, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		ImageView iv = view.findViewById(R.id.backgroundImageView);
		iv.setImageResource(backgroundId);
		TextView wtitle = view.findViewById(R.id.dayNameTitle);
		wtitle.setText(w_dayTitle);
		TextView wstatus = view.findViewById(R.id.statusLabel);
		wstatus.setText(w_dayStatus);
		TextView wtemp = view.findViewById(R.id.tempLabel);
		wtemp.setText(w_temp);
		TextView wlowhigh = view.findViewById(R.id.lowHighLabel);
		wlowhigh.setText(w_tempLowHigh);
	}

}
