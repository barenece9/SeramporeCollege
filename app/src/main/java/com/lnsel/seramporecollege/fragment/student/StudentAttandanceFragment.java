package com.lnsel.seramporecollege.fragment.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.VolleyLibrary.AppController;
import com.lnsel.seramporecollege.adapter.StudentAttandanceAdapter;
import com.lnsel.seramporecollege.adapter.StudentFacultyContactAdapter;
import com.lnsel.seramporecollege.adapter.StudentSubjectPaperAdapter;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONArray;
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
 * Use the {@link StudentAttandanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentAttandanceFragment extends Fragment {
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
    String TAG="StudentAttandanceFragment";
    ListView list_attandance,list_subject_paper;
    StudentSubjectPaperAdapter adapter;

    StudentAttandanceAdapter attandanceAdapter;
    ArrayList<HashMap<String,String>> subjectPaperList=new ArrayList<>();
    ArrayList<HashMap<String,String>> attendanceList=new ArrayList<>();

    TextView tv_subject,tv_paper,tv_class,tv_present,tv_percentage;
    CardView cv_present_count;
    Spinner subject_spinner;

    ArrayAdapter<String> subjectAdapter;
    ArrayList<String> subject_paper_name_list=new ArrayList<>();
    ArrayList<String> subject_id_list=new ArrayList<>();
    ArrayList<String> paper_id_list=new ArrayList<>();
    String selected_subject_id="";
    String selected_paper_id="";

    public StudentAttandanceFragment() {
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
    public static StudentAttandanceFragment newInstance(String param1, String param2) {
        StudentAttandanceFragment fragment = new StudentAttandanceFragment();
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
            rootView = inflater.inflate(R.layout.fragment_student_attandance, container, false);
        } catch (InflateException e) {

        }
        session=new SharedManagerUtil(getActivity());
        cv_present_count=(CardView)rootView.findViewById(R.id.cv_present_count);
        cv_present_count.setVisibility(View.GONE);
        tv_subject=(TextView)rootView.findViewById(R.id.tv_subject);
        tv_paper=(TextView)rootView.findViewById(R.id.tv_paper);
        tv_class=(TextView)rootView.findViewById(R.id.tv_class);
        tv_present=(TextView)rootView.findViewById(R.id.tv_present);
        tv_percentage=(TextView)rootView.findViewById(R.id.tv_percentage);

        list_subject_paper=(ListView)rootView.findViewById(R.id.list_subject_paper);
        adapter = new StudentSubjectPaperAdapter(getActivity(), subjectPaperList);
        list_subject_paper.setAdapter(adapter);
        list_subject_paper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                doAttendanceList(i);
            }
        });

        //subject selection=====================================================
        subject_spinner=(Spinner)rootView.findViewById(R.id.subject_spinner);
        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(subject_paper_name_list.size()>0) {
                    doAttendanceList(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        subjectAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, subject_paper_name_list);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject_spinner.setAdapter(subjectAdapter);

        list_attandance=(ListView)rootView.findViewById(R.id.list_attandance);
        attandanceAdapter = new StudentAttandanceAdapter(getActivity(), attendanceList);
        list_attandance.setAdapter(attandanceAdapter);

        doSubjectPaperList();
        return rootView;
    }



    public void doSubjectPaperList(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.STUDENT_SUBJECT_PAPER_LIST_URL,
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

                                subjectPaperList.clear();
                                subject_paper_name_list.clear();

                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    HashMap<String,String> notice=new HashMap<>();

                                    String id = obj.getString("id");
                                    String batch_id = obj.getString("batch_id");
                                    String name = obj.getString("name");
                                    String code = obj.getString("code");
                                    String honours_general = obj.getString("honours_general");
                                    String paper_code = obj.getString("paper_code");

                                    String paper_type = obj.getString("paper_type");
                                    String paper_id = obj.getString("paper_id");
                                    String status2 = obj.getString("status");

                                    notice.put("id",id);
                                    notice.put("batch_id",batch_id);
                                    notice.put("name",name);
                                    notice.put("code",code);
                                    notice.put("honours_general",honours_general);
                                    notice.put("paper_code",paper_code);
                                    notice.put("paper_type",paper_type);
                                    notice.put("paper_id",paper_id);

                                    subject_paper_name_list.add(name+" ("+paper_code+")");
                                    subjectPaperList.add(notice);
                                }

                                subjectAdapter.notifyDataSetChanged();
                               // adapter.refresh(subjectPaperList);

                                progress.dismiss();


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
                System.out.println("student_id : "+session.getKeyStudentId());
                params.put("student_id",session.getKeyStudentId());
                //params.put("student_id","3750");
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void doAttendanceList(final int position){

        final String subject_id=subjectPaperList.get(position).get("id");
        final String paper_id=subjectPaperList.get(position).get("paper_id");

        final String subject_name=subjectPaperList.get(position).get("name");
        final String paper_code=subjectPaperList.get(position).get("paper_code");

       // progress = new ProgressDialog(getActivity());
        //progress.setMessage("loading...");
        //progress.setCancelable(false);
        //progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.STUDENT_CLASS_WISE_ATTENDANCE_URL,
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

                                attendanceList.clear();




                                JSONArray record=jsonObj.getJSONArray("record");
                                int total_class=record.length();
                                int total_present=0;

                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    HashMap<String,String> notice=new HashMap<>();

                                    String id = obj.getString("id");
                                    String attendence_id = obj.getString("attendence_id");
                                    String attendence_date = obj.getString("attendence_date");
                                    String time_slot = obj.getString("time_slot");
                                    String attendance_status = obj.getString("status");
                                    if(attendance_status.equalsIgnoreCase("1")){
                                        total_present=total_present+1;
                                    }
                                    String year = obj.getString("year");
                                    String subject_id = obj.getString("subject_id");
                                    String paper_id = obj.getString("paper_id");
                                    notice.put("id",id);
                                    notice.put("attendence_id",attendence_id);
                                    notice.put("attendence_date",attendence_date);
                                    notice.put("time_slot",time_slot);
                                    notice.put("attendance_status",attendance_status);
                                    notice.put("year",year);
                                    notice.put("subject_id",subject_id);
                                    notice.put("paper_id",paper_id);

                                    attendanceList.add(notice);
                                }

                                Double percentage=Double.valueOf((total_present/total_class)*100);
                                cv_present_count.setVisibility(View.VISIBLE);
                                tv_subject.setText(subject_name);
                                tv_paper.setText(paper_code);
                                tv_class.setText(String.valueOf(total_class));
                                tv_present.setText(String.valueOf(total_present));
                                tv_percentage.setText(String.valueOf(percentage));
                                attandanceAdapter.refresh(attendanceList);

                                progress.dismiss();


                            }else{
                                progress.dismiss();
                                cv_present_count.setVisibility(View.GONE);
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
              //  params.put("student_id",session.getKeyStudentId());
                params.put("student_id","3174");
                params.put("subject_id","5");
                params.put("paper_id","312");
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
