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
 * {@link ForecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForecastFragment extends Fragment {
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM_DAYS = "param1";
	private static final String ARG_PARAM_STATUSES = "param2";
	private static final String ARG_PARAM_TEMPS = "param3";

	private String[] paramDays;
	private String[] paramStatuses;
	private int[] paramTemps;

	private OnFragmentInteractionListener mListener;

	public ForecastFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param days The list of day names.
	 * @param statuses The list of statuses (for each day).
	 * @param temps The list of temps, formatted as: {temp, high, low, temp2, high2, low2, etc.}
	 * @return A new instance of fragment ForecastFragment.
	 */
	public static ForecastFragment newInstance(String[] days, String[] statuses, int[] temps) {
		ForecastFragment fragment = new ForecastFragment();
		Bundle args = new Bundle();
		args.putStringArray(ARG_PARAM_DAYS, days);
		args.putStringArray(ARG_PARAM_STATUSES, statuses);
		args.putIntArray(ARG_PARAM_TEMPS, temps);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			paramDays = getArguments().getStringArray(ARG_PARAM_DAYS);
			paramStatuses = getArguments().getStringArray(ARG_PARAM_STATUSES);
			paramTemps = getArguments().getIntArray(ARG_PARAM_TEMPS);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_forecast, container, false);
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
