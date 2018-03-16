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
import com.lnsel.seramporecollege.utils.CommonMethod;
import com.lnsel.seramporecollege.utils.ConstantUtil;
import com.lnsel.seramporecollege.utils.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherLeaveCBAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public TeacherLeaveCBAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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
            itemView = inflater.inflate(R.layout.teacher_leave_cb_list_item, null, false);
            holder = new MyViewHolder();
            holder.subject=(TextView) itemView.findViewById(R.id.subject);
            holder.leave_title=(TextView) itemView.findViewById(R.id.leave_title);
            holder.leave_amount=(TextView) itemView.findViewById(R.id.leave_amount);


            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        //holder.subject.setText(resultp.get("sal_y"));

        holder.leave_title.setText(resultp.get("leave_title")+" : ");
        holder.leave_amount.setText(resultp.get("leave_amount"));


        return itemView;
    }
    private static class MyViewHolder {
        public TextView subject,leave_title,leave_amount;
    }




    public void refresh(ArrayList<HashMap<String,String>> list){
        data = list;
        notifyDataSetChanged();
    }


}