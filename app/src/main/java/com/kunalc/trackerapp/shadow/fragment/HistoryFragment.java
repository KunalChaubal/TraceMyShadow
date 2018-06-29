package com.kunalc.trackerapp.shadow.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kunalc.trackerapp.shadow.R;
import com.kunalc.trackerapp.shadow.activity.ActivityBase;
import com.kunalc.trackerapp.shadow.adapter.ListCustomAdapter;
import com.kunalc.trackerapp.shadow.bean.TrackDetails;
import com.kunalc.trackerapp.shadow.database.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    DatePickerDialog.OnDateSetListener date;
    private OnFragmentInteractionListener mListener;
    ListView listView;
    ListCustomAdapter listCustomAdapter;
    List<TrackDetails> trackDetailsList;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        dateView = (TextView) rootView.findViewById(R.id.txt_date);

        String myFormat = "EEE, d MMM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        calendar = Calendar.getInstance();
        dateView.setText(sdf.format(calendar.getTime()));

        initiateDatePicker(rootView);


        trackDetailsList = getLocationList();

        listView = (ListView) rootView.findViewById(R.id.listView);
        listCustomAdapter = new ListCustomAdapter(activityBase, trackDetailsList);
        listView.setAdapter(listCustomAdapter);
        return rootView;
    }

    private List<TrackDetails> getLocationList() {
        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity().getApplicationContext());
        List<TrackDetails> trackDetailsList = databaseHandler.getAllTrackDetailss();
        Log.e("KunalC","List: "+trackDetailsList);
        return trackDetailsList;
    }

    public void initiateDatePicker(View rootView) {
        calendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                applyfilter(calendar);
            }

        };

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private void applyfilter(Calendar calendar){
        //trackDetailsList.clear();
       // TrackDetails trackBean = new TrackDetails();
       //trackDetailsList =  getLocationList();
        List<TrackDetails> temp = getLocationList();
        //temp = getLocationList();
        trackDetailsList.clear();
        for(TrackDetails trackBean : temp) {
          // Date visitedDate = trackBean.getVisitedDate();
            Calendar cal_visited=Calendar.getInstance();
            cal_visited.setTime(trackBean.getVisitedDate());

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            cal_visited.set(Calendar.HOUR_OF_DAY, 0);
            cal_visited.set(Calendar.MINUTE, 0);
            cal_visited.set(Calendar.SECOND, 0);
            cal_visited.set(Calendar.MILLISECOND, 0);

            if(calendar.getTime().equals(cal_visited.getTime()))
            {

                trackDetailsList.add(trackBean);
            }




        }
       // trackDetailsList.clear();
       /* for(int i=0;i<trackDetailsList.size();i++){
            TrackDetails trackDetails = trackDetailsList.get(i);
            Calendar cal_visited=Calendar.getInstance();
            cal_visited.setTime(trackDetails.getVisitedDate());

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            cal_visited.set(Calendar.HOUR_OF_DAY, 0);
            cal_visited.set(Calendar.MINUTE, 0);
            cal_visited.set(Calendar.SECOND, 0);
            cal_visited.set(Calendar.MILLISECOND, 0);

            if(calendar.getTime().equals(cal_visited.getTime()))
            {

                temp.add(trackDetails);
            }
        }*/
        //trackDetailsList.clear();
        //trackDetailsList = temp;
        Log.e("KunalC","New list: "+trackDetailsList);
        //listCustomAdapter = new ListCustomAdapter(activityBase, trackDetailsList);
       // listCustomAdapter.notify();
        //listView.invalidateViews();
        listCustomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        //listCustomAdapter.notifyDataSetChanged();
    }

    private void updateLabel() {

        String myFormat = "EEE, d MMM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateView.setText(sdf.format(calendar.getTime()));
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
    ActivityBase activityBase;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityBase = (ActivityBase) context;
    }

}
