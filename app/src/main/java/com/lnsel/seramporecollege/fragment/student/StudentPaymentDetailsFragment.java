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
import com.lnsel.seramporecollege.adapter.StudentPaymentDetailsAdapter;
import com.lnsel.seramporecollege.utils.CommonMethod;
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
 * Use the {@link StudentPaymentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentPaymentDetailsFragment extends Fragment {
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
    StudentPaymentDetailsAdapter adapter;
    ArrayList<HashMap<String,String>> paymentList=new ArrayList<>();

    Spinner year_spinner,month_spinner;
    ArrayAdapter<String> yearAdapter;
    ArrayList<String> year_List=new ArrayList<String>();

    ArrayAdapter<String> monthAdapter;
    ArrayList<String> month_List=new ArrayList<String>();
    String selectedYear="2018";

    public StudentPaymentDetailsFragment() {
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
    public static StudentPaymentDetailsFragment newInstance(String param1, String param2) {
        StudentPaymentDetailsFragment fragment = new StudentPaymentDetailsFragment();
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
            rootView = inflater.inflate(R.layout.fragment_student_payment_details, container, false);
        } catch (InflateException e) {

        }

        session=new SharedManagerUtil(getActivity());
        lv_notice=(ListView)rootView.findViewById(R.id.list_notice);
        adapter = new StudentPaymentDetailsAdapter(getActivity(), paymentList);
        lv_notice.setAdapter(adapter);

        year_spinner=(Spinner)rootView.findViewById(R.id.year_spinner);
        year_List.clear();
        year_List.add("2015");
        year_List.add("2016");
        year_List.add("2017");
        year_List.add("2018");
        year_List.add("2019");


        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if(year_List.size()>0) {
                    filterAdapterByYear(year_List.get(position),paymentList);
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
                    filterAdapterByMonth(month_List.get(position),selectedYear,paymentList);
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
        doPaymentList();

        return rootView;
    }

    public void filterAdapterByYear(String key,ArrayList<HashMap<String,String>> timeList){
        ArrayList<HashMap<String,String>> newList=new ArrayList<>();
        for(int i=0;i<timeList.size();i++){
            if(timeList.get(i).get("student_payment_date").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
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
            if(timeList.get(i).get("student_payment_date").toLowerCase(Locale.getDefault()).contains(selectedDate.toLowerCase(Locale.getDefault()))){
                newList.add(timeList.get(i));
            }
        }

        ArrayList<HashMap<String,String>> newerList=new ArrayList<>();
        if(key.equalsIgnoreCase("-select-")){
            newerList.addAll(newList);
        }else {
            for(int i=0;i<newList.size();i++){
                try {
                    if(CommonMethod.getMonthName(newList.get(i).get("student_payment_date")).toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                        newerList.add(timeList.get(i));
                    }
                }catch (Exception e){

                }
                /*if(newList.get(i).get("date_start").toLowerCase(Locale.getDefault()).contains(key.toLowerCase(Locale.getDefault()))){
                    newerList.add(timeList.get(i));
                }*/
            }
        }
        adapter.refresh(newerList);
        //adapter.filterByTime(key);
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



    public void doPaymentList(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.STUDENT_PAYMENT_LIST_URL,
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

                                paymentList.clear();

                                JSONArray record=jsonObj.getJSONArray("record");
                                //10(0-9)
                                for(int i=record.length()-1;i>=0;i--){
                                    JSONObject obj=record.getJSONObject(i);

                                    HashMap<String,String> notice=new HashMap<>();

                                    String id = obj.getString("id");
                                    String fee_structure_id = obj.getString("fee_structure_id");
                                    String year_no = obj.getString("year_no");
                                    String total_amount = obj.getString("total_amount");
                                    String fine_amount = obj.getString("fine_amount");
                                    String receipt_generation_date = obj.getString("receipt_generation_date");
                                    String remarks = obj.getString("remarks");
                                    String bank_payment_status = obj.getString("bank_payment_status");
                                    String bank_payment_date = obj.getString("bank_payment_date");


                                    String student_payment_status = obj.getString("student_payment_status");
                                    String student_payment_date = obj.getString("student_payment_date");
                                    String status2 = obj.getString("status");
                                    String payment_date = obj.getString("payment_date");
                                    String trans_id = obj.getString("trans_id");
                                    String received_in = obj.getString("received_in");
                                    String student_id=obj.getString("student_id");

                                    notice.put("id",id);
                                    notice.put("fee_structure_id",fee_structure_id);
                                    notice.put("year_no",year_no);
                                    notice.put("total_amount",total_amount);
                                    notice.put("fine_amount",fine_amount);
                                    notice.put("receipt_generation_date",receipt_generation_date);
                                    notice.put("remarks",remarks);
                                    notice.put("bank_payment_status",bank_payment_status);
                                    notice.put("bank_payment_date",bank_payment_date);


                                    notice.put("student_payment_status",student_payment_status);
                                    notice.put("student_payment_date",student_payment_date);
                                    notice.put("status2",status2);
                                    notice.put("payment_date",payment_date);
                                    notice.put("trans_id",trans_id);
                                    notice.put("received_in",received_in);
                                    notice.put("student_id",student_id);

                                    paymentList.add(notice);
                                }

                                adapter.refresh(paymentList);

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
                params.put("student_id",session.getKeyStudentId());
                Log.d("student_id",session.getKeyStudentId());
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
