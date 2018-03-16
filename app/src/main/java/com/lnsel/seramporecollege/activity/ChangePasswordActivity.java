package com.lnsel.seramporecollege.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.VolleyLibrary.AppController;
import com.lnsel.seramporecollege.activity.teacher.ApplyLeaveActivity;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.InternetConnectivity;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChangePasswordActivity extends AppCompatActivity {


    ProgressDialog progress;
    SharedManagerUtil session;

    EditText etn_old_password,etn_new_password,etn_confirm_password;
    TextInputLayout til_old_password,til_new_password,til_confirm_password;
    Button add_btn_cancel,add_btn_submit;
    String old_password="",new_password="",confirm_password="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        session=new SharedManagerUtil(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");


        til_old_password=(TextInputLayout)findViewById(R.id.til_old_password);
        til_new_password=(TextInputLayout)findViewById(R.id.til_new_password);
        til_confirm_password=(TextInputLayout)findViewById(R.id.til_confirm_password);

        etn_old_password=(EditText)findViewById(R.id.etn_old_password);
        etn_new_password=(EditText)findViewById(R.id.etn_new_password);
        etn_confirm_password=(EditText)findViewById(R.id.etn_confirm_password);
        add_btn_submit=(Button)findViewById(R.id.add_btn_submit);
        add_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_password=etn_old_password.getText().toString();
                new_password=etn_new_password.getText().toString();
                confirm_password=etn_confirm_password.getText().toString();
                if (!validateOldPassword()) {
                    return;
                }
                if (!validateNewPassword()) {
                    return;
                }
                if (!validateConfirmPassword()) {
                    return;
                }
                if(!InternetConnectivity.isConnectedFast(ChangePasswordActivity.this)){
                    Toast.makeText(getApplicationContext(),"Network Not Available", Toast.LENGTH_LONG).show();
                }else{


                    if(session.getKeyUserType().equalsIgnoreCase("2")){
                        //teacher
                        String username=session.getKeyUserName();
                        String changePasswordUrl=UrlUtil.TEACHER_CHANGE_PASSWORD_URL;
                        changePassword(changePasswordUrl,username,old_password, new_password, confirm_password);
                    }else if(session.getKeyUserType().equalsIgnoreCase("3")){
                        //student
                        String username=session.getKeyUserName();
                        String changePasswordUrl=UrlUtil.STUDENT_CHANGE_PASSWORD_URL;
                        changePassword(changePasswordUrl,username,old_password, new_password, confirm_password);
                    }else if(session.getKeyUserType().equalsIgnoreCase("1")){
                        //admin
                        String username="";
                        String changePasswordUrl="";
                        changePassword(changePasswordUrl,username,old_password, new_password, confirm_password);
                    }

                }
            }
        });
        add_btn_cancel=(Button)findViewById(R.id.add_btn_cancel);
        add_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getKeyUserType().equalsIgnoreCase("2")){
                    new ActivityUtil(ChangePasswordActivity.this).startTeacherActivity();
                }else if(session.getKeyUserType().equalsIgnoreCase("3")){
                    new ActivityUtil(ChangePasswordActivity.this).startStudentActivity();
                }else {
                    new ActivityUtil(ChangePasswordActivity.this).startLoginActivity();
                }
            }
        });


        etn_old_password.addTextChangedListener(new ChangePasswordActivity.MyTextWatcher(etn_old_password));
        etn_new_password.addTextChangedListener(new ChangePasswordActivity.MyTextWatcher(etn_new_password));
        etn_confirm_password.addTextChangedListener(new ChangePasswordActivity.MyTextWatcher(etn_confirm_password));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                if(session.getKeyUserType().equalsIgnoreCase("2")){
                    new ActivityUtil(ChangePasswordActivity.this).startTeacherActivity();
                }else if(session.getKeyUserType().equalsIgnoreCase("3")){
                    new ActivityUtil(ChangePasswordActivity.this).startStudentActivity();
                }else {
                    new ActivityUtil(ChangePasswordActivity.this).startLoginActivity();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            session.logoutUser();
            new ActivityUtil(ApplyLeaveActivity.this).startLoginActivity();
            return true;
        }

        if(id == R.id.action_about){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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
                case R.id.etn_old_password:
                    validateOldPassword();
                    break;
                case R.id.etn_new_password:
                    validateNewPassword();
                    break;
                case R.id.etn_confirm_password:
                    validateConfirmPassword();
                    break;
            }
        }
    }

    private boolean validateOldPassword() {
        if (etn_old_password.getText().toString().trim().isEmpty()) {
            til_old_password.setError("Please enter old password");
            requestFocus(etn_old_password);
            return false;
        } else {
            til_old_password.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateNewPassword() {
        if (etn_new_password.getText().toString().trim().isEmpty()) {
            til_new_password.setError("Please enter new password");
            requestFocus(etn_new_password);
            return false;
        } else {
            til_new_password.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateConfirmPassword() {
        if (etn_confirm_password.getText().toString().trim().isEmpty()) {
            til_confirm_password.setError("Please enter confirm password");
            requestFocus(etn_confirm_password);
            return false;
        }else if (!etn_confirm_password.getText().toString().trim().equalsIgnoreCase(etn_new_password.getText().toString().trim())) {
            til_confirm_password.setError("Confirm password not match");
            requestFocus(etn_confirm_password);
            return false;
        } else {
            til_confirm_password.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }








    public void changePassword(
            final String changePasswordUrl,
            final String username,
            final String old_password,
            final String new_password,
            final String confirm_password){

        progress = new ProgressDialog(this);
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                changePasswordUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response.toString());

                        //{"status":"success","message":"Leave added successfully","record":{"rs":8}}
                        //String str_response = response;

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");
                            String message = jsonObj.getString("message");
                            if(status.equals("success")){
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_SHORT).show();
                                if(session.getKeyUserType().equalsIgnoreCase("2")){
                                    new ActivityUtil(ChangePasswordActivity.this).startTeacherActivity();
                                }else if(session.getKeyUserType().equalsIgnoreCase("3")){
                                    new ActivityUtil(ChangePasswordActivity.this).startStudentActivity();
                                }else {
                                    new ActivityUtil(ChangePasswordActivity.this).startLoginActivity();
                                }
                            }else{
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"Server not Responding, " +
                        "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                //view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("old_password", old_password);
                params.put("new_password", new_password);



                Log.d("username",  username);
                Log.d("old_password", old_password);
                Log.d("new_password", new_password);
                Log.d("confirm_password", confirm_password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onBackPressed() {
        if(session.getKeyUserType().equalsIgnoreCase("2")){
            new ActivityUtil(ChangePasswordActivity.this).startTeacherActivity();
        }else if(session.getKeyUserType().equalsIgnoreCase("3")){
            new ActivityUtil(ChangePasswordActivity.this).startStudentActivity();
        }else {
            new ActivityUtil(ChangePasswordActivity.this).startLoginActivity();
        }
        super.onBackPressed();
    }
}
