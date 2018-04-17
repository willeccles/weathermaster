package me.willeccles.weathermaster;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentWeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentWeatherFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	public static final String ARG_PARAM_CITY = "param1";
	public static final String ARG_PARAM_STATUS = "param2";
	public static final String ARG_PARAM_TEMP = "param3";
	public static final String ARG_PARAM_HIGH = "param4";
	public static final String ARG_PARAM_LOW = "param5";

	// TODO: Rename and change types of parameters
	private String paramCity;
	private String paramStatus;
	private int paramTemp;
	private int paramHigh;
	private int paramLow;

	private OnFragmentInteractionListener mListener;

	public CurrentWeatherFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param location The name of the location.
	 * @param status The status of the weather (raining, snowing, etc)
	 * @param temp The current temperature.
	 * @param high The high temperature of the day.
	 * @param low The low temperature for the day.
	 * @return A new instance of fragment CurrentWeatherFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static CurrentWeatherFragment newInstance(String location, String status, int temp, int high, int low) {
		CurrentWeatherFragment fragment = new CurrentWeatherFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM_CITY, location);
		args.putString(ARG_PARAM_STATUS, status);
		args.putInt(ARG_PARAM_TEMP, temp);
		args.putInt(ARG_PARAM_HIGH, high);
		args.putInt(ARG_PARAM_LOW, low);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			paramCity = getArguments().getString(ARG_PARAM_CITY);
			paramStatus = getArguments().getString(ARG_PARAM_STATUS);
			paramTemp = getArguments().getInt(ARG_PARAM_TEMP);
			paramHigh = getArguments().getInt(ARG_PARAM_HIGH);
			paramLow = getArguments().getInt(ARG_PARAM_LOW);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_current_weather, container, false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
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
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
