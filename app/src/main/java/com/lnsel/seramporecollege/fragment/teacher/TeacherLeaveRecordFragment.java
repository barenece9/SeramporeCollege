package com.lnsel.seramporecollege.fragment.teacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.VolleyLibrary.AppController;
import com.lnsel.seramporecollege.adapter.TeacherLeaveCBAdapter;
import com.lnsel.seramporecollege.adapter.TeacherLeaveMonthAdapter;
import com.lnsel.seramporecollege.adapter.TeacherLeaveOBAdapter;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.CommonMethod;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeacherLeaveRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherLeaveRecordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static View rootView;

    SharedManagerUtil session;
    private ProgressDialog progress;
    String TAG="TeacherLeaveRecordFragment";
    Button btn_apply;
    Spinner year_spinner,month_spinner;
    ListView lv_opening_balance,lv_closing_balance,lv_month;

    TeacherLeaveCBAdapter teacherLeaveCBAdapter;
    TeacherLeaveOBAdapter teacherLeaveOBAdapter;
    TeacherLeaveMonthAdapter teacherLeaveMonthAdapter;

    ArrayList<HashMap<String,String>> obLeaveList=new ArrayList<>();
    ArrayList<HashMap<String,String>> cbLeaveList=new ArrayList<>();
    ArrayList<HashMap<String,String>> leaveMonthList=new ArrayList<>();

    ArrayAdapter<String> yearAdapter;
    ArrayList<String> year_List=new ArrayList<String>();

    ArrayAdapter<String> monthAdapter;
    ArrayList<String> month_List=new ArrayList<String>();

    public TeacherLeaveRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeacherLeaveRecordFragment newInstance(String param1, String param2) {
        TeacherLeaveRecordFragment fragment = new TeacherLeaveRecordFragment();
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
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_teacher_leave_record, container, false);
        } catch (InflateException e) {

        }

        session=new SharedManagerUtil(getActivity());

        btn_apply=(Button)rootView.findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(getActivity()).startApplyLeaveActivity();
            }
        });



        lv_opening_balance=(ListView)rootView.findViewById(R.id.lv_opening_balance);
        teacherLeaveOBAdapter = new TeacherLeaveOBAdapter(getActivity(), obLeaveList);
        lv_opening_balance.setAdapter(teacherLeaveOBAdapter);



        lv_closing_balance=(ListView)rootView.findViewById(R.id.lv_closing_balance);
        teacherLeaveCBAdapter = new TeacherLeaveCBAdapter(getActivity(), cbLeaveList);
        lv_closing_balance.setAdapter(teacherLeaveCBAdapter);



        lv_month=(ListView)rootView.findViewById(R.id.lv_month);
        teacherLeaveMonthAdapter = new TeacherLeaveMonthAdapter(getActivity(), leaveMonthList);
        lv_month.setAdapter(teacherLeaveMonthAdapter);



        HashMap<String,String> list1=new HashMap<>();
        list1.put("leave_title","CASUAL LEAVE");
        list1.put("leave_amount","12");
        HashMap<String,String> list2=new HashMap<>();
        list2.put("leave_title","SPECIAL LEAVE");
        list2.put("leave_amount","8");

        obLeaveList.add(list1);
        obLeaveList.add(list2);
        CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_opening_balance);
        teacherLeaveOBAdapter.refresh(obLeaveList);

        cbLeaveList.add(list1);
        cbLeaveList.add(list2);
        CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_closing_balance);
        teacherLeaveCBAdapter.refresh(cbLeaveList);

        leaveMonthList.add(list1);
        leaveMonthList.add(list2);
        CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_month);
        teacherLeaveMonthAdapter.refresh(leaveMonthList);


        year_spinner=(Spinner)rootView.findViewById(R.id.year_spinner);
        year_List.clear();
        year_List.add("2015");
        year_List.add("2016");
        year_List.add("2017");
        year_List.add("2018");
        year_List.add("2019");


        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(year_List.size()>0) {
                    //filterAdapterByYear(year_List.get(position),noticeList);
                    //selectedYear=year_List.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        yearAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, year_List);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(yearAdapter);

        month_spinner=(Spinner)rootView.findViewById(R.id.month_spinner);
        month_List.clear();
        month_List.add("-select-");
        month_List.add("January");
        month_List.add("February");
        month_List.add("March");
        month_List.add("April");

        month_List.add("May");
        month_List.add("June");
        month_List.add("July");
        month_List.add("August");

        month_List.add("September");
        month_List.add("October");
        month_List.add("November");
        month_List.add("December");

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(month_List.size()>0) {
                    //filterAdapterByMonth(month_List.get(position),selectedYear,noticeList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        monthAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, month_List);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(monthAdapter);

       // getProfileDetails();

        return rootView;
    }







    public void getProfileDetails(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.TEACHER_PROFILE_DETAILS_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());

                        //String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){

                                JSONObject record=jsonObj.getJSONObject("record");

                                JSONObject rs=record.getJSONObject("rs");

                                String id=rs.getString("id");
                                String name = rs.getString("name");
                                String department_name = rs.getString("department_name");

                                String userDesignation=rs.getString("employee_type_name");

                                //session.updateProfile(id,name,userDesignation,department_name);

                                progress.dismiss();


                            }else{
                                progress.dismiss();

                            }
                        } catch (JSONException e) {
                            progress.dismiss();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progress.dismiss();
                Toast.makeText(getActivity(),"Server not Responding, Please check your Internet Connection", Toast.LENGTH_LONG).show();
                //view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session.getKeyUserID());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
