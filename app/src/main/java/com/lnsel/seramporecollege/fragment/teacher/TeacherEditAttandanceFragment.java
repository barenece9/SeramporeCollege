package com.lnsel.seramporecollege.fragment.teacher;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.lnsel.seramporecollege.activity.teacher.ApplyLeaveActivity;
import com.lnsel.seramporecollege.adapter.TeacherEditAttendanceAdapter;
import com.lnsel.seramporecollege.adapter.TeacherTakeAttendanceAdapter;
import com.lnsel.seramporecollege.data.Student;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.CommonMethod;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.DialogUtil;
import com.lnsel.seramporecollege.utils.InternetConnectivity;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeacherEditAttandanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherEditAttandanceFragment extends Fragment {
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
    ProgressDialog progress;
    ListView lv_student,lv_attendance_list;
    Button btn_select_all,btn_deselect_all;
    EditText attendance_date;
    Spinner spinner_time_slot;

   // Select subject -> paper -> institute->  h/g->  -> year(1,2,3) ->  section->

    TeacherEditAttendanceAdapter attendanceAdapter;
    ArrayList<Student> studentList=new ArrayList<>();


    ArrayAdapter<String> time_slotAdapter;
    ArrayList<String> time_slot_name_list=new ArrayList<>();
    ArrayList<String> time_slot_id_list=new ArrayList<>();
    String selected_time_slot_name="";
    String selected_time_slot_id="";


    String selected_attendance_date="";
    String selected_attendance_id="";
    String selected_year="1";//1,2,3

    public TeacherEditAttandanceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TeacherEditAttandanceFragment newInstance(String param1, String param2) {
        TeacherEditAttandanceFragment fragment = new TeacherEditAttandanceFragment();
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
            rootView = inflater.inflate(R.layout.fragment_teacher_edit_attendance, container, false);
        } catch (InflateException e) {

        }
        session=new SharedManagerUtil(getActivity());

        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        progress.setCanceledOnTouchOutside(false);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        selected_attendance_date = df.format(c.getTime());
        attendance_date=(EditText)rootView.findViewById(R.id.attendance_date);
        //attendance_date.setText(selected_attendance_date);
        attendance_date.setText(CommonMethod.dateConversion(selected_attendance_date));
        attendance_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrmDateDialog();
            }
        });


        lv_student=(ListView)rootView.findViewById(R.id.lv_student);
        lv_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // for(int k=0;k<studentList.size();k++){
                    if(studentList.get(i).getStatus()){
                        studentList.get(i).setStatus(false);
                    }else {
                        studentList.get(i).setStatus(true);
                    }
               // }
                attendanceAdapter.refreshList(studentList);
            }
        });
        attendanceAdapter = new TeacherEditAttendanceAdapter(getActivity(),studentList);
        lv_student.setAdapter(attendanceAdapter);
        CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);

        Button btn_submit=(Button)rootView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rolls="";
                for(int i=0;i<studentList.size();i++){
                    if(studentList.get(i).getStatus()){
                        if(rolls.equalsIgnoreCase("")){
                            rolls=studentList.get(i).getId()+"-1";
                        }else {
                            rolls=rolls+","+studentList.get(i).getId()+"-1";
                        }
                    }else {
                        if(rolls.equalsIgnoreCase("")){
                            rolls=studentList.get(i).getId()+"-0";
                        }else {
                            rolls=rolls+","+studentList.get(i).getId()+"-0";
                        }
                    }
                }
                System.out.println("rolls : ==  "+rolls);
                Log.e("rolls ",rolls);


                if (selected_time_slot_id.equalsIgnoreCase("") || selected_time_slot_id.equalsIgnoreCase("-1")) {

                    DialogUtil.customDialog(getActivity(),"Time slot can't be blank");
                }else if (!InternetConnectivity.isConnectedFast(getActivity())) {

                    DialogUtil.customDialog(getActivity(),"Check your internal connection");
                }else if (studentList.size()==0) {
                    DialogUtil.customDialog(getActivity(),"Student list blank");
                }else {
                    SubmitDialog(selected_attendance_id,rolls);
                }
                //new ActivityUtil(getActivity()).startTakeStudentsAttendance();
               // getStudentList();
            }
        });
        Button btn_cancel=(Button)rootView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelDialog();
               // ConstantUtil.fag_index=0;//home/profile
               // new ActivityUtil(getActivity()).startTeacherActivity();
            }
        });
        btn_deselect_all=(Button)rootView.findViewById(R.id.btn_deselect_all);
        btn_deselect_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               for(int i=0;i<studentList.size();i++){
                   studentList.get(i).setStatus(false);
               }
                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                attendanceAdapter.refreshList(studentList);
                btn_deselect_all.setVisibility(View.GONE);
                btn_select_all.setVisibility(View.VISIBLE);

            }
        });
        btn_select_all=(Button)rootView.findViewById(R.id.btn_select_all);
        btn_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<studentList.size();i++){
                    studentList.get(i).setStatus(true);
                }
                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                attendanceAdapter.refreshList(studentList);
                btn_deselect_all.setVisibility(View.VISIBLE);
                btn_select_all.setVisibility(View.GONE);
            }
        });


        //time_slot selection=====================================================
        spinner_time_slot=(Spinner)rootView.findViewById(R.id.spinner_time_slot);
        spinner_time_slot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(time_slot_name_list.size()>0) {
                    selected_time_slot_id=time_slot_id_list.get(position);
                    selected_time_slot_name=time_slot_name_list.get(position);
                    if(!selected_time_slot_name.equalsIgnoreCase("-select-")){
                        getStudentList(selected_attendance_date,selected_time_slot_id);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        time_slotAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, time_slot_name_list);
        time_slotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time_slot.setAdapter(time_slotAdapter);

        getTimeSlotList();

        return rootView;
    }


    public void SubmitDialog(final String selected_attendance_id,final String rolls){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_title=(TextView)dialog.findViewById(R.id.dialog_title);
        dialog_title.setText("Confirmation !");
        TextView dialog_common_header = (TextView) dialog.findViewById(R.id.dialog_common_header);
        dialog_common_header.setText("Do you want to update?");

        Button dialog_confirmation_cancel = (Button) dialog.findViewById(R.id.dialog_confirmation_cancel);
        dialog_confirmation_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        final Button dialog_confirmation_ok = (Button) dialog.findViewById(R.id.dialog_confirmation_ok);
        dialog_confirmation_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //successfully submitted.............
                submitAttendance(selected_attendance_id,rolls);
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void CancelDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_title=(TextView)dialog.findViewById(R.id.dialog_title);
        dialog_title.setText("Confirmation !");
        TextView dialog_common_header = (TextView) dialog.findViewById(R.id.dialog_common_header);
        dialog_common_header.setText("Do you want to exit,without update?");

        Button dialog_confirmation_cancel = (Button) dialog.findViewById(R.id.dialog_confirmation_cancel);
        dialog_confirmation_cancel.setText("No");
        dialog_confirmation_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        final Button dialog_confirmation_ok = (Button) dialog.findViewById(R.id.dialog_confirmation_ok);
        dialog_confirmation_ok.setText("Yes");
        dialog_confirmation_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantUtil.fag_index=0;
                new ActivityUtil(getActivity()).startTeacherActivity();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void FrmDateDialog(){

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                DecimalFormat mFormat= new DecimalFormat("00");
                mFormat.setRoundingMode(RoundingMode.DOWN);
                selectedmonth = selectedmonth + 1;
                String select_date =  selectedyear + "-" +  mFormat.format(Double.valueOf(selectedmonth)) + "-" +  mFormat.format(Double.valueOf(selectedday));
                //attendance_date.setText(select_date);
                attendance_date.setText(CommonMethod.dateConversion(select_date));
                selected_attendance_date=select_date;

                if(time_slot_name_list.size()>0){
                    spinner_time_slot.setSelection(0);
                }
                selected_time_slot_id="";
                //paper_name_list.clear();
                //section_name_list.clear();
                studentList.clear();

            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        //  mDatePicker.getDatePicker().setMaxDate(max_date);
        mDatePicker.show();
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
                                time_slot_name_list.clear();
                                time_slot_id_list.clear();
                                time_slot_name_list.add("-select-");
                                time_slot_id_list.add("-1");
                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    String id = obj.getString("id");
                                    String time_slot = obj.getString("time_slot");
                                    String status2 = obj.getString("status");

                                    time_slot_id_list.add(id);
                                    time_slot_name_list.add(time_slot);

                                }

                                time_slotAdapter.notifyDataSetChanged();
                                progress.dismiss();
                               // getSubjectList();

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

    public void getStudentList(final String attendence_date,final String time_id){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_ATTENDANCE_LIST_URL,
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

                                studentList.clear();
                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    Student student=new Student();
                                    String id = obj.getString("id");
                                    String student_id=obj.getString("student_id");
                                    String name = obj.getString("name");
                                    String roll_no=obj.getString("roll_no");
                                    selected_attendance_id=obj.getString("attendence_id");
                                    student.setName(name);
                                    //student.setId(id);
                                    student.setId(student_id);
                                    String studentStatus=obj.getString("status");
                                    if(studentStatus.equalsIgnoreCase("1")){
                                        student.setStatus(true);
                                    }else {
                                        student.setStatus(false);
                                    }
                                    student.setRoll(roll_no);
                                    studentList.add(student);

                                }

                                //attendanceAdapter = new TeacherTakeAttendanceAdapter(getActivity(),studentList);
                                //lv_student.setAdapter(attendanceAdapter);
                                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);

                                attendanceAdapter.refreshList(studentList);
                                progress.dismiss();
                               // getSubjectList();

                            }else{
                                studentList.clear();

                                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                                attendanceAdapter.refreshList(studentList);
                               /* attendanceAdapter = new TeacherTakeAttendanceAdapter(getActivity(),studentList);
                                lv_student.setAdapter(attendanceAdapter);
                                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);*/

                                progress.dismiss();
                                Toast.makeText(getActivity(),"no student found", Toast.LENGTH_SHORT).show();
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
                params.put("employee_id",session.getKeyEmployeeId());
                params.put("attendence_date",attendence_date);
                params.put("time_id",time_id);

                Log.e("employee_id",session.getKeyEmployeeId());
                Log.e("attendence_date",attendence_date);
                Log.e("time_id",time_id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void submitAttendance(final String selected_attendance_id, final String rolls){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_UPDATED_URL,
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

                                progress.dismiss();
                                Toast.makeText(getActivity(),"successfully updated", Toast.LENGTH_SHORT).show();
                                ConstantUtil.fag_index=0;
                                new ActivityUtil(getActivity()).startTeacherActivity();

                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"failed to update", Toast.LENGTH_SHORT).show();
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
                //params.put("employee_id",session.getKeyEmployeeId());
                params.put("attendence_id",selected_attendance_id);
                params.put("student_ids",rolls);


                //Log.e("employee_id",session.getKeyEmployeeId());
                Log.e("attendence_id",selected_attendance_id);
                Log.e("student_ids",rolls);

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
