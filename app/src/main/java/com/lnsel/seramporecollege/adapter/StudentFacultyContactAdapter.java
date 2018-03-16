package com.lnsel.seramporecollege.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class StudentFacultyContactAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String,String>> arraylist;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public StudentFacultyContactAdapter(Context context,
                                        ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;


        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(arraylist);
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
            itemView = inflater.inflate(R.layout.faculty_contact_list_item, null, false);
            holder = new MyViewHolder();
            holder.tv_name=(TextView) itemView.findViewById(R.id.tv_name);
            holder.tv_emp_type=(TextView) itemView.findViewById(R.id.tv_emp_type);
            holder.tv_dept=(TextView) itemView.findViewById(R.id.tv_dept);
            holder.tv_contact=(TextView) itemView.findViewById(R.id.tv_contact);
            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);

        holder.tv_name.setText(resultp.get("name"));
        holder.tv_emp_type.setText(resultp.get("category"));
        holder.tv_dept.setText(resultp.get("academic_department"));
        holder.tv_contact.setText(resultp.get("mobile"));

        return itemView;
    }
    private static class MyViewHolder {
        public TextView tv_contact,tv_emp_type,tv_dept,tv_name;
    }

    public void refresh(ArrayList<HashMap<String,String>> list){
        data = list;
        this.arraylist.addAll(data);
        notifyDataSetChanged();
    }

    // Filter Class
    public void filter(String charText, View btn_clear) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0||charText.equalsIgnoreCase("")) {
            data.addAll(arraylist);
            btn_clear.setVisibility(View.GONE);
        }
        else
        {
            for (HashMap<String,String> wp : arraylist)
            {
                if (wp.get("name").toLowerCase(Locale.getDefault()).contains(charText)||
                        wp.get("academic_department").toLowerCase(Locale.getDefault()).contains(charText))
                {
                    data.add(wp);
                }
            }
            btn_clear.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }



}