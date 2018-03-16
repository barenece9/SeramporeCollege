package com.lnsel.seramporecollege.fragment.student;

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
import com.lnsel.seramporecollege.adapter.StudentTimeTableAdapter;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentTimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentTimeTableFragment extends Fragment {
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
    ListView lv_notice;
    StudentTimeTableAdapter adapter;
    ArrayList<HashMap<String,String>> timeTableList=new ArrayList<>();
    String TAG="StudentTimeTableFragment";
    private ProgressDialog progress;

    Spinner day_spinner,time_spinner;
    ArrayAdapter<String> dayAdapter;
    ArrayList<String> day_List=new ArrayList<String>();

    ArrayAdapter<String> timeAdapter;
    ArrayList<String> time_List=new ArrayList<String>();
    String selectedDate="";


    public StudentTimeTableFragment() {
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
    public static StudentTimeTableFragment newInstance(String param1, String param2) {
        StudentTimeTableFragment fragment = new StudentTimeTableFragment();
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
        // Inflate the layout for this fragment
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_student_time_table, container, false);
        } catch (InflateException e) {

        }

        session=new SharedManagerUtil(getActivity());
        lv_notice=(ListView)rootView.findViewById(R.id.list_notice);
        adapter = new StudentTimeTableAdapter(getActivity(), timeTableList);
        lv_notice.setAdapter(adapter);


        day_spinner=(Spinner)rootView.findViewById(R.id.day_spinner);
        day_List.clear();
        day_List.add("Sunday");
        day_List.add("Monday");
        day_List.add("Tuesday");
        day_List.add("Wednesday");
        day_List.add("Thursday");
        day_List.add("Friday");
        day_List.add("Saturday");


        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(day_List.size()>0) {
                    filterAdapterByDate(day_List.get(position),timeTableList);
                    // adapter.filter(day_List.get(position));
                    selectedDate=day_List.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        dayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, day_List);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day_spinner.setAdapter(dayAdapter);

        time_spinner=(Spinner)rootView.findViewById(R.id.time_spinner);
        time_List.clear();
        // time_List.add("-select-");
        // time_List.add("9.00 - 10.00");
        // time_List.add("10.00 - 11.00");
        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(time_List.size()>0) {
                    //adapter.filter(day_List.get(position));
                    filterAdapterByTime(time_List.get(position),selectedDate,timeTableList);
                    //current_fin_year = year_List.get(position);
                    // doAttandanceList(current_fin_year);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });



        timeAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, time_List);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(timeAdapter);
        getCurrentDay();
        getDeptTimeTable();

        //getDeptTimeTable();
        return rootView;
    }

    public String getCurrentDay(){
        String dayOfTheWeek="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            dayOfTheWeek = sdf.format(d);
            selectedDate=dayOfTheWeek;
        }catch (Exception e){
        }
        for(int i=0;i<day_List.size();i++){
            if(day_List.get(i).toLowerCase(Locale.getDefault()).contains(dayOfTheWeek.toLowerCase(Locale.getDefault()))){
                day_spinner.setSelection(i);
            }
        }
        System.out.print("dayOfTheWeek====> "+dayOfTheWeek);
        return dayOfTheWeek;
    }

    public void filterAdapterByDate(String key,ArrayList<HashMap<String,String>> timeList){
        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<timeList.size();i++){
            if(timeList.get(i).get("day_slot").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                newList.add(timeList.get(i));
            }
        }
        time_spinner.setSelection(0);
        adapter.refresh(newList);
        // adapter.filterByDate(key);
    }

    public void filterAdapterByTime(String key,String selectedDate,ArrayList<HashMap<String,String>> timeList){

        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<timeList.size();i++){
            if(timeList.get(i).get("day_slot").toLowerCase(Locale.getDefault()).contains(selectedDate.toLowerCase(Locale.getDefault()))){
                newList.add(timeList.get(i));
            }
        }

        ArrayList<HashMap<String,String>> newerList=new ArrayList<>();
        if(key.equalsIgnoreCase("-select-")){
            newerList.addAll(newList);
        }else {
            for(int i=0;i<newList.size();i++){
                if(newList.get(i).get("time_slot").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                    newerList.add(timeList.get(i));
                }
            }
        }
        adapter.refresh(newerList);
        //adapter.filterByTime(key);
    }


    public void getTimeSlotList(){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_TIME_SLOT_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response.toString());

                        //String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                time_List.clear();
                                //time_slot_id_list.clear();
                                time_List.add("-select-");
                                //time_slot_id_list.add("-1");
                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    String id = obj.getString("id");
                                    String time_slot = obj.getString("time_slot");
                                    String status2 = obj.getString("status");

                                    // time_slot_id_list.add(id);
                                    time_List.add(time_slot);

                                }

                                timeAdapter.notifyDataSetChanged();
                                progress.dismiss();


                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"time slot list blank", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                progress.dismiss();
                Toast.makeText(getActivity(),"Server not Responding, Please check your Internet Connection", Toast.LENGTH_LONG).show();
                //view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("academic_department_id",session.getKeyUserAcademicDepartmentId());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void getDeptTimeTable(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.STUDENT_TIME_TABLE_URL,
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

                                timeTableList.clear();
                                JSONObject record = jsonObj.getJSONObject("record");
                                JSONArray rs=record.getJSONArray("rs");
                                for(int i=0;i<rs.length();i++){
                                    JSONObject obj=rs.getJSONObject(i);

                                    HashMap<String,String> notice=new HashMap<>();

                                    String id = obj.getString("id");
                                    String course_id = obj.getString("course_id");
                                    String academic_yr = obj.getString("academic_yr");
                                    String day_id = obj.getString("day_id");
                                    String time_id = obj.getString("time_id");

                                    String subject_id = obj.getString("subject_id");
                                    String paper_id = obj.getString("paper_id");
                                    String day_slot = obj.getString("day_slot");
                                    String status2 = obj.getString("status");

                                    String time_slot = obj.getString("time_slot");
                                    String subject_name = obj.getString("subject_name");
                                    String honours_general = obj.getString("honours_general");
                                    String paper_code = obj.getString("paper_code");
                                    String subject_code = obj.getString("subject_code");
                                    String paper_type = obj.getString("paper_type");
                                    String academic_year = obj.getString("academic_year");
                                    String year=obj.getString("year");

                                    String teacher_name=obj.getString("teacher_name");
                                    String section_name=obj.getString("section_name");


                                    notice.put("id",id);
                                    notice.put("course_id",course_id);
                                    notice.put("day_slot",day_slot);
                                    notice.put("time_slot",time_slot);
                                    notice.put("subject_name",subject_name);
                                    notice.put("subject_code",subject_code);
                                    notice.put("honours_general",honours_general);
                                    notice.put("paper_code",paper_code);
                                    notice.put("paper_type",paper_type);
                                    notice.put("year",year);
                                    notice.put("status2",status2);
                                    notice.put("teacher_name",teacher_name);
                                    notice.put("section_name",section_name);


                                    timeTableList.add(notice);
                                }

                                adapter.refresh(timeTableList);
                                progress.dismiss();
                                getTimeSlotList();
                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"No data found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
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
               // params.put("student_id","3107");
                params.put("student_id",session.getKeyStudentId());
                params.put("academic_section_id",session.getKeyStudentAcadSectionId());
                System.out.println("academic_section_id=== "+session.getKeyStudentAcadSectionId());

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
