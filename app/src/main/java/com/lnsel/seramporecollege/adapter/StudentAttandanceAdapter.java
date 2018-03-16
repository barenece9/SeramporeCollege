package com.lnsel.seramporecollege.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnsel.seramporecollege.R;
import com.lnsel.seramporecollege.utils.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentAttandanceAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();
    public StudentAttandanceAdapter(Context context,
                                    ArrayList<HashMap<String, String>> arraylist) {
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
            itemView = inflater.inflate(R.layout.attandance_list_item, null, false);
            holder = new MyViewHolder();
            holder.tv_date=(TextView) itemView.findViewById(R.id.tv_date);
            holder.tv_time=(TextView) itemView.findViewById(R.id.tv_time);
            holder.tv_status=(TextView) itemView.findViewById(R.id.tv_status);
            itemView.setTag(holder);
        } else {
            holder = (MyViewHolder) itemView.getTag();
        }
        resultp = data.get(position);


        holder.tv_date.setText( CommonMethod.dateConversion(resultp.get("attendence_date")));
        holder.tv_time.setText(resultp.get("time_slot"));
        if(resultp.get("attendance_status").equalsIgnoreCase("1")){
            holder.tv_status.setText("P");
            holder.tv_status.setTextColor(Color.parseColor("#19AF34"));
        }else {
            holder.tv_status.setText("A");
            holder.tv_status.setTextColor(Color.parseColor("#E82F29"));
        }

        return itemView;
    }
    private static class MyViewHolder {
        public TextView tv_date,tv_time,tv_status;
    }


    public void refresh(ArrayList<HashMap<String,String>> list){
        data = list;
        notifyDataSetChanged();
    }
}