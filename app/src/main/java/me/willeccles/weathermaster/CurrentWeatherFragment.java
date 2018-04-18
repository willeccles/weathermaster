package me.willeccles.weathermaster;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentWeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentWeatherFragment extends Fragment {
	private OnFragmentInteractionListener mListener;

	private String w_status = "";
	private String w_temp = "";
	private String w_hi_lo_temp = "";

	public CurrentWeatherFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param weather The weather bundle straight from the WeatherWorker that should be used for this fragment.
	 * @return A new instance of fragment CurrentWeatherFragment.
	 */
	public static CurrentWeatherFragment newInstance(Bundle weather) {
		CurrentWeatherFragment fragment = new CurrentWeatherFragment();
		fragment.setArguments(weather);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			Bundle w = getArguments();
			w_status = w.getString("status");
			w_temp = String.format("Temperature: %.1fº", WeatherWorker.convertTemp(getContext(), w.getDouble("temp")));
			w_hi_lo_temp = String.format("Low: %.1fº, High: %.1fº",
					WeatherWorker.convertTemp(getContext(), w.getDouble("temp_min")),
					WeatherWorker.convertTemp(getContext(), w.getDouble("temp_max"))
			);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_current_weather, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		TextView wstatus = getView().findViewById(R.id.weatherStatus);
		wstatus.setText(w_status);
		TextView wtemp = getView().findViewById(R.id.temperature);
		wtemp.setText(w_temp);
		TextView wtemps = getView().findViewById(R.id.highLowTemperature);
		wtemps.setText(w_hi_lo_temp);

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
