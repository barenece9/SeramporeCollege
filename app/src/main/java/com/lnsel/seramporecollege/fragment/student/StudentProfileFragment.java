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
import android.widget.Button;
import android.widget.ImageView;
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
import com.lnsel.seramporecollege.activity.LoginActivity;
import com.lnsel.seramporecollege.circularimage.CircularImageView;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.InternetConnectivity;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static View rootView;

    TextView tv_roll,tv_phone,tv_uni_reg_no,tv_dept,tv_email,tv_name;
    LinearLayout activity_main_ll_one,activity_main_ll_two,activity_main_ll_three,activity_main_ll_four;
    CircularImageView iv_pf_pic;
    SharedManagerUtil session;

    public String TAG="StudentProfileFragment";
    ProgressDialog progress;
    public StudentProfileFragment() {
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
    public static StudentProfileFragment newInstance(String param1, String param2) {
        StudentProfileFragment fragment = new StudentProfileFragment();
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
            rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);
        } catch (InflateException e) {

        }

        session=new SharedManagerUtil(getActivity());
        Button btn_pf_details=(Button)rootView.findViewById(R.id.btn_pf_details);
        btn_pf_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        iv_pf_pic=(CircularImageView)rootView.findViewById(R.id.iv_pf_pic);

        if(!session.getKeyStudentImage().equalsIgnoreCase("")) {
            String url = UrlUtil.BASE_IMAGE_URL + session.getKeyStudentImage();
            System.out.println("StudentProfileFragment url  "+url);
            Log.e("url",url);
            Picasso.with(getActivity())
                    .load(url)
                    .placeholder(R.drawable.icons8_student_male_48)
                    .resize(100, 100)
                    .centerCrop()
                    .into(iv_pf_pic);
        }

        tv_name=(TextView)rootView.findViewById(R.id.tv_name);
        tv_name.setText(session.getKeyStudentName());

        tv_email=(TextView)rootView.findViewById(R.id.tv_email);
        tv_email.setText(session.getKeyStudentEmail());

        tv_dept=(TextView)rootView.findViewById(R.id.tv_dept);
        tv_dept.setText(session.getKeyStudentAcadDeptName());

        tv_uni_reg_no=(TextView)rootView.findViewById(R.id.tv_uni_reg_no);
        tv_uni_reg_no.setText(session.getKeyStudentUnvRegNo());

        tv_phone=(TextView)rootView.findViewById(R.id.tv_phone);
        tv_phone.setText(session.getKeyStudentPhone());

        tv_roll=(TextView)rootView.findViewById(R.id.tv_roll);
        tv_roll.setText(session.getKeyStudentRollNo());

        activity_main_ll_one=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_one);
        activity_main_ll_two=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_two);
        activity_main_ll_three=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_three);
        activity_main_ll_four=(LinearLayout) rootView.findViewById(R.id.activity_main_ll_four);
        activity_main_ll_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=1;//class attendance
                new ActivityUtil(getActivity()).startStudentActivity();
            }
        });
        activity_main_ll_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=6;//internal marks
                new ActivityUtil(getActivity()).startStudentActivity();
            }
        });
        activity_main_ll_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=7;// time table
                new ActivityUtil(getActivity()).startStudentActivity();
            }
        });
        activity_main_ll_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtil.fag_index=2;//home/profile
                new ActivityUtil(getActivity()).startStudentActivity();
            }
        });

        if(InternetConnectivity.isConnectedFast(getActivity())){
            getStudentProfileDetails();
        }else{
            Toast.makeText(getActivity(),"Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }

        return rootView;
    }




    public void getStudentProfileDetails(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading profile...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.STUDENT_PROFILE_DETAILS_URL,
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

                                JSONObject rs=jsonObj.getJSONObject("record");
                                String id=rs.getString("id");
                                String name = rs.getString("name");
                                String unit_id=rs.getString("unit_id");
                                String academic_institute_id=rs.getString("academic_institute_id");
                                String academic_department_id=rs.getString("academic_department_id");
                                String academic_course_id=rs.getString("academic_course_id");
                                String academic_year_id=rs.getString("academic_year_id");
                                String university_registration_no=rs.getString("university_registration_no");
                                String college_registration_no=rs.getString("college_registration_no");
                                String roll_no=rs.getString("roll_no");

                                String section=rs.getString("section");
                                String date_of_birth=rs.getString("date_of_birth");
                                String father_name=rs.getString("father_name");
                                String mother_name=rs.getString("mother_name");
                                String sex=rs.getString("sex");
                                String phone=rs.getString("phone");

                                String email=rs.getString("email");
                                String parent_phone=rs.getString("parent_phone");
                                String image=rs.getString("image");

                                String sign=rs.getString("sign");
                                String admission_user_id=rs.getString("admission_user_id");
                                String university_sl_no=rs.getString("university_sl_no");

                                String academic_department_name=rs.getString("academic_department");
                                String academic_section_id=rs.getString("academic_section_id");
                                String section_name=rs.getString("section_name");
                                String current_year=rs.getString("current_year");

                                session.updateStudentProfile( id,
                                        name,
                                        unit_id,
                                        academic_institute_id,
                                        academic_department_id,
                                        academic_department_name,
                                        academic_section_id,
                                        section_name,
                                        academic_course_id,
                                        university_registration_no,
                                        college_registration_no,
                                        current_year,
                                        roll_no,
                                        section,
                                        date_of_birth,
                                        sex,
                                        phone,
                                        email,
                                        image);
                                // session.updateProfile(id,name,userDesignation,department_name,academic_department_id,institute_id);
                                // session.updateProfile(id,name,userDesignation,department_name);

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
                params.put("user_id", session.getKeyStudentLoginId());
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
