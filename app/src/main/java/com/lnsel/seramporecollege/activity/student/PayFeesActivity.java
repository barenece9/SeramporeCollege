package com.lnsel.seramporecollege.activity.student;

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

public class PayFeesActivity extends AppCompatActivity {


    ProgressDialog progress;
    SharedManagerUtil session;
    Spinner type_spinner;
    EditText start_date,end_date,leave_region;
    TextInputLayout til_start_date,til_end_date,til_leave_region;
    Button add_btn_cancel,add_btn_submit;
    String frm_date="",to_date="",selected_leave_id="",selected_leave_type="";

    ArrayAdapter<String> leave_typeAdapter;
    ArrayList<String> leave_typeList = new ArrayList<String>();
    ArrayList<String> leave_typeId = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_fees);
        session=new SharedManagerUtil(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pay Fees");


        til_start_date=(TextInputLayout)findViewById(R.id.til_start_date);
        til_end_date=(TextInputLayout)findViewById(R.id.til_end_date);
        til_leave_region=(TextInputLayout)findViewById(R.id.til_leave_region);

        start_date=(EditText)findViewById(R.id.start_date);
        end_date=(EditText)findViewById(R.id.end_date);
        leave_region=(EditText)findViewById(R.id.leave_region);
        add_btn_submit=(Button)findViewById(R.id.add_btn_submit);
        add_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateStartDate()) {
                    return;
                }
                if (!validateEndDate()) {
                    return;
                }
                if (!validateLeaveReasons()) {
                    return;
                }
                if(selected_leave_id.equalsIgnoreCase("") || selected_leave_id.equalsIgnoreCase("-1")){
                    Toast.makeText(getApplicationContext(),"Please select leave type",Toast.LENGTH_SHORT).show();
                }else if(!InternetConnectivity.isConnectedFast(PayFeesActivity.this)){
                    Toast.makeText(getApplicationContext(),"Network Not Available", Toast.LENGTH_LONG).show();
                }else{
                    String leave_frm_dt=frm_date;
                    String leave_to_dt=to_date;
                    String mstr_leave_type_id=selected_leave_id;
                    String reason_of_leave=leave_region.getText().toString();
                    String lk_approval_status_id="1";//pending
                    String number_of_days=NoOfDay(frm_date,to_date);

                    applyLeave(leave_frm_dt,
                            leave_to_dt,
                            mstr_leave_type_id,
                            reason_of_leave,
                            lk_approval_status_id,
                            number_of_days);
                }
            }
        });
        add_btn_cancel=(Button)findViewById(R.id.add_btn_cancel);
        add_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActivityUtil(PayFeesActivity.this).startStudentActivity();
            }
        });
        type_spinner=(Spinner)findViewById(R.id.type_spinner);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                selected_leave_id = leave_typeId.get(position);
                selected_leave_type=leave_typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        /*type_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_leave_id = leave_typeId.get(i);
                selected_leave_type=leave_typeList.get(i);
            }
        });*/


        leave_typeAdapter = new ArrayAdapter<String>(PayFeesActivity.this,R.layout.spinner_rows, leave_typeList);
        leave_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(leave_typeAdapter);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrmDateDialog();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!frm_date.equalsIgnoreCase("")){
                    ToDateDialog();
                }else {
                    Toast.makeText(getApplicationContext(),"First select start date",Toast.LENGTH_SHORT).show();
                }

            }
        });


        start_date.addTextChangedListener(new MyTextWatcher(start_date));
        end_date.addTextChangedListener(new MyTextWatcher(end_date));
        leave_region.addTextChangedListener(new MyTextWatcher(leave_region));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                new ActivityUtil(PayFeesActivity.this).startStudentActivity();
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
                case R.id.start_date:
                    validateStartDate();
                    break;
                case R.id.end_date:
                    validateEndDate();
                    break;
                case R.id.leave_region:
                    validateLeaveReasons();
                    break;
            }
        }
    }

    private boolean validateStartDate() {
        if (start_date.getText().toString().trim().isEmpty()) {
            til_start_date.setError("Please enter start date");
            requestFocus(start_date);
            return false;
        } else {
            til_start_date.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEndDate() {
        if (end_date.getText().toString().trim().isEmpty()) {
            til_end_date.setError("Please enter end date");
            requestFocus(end_date);
            return false;
        } else {
            til_end_date.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateLeaveReasons() {
        if (leave_region.getText().toString().trim().isEmpty()) {
            til_leave_region.setError("Please enter reason");
            requestFocus(leave_region);
            return false;
        } else {
            til_leave_region.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    public void FrmDateDialog(){

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(PayFeesActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                selectedmonth = selectedmonth + 1;
                String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                start_date.setText(select_date);
                frm_date=select_date;
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        //  mDatePicker.getDatePicker().setMaxDate(max_date);
        mDatePicker.show();
    }

    public void ToDateDialog(){

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        long mim_date=0;
        try {
            Date date1 = myFormat.parse(frm_date);
            mim_date = date1.getTime();
            // System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(PayFeesActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                selectedmonth = selectedmonth + 1;
                String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                end_date.setText(select_date);
                to_date=select_date;
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        // mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.getDatePicker().setMinDate(mim_date);
        mDatePicker.show();
    }

    public String NoOfDay(final String frm_date,final String to_date){
        String no_of_days="0";
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = myFormat.parse(frm_date);
            Date date2 = myFormat.parse(to_date);
            long diff = date2.getTime() - date1.getTime();
            no_of_days=String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            int days=Integer.valueOf(no_of_days)+1;
            no_of_days=String.valueOf(days);
            System.out.println("no_of_days $$$$$$$$$$ : "+no_of_days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return no_of_days;
    }




    public void applyLeave(
            final String leave_frm_dt,
            final String leave_to_dt,
            final String mstr_leave_type_id,
            final String reason_of_leave,
            final String lk_approval_status_id,
            final String number_of_days){

        progress = new ProgressDialog(this);
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.TEACHER_APPLY_LEAVE_URL,
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
                                Toast.makeText(getApplicationContext(),"Leave apply successfully",Toast.LENGTH_SHORT).show();
                                new ActivityUtil(PayFeesActivity.this).startTeacherActivity();
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
                params.put("employee_id", session.getKeyEmployeeId());
                params.put("leave_frm_dt", leave_frm_dt);
                params.put("leave_to_dt", leave_to_dt);

                params.put("mstr_leave_type_id", mstr_leave_type_id);
                params.put("reason_of_leave", reason_of_leave);
                params.put("lk_approval_status_id", lk_approval_status_id);

                params.put("number_of_days", number_of_days);
                params.put("update_by_emp_id", session.getKeyUserID());


                Log.d("employee_id", session.getKeyEmployeeId());
                Log.d("leave_frm_dt", leave_frm_dt);
                Log.d("leave_to_dt", leave_to_dt);

                Log.d("mstr_leave_type_id", mstr_leave_type_id);
                Log.d("reason_of_leave", reason_of_leave);
                Log.d("lk_approval_status_id", lk_approval_status_id);

                Log.d("number_of_days", number_of_days);
                Log.d("update_by_emp_id", session.getKeyUserID());

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onBackPressed() {
        new ActivityUtil(PayFeesActivity.this).startStudentActivity();
        super.onBackPressed();
    }
}
