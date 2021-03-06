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
import com.lnsel.seramporecollege.adapter.TeacherDeptTimeTableAdapter;
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
 * Use the {@link TeacherDeptTimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherDeptTimeTableFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static View rootView;
    private OnFragmentInteractionListener mListener;

    SharedManagerUtil session;
    private ProgressDialog progress;
    String TAG="TeacherTimeTableFragment";
    ListView lv_notice;
    Spinner day_spinner,time_spinner,teacher_spinner;
    public TeacherDeptTimeTableAdapter adapter;
    ArrayList<HashMap<String,String>> timeList=new ArrayList<>();

    ArrayAdapter<String> dayAdapter;
    ArrayList<String> day_List=new ArrayList<String>();

    ArrayAdapter<String> timeAdapter;
    ArrayList<String> time_List=new ArrayList<String>();
    String selectedDate="";

    ArrayAdapter<String> teacherAdapter;
    ArrayList<String> teacher_name_list=new ArrayList<>();
    ArrayList<String> teacher_id_list=new ArrayList<>();
    String selected_teacher_name="";
    String selected_teacher_id="";

    public TeacherDeptTimeTableFragment() {
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
    public static TeacherDeptTimeTableFragment newInstance(String param1, String param2) {
        TeacherDeptTimeTableFragment fragment = new TeacherDeptTimeTableFragment();
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
        // Inflate the layout for this fragment
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_teacher_dept_time_table, container, false);
        } catch (InflateException e) {

        }

        session=new SharedManagerUtil(getActivity());
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        lv_notice=(ListView)rootView.findViewById(R.id.list_notice);
        adapter = new TeacherDeptTimeTableAdapter(getActivity(), timeList);
        lv_notice.setAdapter(adapter);


        //subject selection=====================================================
        teacher_spinner=(Spinner)rootView.findViewById(R.id.teacher_spinner);
        teacher_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(teacher_name_list.size()>0) {
                    selected_teacher_id=teacher_id_list.get(position);
                    selected_teacher_name=teacher_name_list.get(position);
                    filterAdapterByTeacher(selected_teacher_name,timeList);
                   // getPaperList(selected_subject_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        teacherAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, teacher_name_list);
        teacherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teacher_spinner.setAdapter(teacherAdapter);

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
                    filterAdapterByDate(day_List.get(position),timeList);
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
        //time_List.add("-select-");
        //time_List.add("9.00 - 10.00");
        //time_List.add("10.00 - 11.00");
        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(time_List.size()>0) {
                    //adapter.filter(day_List.get(position));
                    filterAdapterByTime(time_List.get(position),selectedDate,timeList);
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
        getTeacherList();


        return rootView;
    }

    public void filterAdapterByTeacher(String teacherName,ArrayList<HashMap<String,String>> timeList){
        System.out.println("filterAdapterByTeacher teacherName : "+teacherName);
        ArrayList<HashMap<String,String>> teacherFilterList=new ArrayList<>();
        if(teacherName.equalsIgnoreCase("-select-")){
            teacherFilterList.addAll(timeList);
        }else {
            for(int i=0;i<timeList.size();i++){
                if(timeList.get(i).get("teacher_name").toLowerCase(Locale.getDefault()).contains(teacherName.toLowerCase(Locale.getDefault()))){
                    teacherFilterList.add(timeList.get(i));
                }
            }
        }

        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<teacherFilterList.size();i++){
            if(teacherFilterList.get(i).get("day_slot").toLowerCase(Locale.getDefault()).contains(selectedDate.toLowerCase(Locale.getDefault()))){
                newList.add(teacherFilterList.get(i));
            }
        }

        time_spinner.setSelection(0);
        adapter.refresh(newList);
        // adapter.filterByDate(key);
    }

    public void filterAdapterByDate(String key,ArrayList<HashMap<String,String>> timeList){

        ArrayList<HashMap<String, String>> teacherFilterList = new ArrayList<>();
        if(selected_teacher_name.equalsIgnoreCase("-select-")) {
            teacherFilterList.addAll(timeList);
        }else {
            for (int i = 0; i < timeList.size(); i++) {
                if (timeList.get(i).get("teacher_name").toLowerCase(Locale.getDefault()).contains(selected_teacher_name.toLowerCase(Locale.getDefault()))) {
                    teacherFilterList.add(timeList.get(i));
                }
            }
        }


        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<teacherFilterList.size();i++){
            if(teacherFilterList.get(i).get("day_slot").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                newList.add(teacherFilterList.get(i));
            }
        }
        time_spinner.setSelection(0);
        adapter.refresh(newList);
       // adapter.filterByDate(key);
    }

    public void filterAdapterByTime(String key,String selectedDate,ArrayList<HashMap<String,String>> timeList){

        ArrayList<HashMap<String,String>> teacherFilterList=new ArrayList<>();
        if(selected_teacher_name.equalsIgnoreCase("-select-")) {
            teacherFilterList.addAll(timeList);
        }else {
            for (int i = 0; i < timeList.size(); i++) {
                if (timeList.get(i).get("teacher_name").toLowerCase(Locale.getDefault()).contains(selected_teacher_name.toLowerCase(Locale.getDefault()))) {
                    teacherFilterList.add(timeList.get(i));
                }
            }
        }

        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<teacherFilterList.size();i++){
            if(teacherFilterList.get(i).get("day_slot").toLowerCase(Locale.getDefault()).contains(selectedDate.toLowerCase(Locale.getDefault()))){
                newList.add(teacherFilterList.get(i));
            }
        }

        ArrayList<HashMap<String,String>> newerList=new ArrayList<>();
        if(key.equalsIgnoreCase("-select-")){
            newerList.addAll(newList);
        }else {
            for(int i=0;i<newList.size();i++){
                if(newList.get(i).get("time_slot").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                    newerList.add(newList.get(i));
                }
            }
        }
        adapter.refresh(newerList);
        //adapter.filterByTime(key);
    }



    public void getTeacherList(){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.TEACHER_LIST_URL,
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
                                teacher_name_list.clear();
                                teacher_id_list.clear();
                                teacher_name_list.add("-select-");
                                teacher_id_list.add("-1");

                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    String id = obj.getString("id");
                                    String institute_id = obj.getString("institute_id");
                                    String academic_department_id = obj.getString("academic_department_id");
                                    String name = obj.getString("name");
                                    String employee_code = obj.getString("employee_code");
                                    String employee_type_id = obj.getString("employee_type_id");
                                    String status2 = obj.getString("status");


                                    teacher_id_list.add(id);
                                    teacher_name_list.add(name);


                                }

                                teacherAdapter.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged();
                                progress.dismiss();

                                getDeptTimeTable();
                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"teacher list blank", Toast.LENGTH_SHORT).show();
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
                params.put("academic_department_id",session.getKeyUserAcademicDepartmentId());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void getDeptTimeTable(){
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.TEACHER_DEPT_TIME_TABLE_URL,
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

                                timeList.clear();
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


                                    timeList.add(notice);
                                }

                                adapter.refresh(timeList);
                                filterAdapterByDate(selectedDate,timeList);
                                //adapter.notifyDataSetChanged();
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
                params.put("employee_id",session.getKeyEmployeeId());
                params.put("academic_department_id",session.getKeyUserAcademicDepartmentId());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
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
