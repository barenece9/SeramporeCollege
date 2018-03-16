package com.lnsel.seramporecollege.fragment.teacher;

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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeacherTakeAttandanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherTakeAttandanceFragment extends Fragment {
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
    Spinner spinner_subject,spinner_paper,spinner_institute,spinner_section,spinner_time_slot;
    RadioButton radio_3rdyear,radio_2ndyear,radio_1styear;
    RadioGroup radio_group_year,radio_group_hg;
    RadioButton radio_h,radio_g;
    TextView tv_selection;
    ListView lv_student;
    Button btn_select_all,btn_deselect_all,btn_student_list;

   // Select subject -> paper -> institute->  h/g->  -> year(1,2,3) ->  section->

    TeacherTakeAttendanceAdapter attendanceAdapter;
    ArrayList<Student> studentList=new ArrayList<>();

    ArrayList<String> subject_name_list=new ArrayList<>();
    ArrayList<String> subject_id_list=new ArrayList<>();
    ArrayList<String> subject_hg_list=new ArrayList<>();
    String selected_subject_name="";
    String selected_subject_id="";
    String selected_subject_hg="";

    ArrayList<String> paper_name_list=new ArrayList<>();
    ArrayList<String> paper_id_list=new ArrayList<>();
    String selected_paper_name="";
    String selected_paper_id="";

    ArrayList<String> institute_name_list=new ArrayList<>();
    ArrayList<String> institute_id_list=new ArrayList<>();
    String selected_institute_name="";
    String selected_institute_id="";


    ArrayList<String> time_slot_name_list=new ArrayList<>();
    ArrayList<String> time_slot_id_list=new ArrayList<>();
    String selected_time_slot_name="";
    String selected_time_slot_id="";

    ArrayList<String> section_name_list=new ArrayList<>();
    ArrayList<String> section_id_list=new ArrayList<>();
    String selected_section_name="";
    String selected_section_id="";

    ArrayAdapter<String> subjectAdapter;
    ArrayAdapter<String> paperAdapter;

    ArrayAdapter<String> instituteAdapter;
    ArrayAdapter<String> sectionAdapter;
    ArrayAdapter<String> time_slotAdapter;

    String selected_year="1";//1,2,3
    public TeacherTakeAttandanceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TeacherTakeAttandanceFragment newInstance(String param1, String param2) {
        TeacherTakeAttandanceFragment fragment = new TeacherTakeAttandanceFragment();
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
            rootView = inflater.inflate(R.layout.fragment_teacher_attendance, container, false);
        } catch (InflateException e) {

        }
        session=new SharedManagerUtil(getActivity());

        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        progress.setCanceledOnTouchOutside(false);

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
        attendanceAdapter = new TeacherTakeAttendanceAdapter(getActivity(),studentList);
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


                if(selected_year.equalsIgnoreCase("")){
                    DialogUtil.customDialog(getActivity(),"Year can't be blank");
                } else if (selected_institute_id.equalsIgnoreCase("") || selected_institute_id.equalsIgnoreCase("-1")) {
                    DialogUtil.customDialog(getActivity(),"Institute can't be blank");

                } else if (selected_time_slot_id.equalsIgnoreCase("") || selected_time_slot_id.equalsIgnoreCase("-1")) {
                    DialogUtil.customDialog(getActivity(),"time slot can't be blank");

                }else if (selected_subject_id.equalsIgnoreCase("") || selected_subject_id.equalsIgnoreCase("-1")) {

                    DialogUtil.customDialog(getActivity(),"Subject can't be blank");
                }else if (selected_subject_hg.equalsIgnoreCase("") || selected_subject_hg.equalsIgnoreCase("NA")) {

                    DialogUtil.customDialog(getActivity(),"Subject can't be blank");
                }else if (selected_paper_id.equalsIgnoreCase("") || selected_paper_id.equalsIgnoreCase("-1")) {

                    DialogUtil.customDialog(getActivity(),"Paper can't be blank");
                }else if (selected_section_id.equalsIgnoreCase("") || selected_section_id.equalsIgnoreCase("-1")) {

                    DialogUtil.customDialog(getActivity(),"Section can't be blank");
                }else if (!InternetConnectivity.isConnectedFast(getActivity())) {

                    DialogUtil.customDialog(getActivity(),"Check your internal connection");
                }else if (studentList.size()==0) {
                    DialogUtil.customDialog(getActivity(),"Student list blank");
                }else {
                    SubmitDialog(rolls);
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



        radio_1styear=(RadioButton)rootView.findViewById(R.id.radio_1styear);
        radio_1styear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selected_year="1";
                }
                if(subject_name_list.size()>0) {
                    spinner_subject.setSelection(0);
                }
                if(paper_name_list.size()>0) {
                    spinner_paper.setSelection(0);
                }
                if(section_name_list.size()>0){
                    spinner_section.setSelection(0);
                }
                selected_subject_id="";
                selected_paper_id="";
                selected_section_id="";
                //paper_name_list.clear();
                //section_name_list.clear();
                studentList.clear();
                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                attendanceAdapter.refreshList(studentList);

            }
        });

        radio_2ndyear=(RadioButton)rootView.findViewById(R.id.radio_2ndyear);
        radio_2ndyear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selected_year="2";
                }
                if(subject_name_list.size()>0) {
                    spinner_subject.setSelection(0);
                }
                if(paper_name_list.size()>0) {
                    spinner_paper.setSelection(0);
                }
                if(section_name_list.size()>0){
                    spinner_section.setSelection(0);
                }
                selected_subject_id="";
                selected_paper_id="";
                selected_section_id="";
                //paper_name_list.clear();
                //section_name_list.clear();
                studentList.clear();
                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                attendanceAdapter.refreshList(studentList);
            }
        });

        radio_3rdyear=(RadioButton)rootView.findViewById(R.id.radio_3rdyear);
        radio_3rdyear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selected_year="3";
                }
                if(subject_name_list.size()>0) {
                    spinner_subject.setSelection(0);
                }
                if(paper_name_list.size()>0) {
                    spinner_paper.setSelection(0);
                }
                if(section_name_list.size()>0){
                    spinner_section.setSelection(0);
                }
                selected_subject_id="";
                selected_paper_id="";
                selected_section_id="";
                //paper_name_list.clear();
                //section_name_list.clear();
                studentList.clear();
                CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                attendanceAdapter.refreshList(studentList);
            }
        });


        //subject selection=====================================================
        spinner_subject=(Spinner)rootView.findViewById(R.id.spinner_subject);
        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(subject_name_list.size()>0) {

                    //clear previous values
                    if(paper_name_list.size()>0) {
                        spinner_paper.setSelection(0);
                    }
                    if(section_name_list.size()>0){
                        spinner_section.setSelection(0);
                    }
                    selected_paper_id="";
                    selected_section_id="";

                    //paper_name_list.clear();
                    //section_name_list.clear();
                    studentList.clear();

                    CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                    attendanceAdapter.refreshList(studentList);


                    selected_subject_id=subject_id_list.get(position);
                    selected_subject_name=subject_name_list.get(position);
                    selected_subject_hg=subject_hg_list.get(position);
                    if(!selected_subject_name.equalsIgnoreCase("-select-")){
                        getPaperList(selected_subject_id);
                    }




                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(),"no subject select",Toast.LENGTH_SHORT).show();

            }
        });
        subjectAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, subject_name_list);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subject.setAdapter(subjectAdapter);


        //paper selection=====================================================
        spinner_paper=(Spinner)rootView.findViewById(R.id.spinner_paper);
        spinner_paper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(paper_name_list.size()>0) {

                    //clear previous values
                    if(section_name_list.size()>0){
                        spinner_section.setSelection(0);
                    }
                    selected_section_id="";

                    //section_name_list.clear();
                    studentList.clear();

                    CommonMethod.setListViewHeightBasedOnChildren(getActivity(),lv_student);
                    attendanceAdapter.refreshList(studentList);

                    selected_paper_id=paper_id_list.get(position);
                    selected_paper_name=paper_name_list.get(position);
                    if(!selected_paper_name.equalsIgnoreCase("-select-")){

                        getSectionList();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        paperAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, paper_name_list);
        paperAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_paper.setAdapter(paperAdapter);


        //institute selection=====================================================
        spinner_institute=(Spinner)rootView.findViewById(R.id.spinner_institute);
        spinner_institute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(subject_name_list.size()>0) {
                    spinner_subject.setSelection(0);
                }
                if(paper_name_list.size()>0) {
                    spinner_paper.setSelection(0);
                }
                if(section_name_list.size()>0){
                    spinner_section.setSelection(0);
                }
                selected_subject_id="";
                selected_paper_id="";
                selected_section_id="";
                //paper_name_list.clear();
                //section_name_list.clear();
                studentList.clear();

                if(institute_name_list.size()>0) {
                    selected_institute_id=institute_id_list.get(position);
                    selected_institute_name=institute_name_list.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        instituteAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, institute_name_list);
        instituteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_institute.setEnabled(false);//false
        spinner_institute.setClickable(false);//false
        spinner_institute.setAdapter(instituteAdapter);



        //time_slot selection=====================================================
        spinner_time_slot=(Spinner)rootView.findViewById(R.id.spinner_time_slot);
        spinner_time_slot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                /*if(subject_name_list.size()>0) {
                    spinner_subject.setSelection(0);
                }
                if(paper_name_list.size()>0) {
                    spinner_paper.setSelection(0);
                }
                if(section_name_list.size()>0){
                    spinner_section.setSelection(0);
                }
                selected_subject_id="";
                selected_paper_id="";
                selected_section_id="";
                //paper_name_list.clear();
                //section_name_list.clear();
                studentList.clear();*/

                if(time_slot_name_list.size()>0) {
                    selected_time_slot_id=time_slot_id_list.get(position);
                    selected_time_slot_name=time_slot_name_list.get(position);
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

        //section selection=====================================================
        spinner_section=(Spinner)rootView.findViewById(R.id.spinner_section);
        spinner_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(section_name_list.size()>0) {
                    selected_section_id=section_id_list.get(position);
                    selected_section_name=section_name_list.get(position);
                    if(!selected_section_name.equalsIgnoreCase("-select-")){
                        getStudentList();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });
        sectionAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, section_name_list);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_section.setAdapter(sectionAdapter);

        getInstituteList();


        return rootView;
    }


    public void SubmitDialog(final String studentIds){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_title=(TextView)dialog.findViewById(R.id.dialog_title);
        dialog_title.setText("Confirmation !");
        TextView dialog_common_header = (TextView) dialog.findViewById(R.id.dialog_common_header);
        dialog_common_header.setText("Do you want to submit?");

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

                submitAttendance(studentIds);
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
        dialog_common_header.setText("Do you want to exit,without submit?");

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


    public void getInstituteList(){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_INSTITUTE_LIST_URL,
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
                                institute_name_list.clear();
                                institute_id_list.clear();
                                institute_name_list.add("-select-");
                                institute_id_list.add("-1");
                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    String id = obj.getString("id");
                                    String name = obj.getString("name");
                                    String code = obj.getString("code");
                                    String honours_general = obj.getString("honours_general");
                                    String status2 = obj.getString("status");

                                    institute_id_list.add(id);
                                    institute_name_list.add(name);

                                }

                                instituteAdapter.notifyDataSetChanged();
                                progress.dismiss();
                                for(int j=0;j<institute_id_list.size();j++){
                                    if(institute_id_list.get(j).equalsIgnoreCase(session.getKeyUserInstituteId())){
                                        spinner_institute.setSelection(j);
                                        selected_institute_id=institute_id_list.get(j);
                                        selected_institute_name=institute_name_list.get(j);
                                        break;
                                    }
                                }

                                getTimeSlotList();


                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"Institute list blank", Toast.LENGTH_SHORT).show();
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
                                getSubjectList();

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



    public void getSubjectList(){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_SUBJECT_LIST_URL,
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
                                subject_name_list.clear();
                                subject_id_list.clear();
                                subject_hg_list.clear();
                                subject_name_list.add("-select-");
                                subject_id_list.add("-1");
                                subject_hg_list.add("NA");
                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    String id = obj.getString("id");
                                    String batch_id = obj.getString("batch_id");
                                    String academic_department_id = obj.getString("academic_department_id");
                                    String name = obj.getString("name");
                                    String code = obj.getString("code");
                                    String honours_general = obj.getString("honours_general");
                                    String status2 = obj.getString("status");

                                    String subjectName=name+"-"+code+" ("+honours_general+")";
                                    subject_id_list.add(id);
                                    subject_name_list.add(subjectName);
                                    subject_hg_list.add(honours_general);

                                }

                                subjectAdapter.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged();
                                progress.dismiss();
                                //getTimeSlotList();


                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"Subject list blank", Toast.LENGTH_SHORT).show();
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



    public void getPaperList(final String selected_subject_id){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_PAPER_LIST_URL,
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
                                paper_name_list.clear();
                                paper_id_list.clear();
                                paper_name_list.add("-select-");
                                paper_id_list.add("-1");
                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    String id = obj.getString("id");
                                    String batch_id = obj.getString("batch_id");
                                    String subject_id = obj.getString("subject_id");
                                    String name = obj.getString("name");
                                    String code = obj.getString("code");
                                    String paper_type = obj.getString("paper_type");
                                    String status2 = obj.getString("status");

                                    String subjectName=code+" ("+paper_type+")";
                                    paper_id_list.add(id);
                                    paper_name_list.add(subjectName);

                                }

                                paperAdapter.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged();
                                progress.dismiss();
                                //getSectionList();

                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"Paper list blank", Toast.LENGTH_SHORT).show();
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
                params.put("subject_id",selected_subject_id);
                params.put("year",selected_year);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void getSectionList(){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_SECTION_LIST_URL,
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
                                section_name_list.clear();
                                section_id_list.clear();
                                section_name_list.add("-select-");
                                section_id_list.add("-1");
                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=0;i<record.length();i++){
                                    JSONObject obj=record.getJSONObject(i);

                                    String id = obj.getString("id");

                                    String section_name = obj.getString("section_name");

                                    String batch_id = obj.getString("batch_id");
                                    String subject_id = obj.getString("subject_id");
                                    String paper_type = obj.getString("paper_type");


                                    //String subjectName=code+" ("+paper_type+")";
                                    section_id_list.add(id);
                                    section_name_list.add(section_name);

                                }

                                sectionAdapter.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged();
                                progress.dismiss();


                            }else{
                                section_name_list.clear();
                                section_id_list.clear();
                                sectionAdapter.notifyDataSetChanged();

                                progress.dismiss();
                                Toast.makeText(getActivity(),"Section list blank", Toast.LENGTH_SHORT).show();
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
               // params.put("academic_year_id","8");server side
                params.put("academic_institute_id",selected_institute_id);
                params.put("academic_department_id",session.getKeyUserAcademicDepartmentId());
               // params.put("batch_id","8");
                params.put("year",selected_year);
                params.put("subject_id",selected_subject_id);
                params.put("honours_general",selected_subject_hg);
                params.put("paper_id",selected_paper_id);

                Log.e("academic_year_id","8");
                Log.e("academic_institute_id",selected_institute_id);
                Log.e("academic_department_id",session.getKeyUserAcademicDepartmentId());
                // params.put("batch_id","8");
                Log.e("year",selected_year);
                Log.e("subject_id",selected_subject_id);
                Log.e("honours_general",selected_subject_hg);
                Log.e("paper_id",selected_paper_id);

                System.out.println("academic_year_id : 8");
                System.out.println("academic_institute_id"+selected_institute_id);
                System.out.println("academic_department_id"+session.getKeyUserAcademicDepartmentId());
                // params.put("batch_id","8");
                System.out.println("year"+selected_year);
                System.out.println("subject_id"+selected_subject_id);
                System.out.println("honours_general"+selected_subject_hg);
                System.out.println("paper_id"+selected_paper_id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void getStudentList(){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_STUDENT_LIST_URL,
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
                                    String name = obj.getString("name");
                                    String roll_no=obj.getString("roll_no");
                                    student.setName(name);
                                    student.setId(id);
                                    student.setStatus(false);
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
                params.put("academic_section_id",selected_section_id);
                //params.put("academic_year_id","8");server side
                params.put("subject_id",selected_subject_id);
                params.put("paper_id",selected_paper_id);
                params.put("year",selected_year);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    public void submitAttendance(final String student_ids){

        progress.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.ATTENDANCE_SUBMIT_URL,
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
                                Toast.makeText(getActivity(),"Successfully submitted", Toast.LENGTH_LONG).show();
                            }else if(status.equals("exists")){
                                progress.dismiss();
                                Toast.makeText(getActivity(),"Already submitted for this time slot", Toast.LENGTH_SHORT).show();
                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"Failed to submit", Toast.LENGTH_SHORT).show();
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
                params.put("academic_section_id",selected_section_id);
                params.put("academic_department_id",session.getKeyUserAcademicDepartmentId());
                params.put("year",selected_year);
                params.put("subject_id",selected_subject_id);
                params.put("paper_id",selected_paper_id);
                params.put("employee_id",session.getKeyEmployeeId());
                params.put("student_ids",student_ids);
                params.put("time_id",selected_time_slot_id);
                params.put("approve_status","1");

                Log.e("academic_section_id",selected_section_id);
                Log.e("academic_department_id",session.getKeyUserAcademicDepartmentId());
                Log.e("year",selected_year);
                Log.e("subject_id",selected_subject_id);
                Log.e("paper_id",selected_paper_id);
                Log.e("employee_id",session.getKeyEmployeeId());
                Log.e("student_ids",student_ids);
                Log.e("time_id",selected_time_slot_id);
                Log.e("approve_status","1");

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
