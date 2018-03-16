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
import com.lnsel.seramporecollege.adapter.TeacherPaySlipAdapter;
import com.lnsel.seramporecollege.utils.ActivityUtil;
import com.lnsel.seramporecollege.utils.CommonMethod;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.SharedManagerUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeacherPaySlipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherPaySlipFragment extends Fragment {
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
    String TAG="TeacherNoticeBordFragment";
    ListView lv_notice;
    TeacherPaySlipAdapter adapter;


    Spinner year_spinner,month_spinner;
    ArrayAdapter<String> yearAdapter;
    ArrayList<String> year_List=new ArrayList<String>();

    ArrayAdapter<String> monthAdapter;
    ArrayList<String> month_List=new ArrayList<String>();
    String selectedYear="2018";
    ArrayList<HashMap<String,String>> noticeList=new ArrayList<>();

    public TeacherPaySlipFragment() {
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
    public static TeacherPaySlipFragment newInstance(String param1, String param2) {
        TeacherPaySlipFragment fragment = new TeacherPaySlipFragment();
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
            rootView = inflater.inflate(R.layout.fragment_teacher_payslip, container, false);
        } catch (InflateException e) {

        }

        session=new SharedManagerUtil(getActivity());
        lv_notice=(ListView)rootView.findViewById(R.id.list_notice);
        adapter = new TeacherPaySlipAdapter(getActivity(), noticeList);
        lv_notice.setAdapter(adapter);
        /*lv_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                createPayslip(noticeList.get(i).get("sal_id"));

            }
        });*/

        year_spinner=(Spinner)rootView.findViewById(R.id.year_spinner);
        year_List.clear();
        year_List.add("2017");
        year_List.add("2018");
        year_List.add("2019");


        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(year_List.size()>0) {
                    filterAdapterByYear(year_List.get(position),noticeList);
                    // adapter.filter(day_List.get(position));
                    selectedYear=year_List.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        yearAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, year_List);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(yearAdapter);

        month_spinner=(Spinner)rootView.findViewById(R.id.month_spinner);
        month_List.clear();
        month_List.add("-select-");
        month_List.add("January");
        month_List.add("February");
        month_List.add("March");
        month_List.add("April");

        month_List.add("May");
        month_List.add("June");
        month_List.add("July");
        month_List.add("August");

        month_List.add("September");
        month_List.add("October");
        month_List.add("November");
        month_List.add("December");

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(month_List.size()>0) {
                     filterAdapterByMonth(month_List.get(position),selectedYear,noticeList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
                // TODO Auto-generated method stub

            }
        });

        monthAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_rows, month_List);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(monthAdapter);
        getCurrentYear();
        doPayslipList();

        return rootView;
    }


    public void filterAdapterByYear(String key,ArrayList<HashMap<String,String>> timeList){
        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<timeList.size();i++){
            if(timeList.get(i).get("sal_y").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                newList.add(timeList.get(i));
            }
        }
        month_spinner.setSelection(0);
        adapter.refresh(newList);
        // adapter.filterByDate(key);
    }

    public void filterAdapterByMonth(String key,String selectedDate,ArrayList<HashMap<String,String>> timeList){

        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<timeList.size();i++){
            if(timeList.get(i).get("sal_y").toLowerCase(Locale.getDefault()).contains(selectedDate.toLowerCase(Locale.getDefault()))){
                newList.add(timeList.get(i));
            }
        }

        ArrayList<HashMap<String,String>> newerList=new ArrayList<>();
        if(key.equalsIgnoreCase("-select-")){
            newerList.addAll(newList);
        }else {
            for(int i=0;i<newList.size();i++){
                //holder.month.setText(CommonMethod.getMonth(Integer.valueOf(resultp.get("sal_m"))));
                if(CommonMethod.getMonth(Integer.valueOf(newList.get(i).get("sal_m"))).toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                    newerList.add(timeList.get(i));
                }
                /*if(newList.get(i).get("sal_m").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                    newerList.add(timeList.get(i));
                }*/
            }
        }
        adapter.refresh(newerList);
        //adapter.filterByTime(key);
    }




    public void doPayslipList(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.TEACHER_PAYSLIP_LIST_URL,
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

                                noticeList.clear();

                                JSONArray record=jsonObj.getJSONArray("record");
                                for(int i=record.length()-1;i>=0;i--){
                                    JSONObject obj=record.getJSONObject(i);

                                    HashMap<String,String> notice=new HashMap<>();

                                    String sal_id = obj.getString("sal_id");
                                    String sal_m = obj.getString("sal_m");
                                    String sal_y = obj.getString("sal_y");
                                    String fin_year_identifier = obj.getString("fin_year_identifier");

                                    String month_days = obj.getString("month_days");
                                    String working_days = obj.getString("working_days");
                                    String paid_days = obj.getString("paid_days");
                                    String net_salary = obj.getString("net_salary");

                                    String paid_leaves = obj.getString("paid_leaves");

                                    notice.put("sal_id",sal_id);
                                    notice.put("sal_m",sal_m);
                                    notice.put("sal_y",sal_y);
                                    notice.put("fin_year_identifier",fin_year_identifier);

                                    notice.put("month_days",month_days);
                                    notice.put("working_days",working_days);
                                    notice.put("paid_days",paid_days);

                                    notice.put("net_salary",net_salary);
                                    notice.put("paid_leaves",paid_leaves);

                                    noticeList.add(notice);
                                }

                                adapter.refresh(noticeList);
                                filterAdapterByYear(selectedYear,noticeList);
                                /*adapter = new TeacherPaySlipAdapter(getActivity(), noticeList);
                                lv_notice.setAdapter(adapter);*/

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
                params.put("employee_id",session.getKeyEmployeeId());

                System.out.println("employee_id : "+session.getKeyEmployeeId());
                Log.d("employee_id",session.getKeyEmployeeId());
                //params.put("sal_m","4");
               // params.put("sal_y","2017");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public String getCurrentYear(){
        String currentYear="";
        try {
            /*SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            currentYear = sdf.format(d);*/
            int year = Calendar.getInstance().get(Calendar.YEAR);
            currentYear=String.valueOf(year);
            selectedYear=currentYear;
        }catch (Exception e){
        }
        for(int i=0;i<year_List.size();i++){
            if(year_List.get(i).toLowerCase(Locale.getDefault()).contains(currentYear.toLowerCase(Locale.getDefault()))){
                year_spinner.setSelection(i);
            }
        }
        System.out.print("currentYear====> "+currentYear);
        return currentYear;
    }

    public void createPayslip(final String sal_id){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.TEACHER_DOWNLOAD_PAYSLIP_URL,
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

                                String record=jsonObj.getString("record");
                                ConstantUtil.payslipUrl=record;
                                progress.dismiss();

                                new ActivityUtil(getActivity()).startPayslipViewActivity();


                            }else{
                                progress.dismiss();
                                Toast.makeText(getActivity(),"Failed", Toast.LENGTH_SHORT).show();
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
                //sal_id:1535
                params.put("sal_id",sal_id);
                System.out.println("sal_id : "+sal_id);
                Log.d("sal_id",sal_id);
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
