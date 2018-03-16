package com.lnsel.seramporecollege.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentPaymentDueAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public StudentPaymentDueAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        final MyViewHolder holder;
        if (itemView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.student_payment_due_list_item, null, false);
            holder = new MyViewHolder();

            holder.subject=(TextView) itemView.findViewById(R.id.subject);
            holder.student_payment_status=(TextView) itemView.findViewById(R.id.student_payment_status);
            holder.received_in=(TextView) itemView.findViewById(R.id.received_in);
            holder.receipt_generation_date=(TextView) itemView.findViewById(R.id.receipt_generation_date);

            holder.student_payment_date=(TextView) itemView.findViewById(R.id.student_payment_date);
            holder.year_no=(TextView) itemView.findViewById(R.id.year_no);
            holder.student_id=(TextView) itemView.findViewById(R.id.student_id);
            holder.total_amount=(TextView) itemView.findViewById(R.id.total_amount);
            holder.name=(TextView)itemView.findViewById(R.id.name);

            holder.btn_view=(Button)itemView.findViewById(R.id.btn_view);

            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }


        resultp = data.get(position);

        holder.subject.setText("Due Amount : "+resultp.get("amount"));
        holder.student_payment_status.setText("Due");
        //holder.received_in.setText(resultp.get("received_in"));//cash/bank

        //holder.receipt_generation_date.setText(resultp.get("receipt_generation_date"));
        holder.student_payment_date.setText(resultp.get("fee_heads"));
        holder.year_no.setText("Year : "+resultp.get("year_no"));
       // holder.student_id.setText("Student Id : "+resultp.get("student_id"));
        holder.total_amount.setText("Total Amount(Rs) : "+resultp.get("amount"));

        //holder.name.setText("");

        /*holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPayslip(
                        data.get(position).get("sal_id"),
                        data.get(position).get("sal_y"),
                        CommonMethod.getMonth(Integer.valueOf(data.get(position).get("sal_m"))));
            }
        });*/

        return itemView;
    }
    private static class MyViewHolder {
        public TextView student_payment_status,received_in,receipt_generation_date,
                student_payment_date,year_no,student_id,name,total_amount,subject;
        public Button btn_view;
    }


    public void createReceipt(final String sal_id,final String year,final String month){
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("loading...");
        //progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                UrlUtil.TEACHER_DOWNLOAD_PAYSLIP_URL,
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

                                String record=jsonObj.getString("record");
                                ConstantUtil.payslipUrl=record;
                                ConstantUtil.payslipYear=year;
                                ConstantUtil.payslipMonth=month;


                                progress.dismiss();

                                //new ActivityUtil(context).startPayslipViewActivity();
                                new ActivityUtil(context).startPdfViewActivity();

                            }else{
                                progress.dismiss();
                                Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context,"Server not Responding, Please check your Internet Connection", Toast.LENGTH_LONG).show();
                //view.errorInfo("Server not Responding, Please check your Internet Connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //sal_id:1535
                params.put("sal_id",sal_id);
                System.out.println("sal_id : "+sal_id);
                Log.e("sal_id",sal_id);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void refresh(ArrayList<HashMap<String,String>> list){
        data = list;
        notifyDataSetChanged();
    }


}