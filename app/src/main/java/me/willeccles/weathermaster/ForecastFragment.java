package me.willeccles.weathermaster;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {
	private OnFragmentInteractionListener mListener;

	// parameters
	private String w_daynames[] = new String[5];
	private String w_statuses[] = new String[5];
	private String w_hi_lows[] = new String[5];

	public ForecastFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param forecast The bundle of stuff from the forecast.
	 * @return A new instance of fragment ForecastFragment.
	 */
	public static ForecastFragment newInstance(Bundle forecast) {
		ForecastFragment fragment = new ForecastFragment();
		fragment.setArguments(forecast);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			// see weatherworker for formatting of this bundle
			Bundle w = getArguments();
			for (int i = 0; i < 5; i++) {
				Bundle day = w.getBundle("day" + String.valueOf(i));
				w_daynames[i] = day.getString("day");
				double temps[] = day.getDoubleArray("temps");
				w_statuses[i] = String.format("%.1fº, %s", WeatherWorker.convertTemp(getContext(), temps[1]), day.getString("status"));
				w_hi_lows[i] = String.format("Low: %.1fº High: %.1fº",
						WeatherWorker.convertTemp(getContext(), temps[0]),
						WeatherWorker.convertTemp(getContext(), temps[2]));
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_forecast, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		for (int i = 0; i < 5; i++) {
			TextView dayTitle = view.findViewById(R.id.dayTitle + i);
			dayTitle.setText(w_daynames[i]);
			TextView dayStat = view.findViewById(R.id.status + i);
			dayStat.setText(w_statuses[i]);
			TextView dayTemp = view.findViewById(R.id.temps + i);
			dayTemp.setText(w_hi_lows[i]);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}
}
