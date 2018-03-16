package com.lnsel.seramporecollege.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class LoginActivity extends Activity {

    Button activity_login_btn_login;
    ImageButton user_profile_photo;
    RadioGroup typeRadioGroup;
    RadioButton radio_teacher,radio_student;
    TextInputLayout activity_login_til_username,activity_login_til_password;
    EditText activity_login_et_username,activity_login_et_password;

    TextView tv_forgot_password;
    SharedManagerUtil session;
    private ProgressDialog progress;

    //for admin user_type==1 teacher user_type==2 for student user_type==3

    String user_type="2";
    String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        session=new SharedManagerUtil(this);

        progress = new ProgressDialog(this);
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        typeRadioGroup=(RadioGroup)findViewById(R.id.typeRadioGroup);
        radio_teacher=(RadioButton)findViewById(R.id.radio_teacher);
        radio_student=(RadioButton)findViewById(R.id.radio_student);
        tv_forgot_password=(TextView)findViewById(R.id.tv_forgot_password);
        activity_login_et_username = (EditText) findViewById(R.id.activity_login_et_username);
        activity_login_et_password = (EditText) findViewById(R.id.activity_login_et_password);

        activity_login_til_username = (TextInputLayout) findViewById(R.id.activity_login_til_username);
        activity_login_til_password = (TextInputLayout) findViewById(R.id.activity_login_til_password);

        activity_login_et_username.addTextChangedListener(new MyTextWatcher(activity_login_et_username));
        activity_login_et_password.addTextChangedListener(new MyTextWatcher(activity_login_et_password));

        user_profile_photo=(ImageButton)findViewById(R.id.user_profile_photo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_director_48, getApplicationContext().getTheme()));
        } else {
            user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_director_48));
        }
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio_teacher) {
                    user_type="2";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_director_48, getApplicationContext().getTheme()));
                    } else {
                        user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_director_48));
                    }

                } else if(checkedId == R.id.radio_student) {
                     user_type="3";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_student_male_48, getApplicationContext().getTheme()));
                    } else {
                        user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_student_male_48));
                    }
                } else {
                     user_type="2";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_director_48, getApplicationContext().getTheme()));
                    } else {
                        user_profile_photo.setImageDrawable(getResources().getDrawable(R.drawable.icons8_director_48));
                    }
                }

            }

        });

        activity_login_btn_login=(Button)findViewById(R.id.activity_login_btn_login);
        activity_login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateUsername()) {
                    return;
                }
                if (!validatePassword()) {
                    return;
                }
                if(InternetConnectivity.isConnectedFast(LoginActivity.this)){
                    String username=activity_login_et_username.getText().toString();
                    String password=activity_login_et_password.getText().toString();
                    ConstantUtil.fag_index=0;
                    //for teacher user_type==2 for student user_type==1
                    doLogin(username,password,user_type);
                }else{
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_forgot_password.setTextColor(Color.parseColor("#bdbdbd"));
            }
        });
    }


    //********** Text Watcher for Validation *******************//
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.activity_login_et_username:
                    validateUsername();
                    break;
                case R.id.activity_login_et_password:
                    validatePassword();
                    break;
            }
        }
    }



    private boolean validateUsername() {
        if (activity_login_et_username.getText().toString().trim().isEmpty()) {
            activity_login_til_username.setError("Please enter username");
            requestFocus(activity_login_et_username);
            return false;
        } else {
            activity_login_til_username.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (activity_login_et_password.getText().toString().trim().isEmpty()) {
            activity_login_til_password.setError("Please enter password");
            requestFocus(activity_login_et_password);
            return false;
        } else {
            activity_login_til_password.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void doLogin(final String username,final String password,final String user_type){

        progress.show();
        String url="";
        if(user_type.equalsIgnoreCase("2")){
            //teacher login
             url=UrlUtil.TEACHER_LOGIN_URL;
        }else if(user_type.equalsIgnoreCase("3")){
            //student login
             url=UrlUtil.STUDENT_LOGIN_URL;
        }else {
            //admin login
            url="";
        }


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                url,
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

                                String userId = record.getString("id");
                                String userName = record.getString("username");
                                String userType = record.getString("user_type");
                                String statusInner = record.getString("status");


                                if(userType.equalsIgnoreCase("2")){//teacher


                                    session.createTeacherLoginSession(
                                            userId,
                                            userName,
                                            userType);

                                    progress.dismiss();
                                    getTeacherProfileDetails();
                                }else if(userType.equalsIgnoreCase("3")){//student

                                    session.createStudentLoginSession(
                                            userId,
                                            userName,
                                            userType);

                                    progress.dismiss();
                                    getStudentProfileDetails();
                                }



                            }else{
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"Server not Responding, Please check your Internet Connection", Toast.LENGTH_LONG).show();
                //view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("user_type", user_type);

                Log.d("username", username);
                Log.d("password", password);
                Log.d("user_type", user_type);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void getTeacherProfileDetails(){

        progress.setMessage("loading profile...");
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
                                String institute_id=rs.getString("institute_id");
                                String department_id=rs.getString("department_id");

                                String designation_id=rs.getString("designation_id");
                                String employee_type_id=rs.getString("employee_type_id");
                                String category_code=rs.getString("category_code");
                                String academic_department_name=rs.getString("academic_department_name");

                                session.updateTeacherProfile(id,name,userDesignation,department_name,academic_department_id,institute_id,academic_department_name);
                               // session.updateProfile(id,name,userDesignation,department_name,academic_department_id,institute_id);
                               // session.updateProfile(id,name,userDesignation,department_name);

                                progress.dismiss();
                                new ActivityUtil(LoginActivity.this).startTeacherActivity();

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
                Toast.makeText(LoginActivity.this,"Server not Responding, Please check your Internet Connection", Toast.LENGTH_LONG).show();
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


    public void getStudentProfileDetails(){

        progress.setMessage("loading profile...");
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
                                new ActivityUtil(LoginActivity.this).startStudentActivity();

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
                Toast.makeText(LoginActivity.this,"Server not Responding, Please check your Internet Connection", Toast.LENGTH_LONG).show();
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

}
