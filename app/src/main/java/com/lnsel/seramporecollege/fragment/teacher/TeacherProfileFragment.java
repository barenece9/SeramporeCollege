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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.VolleyLibrary.AppController;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.InternetConnectivity;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeacherProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherProfileFragment extends Fragment {
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
    String TAG="TeacherProfileFragment";
    TextView tv_name,tv_dob,tv_dept,tv_emp_type,tv_doj;
    LinearLayout activity_main_ll_one,activity_main_ll_two,activity_main_ll_three,activity_main_ll_four;

    public TeacherProfileFragment() {
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
    public static TeacherProfileFragment newInstance(String param1, String param2) {
        TeacherProfileFragment fragment = new TeacherProfileFragment();
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
            rootView = inflater.inflate(R.layout.fragment_teacher_profile, container, false);
        } catch (InflateException e) {

        }

        session=new SharedManagerUtil(getActivity());

        tv_name=(TextView)rootView.findViewById(R.id.tv_name);
        tv_dob=(TextView)rootView.findViewById(R.id.tv_dob);
        tv_dept=(TextView)rootView.findViewById(R.id.tv_dept);
        tv_emp_type=(TextView)rootView.findViewById(R.id.tv_emp_type);
        tv_doj=(TextView)rootView.findViewById(R.id.tv_doj);

        tv_name.setText(session.getKeyUserFirstName());
        tv_dob.setText("");
        tv_dept.setText(session.getKeyUserDepartment());
        tv_emp_type.setText(session.getKeyUserDesignation());
        tv_doj.setText("");


        activity_main_ll_one=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_one);
        activity_main_ll_two=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_two);
        activity_main_ll_three=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_three);
        activity_main_ll_four=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_four);
        activity_main_ll_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=1;//take attendance
                new ActivityUtil(getActivity()).startTeacherActivity();
            }
        });
        activity_main_ll_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=3;//individual time table
                new ActivityUtil(getActivity()).startTeacherActivity();
            }
        });
        activity_main_ll_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=4;//dept. time table
                new ActivityUtil(getActivity()).startTeacherActivity();
            }
        });
        activity_main_ll_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=0;//home/profile
                new ActivityUtil(getActivity()).startApplyLeaveActivity();
            }
        });


        if(InternetConnectivity.isConnectedFast(getActivity())){
            getProfileDetails();
        }else{
            Toast.makeText(getActivity(),"Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }

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

                                String academic_department_id=rs.getString("academic_department_id");
                                String academic_department_name=rs.getString("academic_department_name");
                                String institute_id=rs.getString("institute_id");
                                String department_id=rs.getString("department_id");

                                String designation_id=rs.getString("designation_id");
                                String employee_type_id=rs.getString("employee_type_id");
                                String category_code=rs.getString("category_code");

                                session.updateTeacherProfile(id,name,userDesignation,department_name,academic_department_id,institute_id,academic_department_name);

                                tv_name.setText(rs.getString("name"));
                                tv_dob.setText(rs.getString("date_of_birth"));
                               // tv_dept.setText(rs.getString("department_name"));
                                tv_dept.setText(rs.getString("academic_department_name"));
                                tv_emp_type.setText(rs.getString("employee_type_name"));
                                tv_doj.setText(rs.getString("date_of_joining"));

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
